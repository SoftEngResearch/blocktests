package org.blocktest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.Type;

import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import org.blocktest.types.StaticMethod;
import org.blocktest.types.BlockTestParamConfig;
import org.blocktest.utils.Constant;
import org.blocktest.utils.Util;
import org.blocktest.visitors.BlockTestConversionVisitor;
import org.blocktest.visitors.ExtractionVisitor;
import org.blocktest.visitors.LambdaTransformationVisitor;
import org.blocktest.visitors.ThisFieldReplacementVisitor;

public class TestExtractor {

    public static Map<String, String> staticFields = new HashMap<>();
    public static Set<String> staticPrivateFields = new HashSet<>();

    String inputFileSource;
    //String testOutputFile;
    String testedClassName;
    //String className; 
    //int duplicatedTestCount; 
    boolean coverage; 
    boolean rewrite; 
    boolean compile;

    String packageName = null;
    NodeList<ImportDeclaration> imports = null;
    List<StaticMethod> staticMethods = new ArrayList<>();

    public HashMap<String, HashSet<Type>> globalSymbolTable = new HashMap<>();

    Map<String, BlockTestParamConfig> blockTestConfigs = new HashMap<>();
    List<BlockTest> blockTests = new ArrayList<BlockTest>();

    public TestExtractor(String inputFileSource, String testedClassName, 
                                boolean coverage, boolean rewrite, boolean compile) {

        this.inputFileSource = inputFileSource;
        this.testedClassName = testedClassName;
        this.coverage = coverage;
        this.rewrite = rewrite;
        this.compile = compile;
    }

    public void extractBlockTests() {
        FileInputStream in;
        try {
            in = new FileInputStream(this.inputFileSource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        CompilationUnit cu = StaticJavaParser.parse(in); // parse input file
        CompilationUnit originalCU = cu.clone();
        boolean changedToPublic = false; // If changedToPublic is true, this mean we need to update the original file

        // Collect all static methods
        for (MethodDeclaration method : cu.findAll(MethodDeclaration.class, MethodDeclaration::isStatic)) {
            Optional<String> name = method.findAncestor(ClassOrInterfaceDeclaration.class)
                    .map(ClassOrInterfaceDeclaration::getNameAsString);
            if (name.isPresent()) {
                String klassName = name.get();
                String methodName = method.getNameAsString();
                int argumentCount = method.getParameters().size();
                this.staticMethods.add(new StaticMethod(klassName, methodName, argumentCount));
            }
        }
        
        // Collect all static variables
        originalCU.findAll(ClassOrInterfaceDeclaration.class).forEach(cls -> {
            String classNameTmp = cls.getNameAsString();
            cls.findAll(FieldDeclaration.class).forEach(fd -> {
                if (fd.hasModifier(Modifier.Keyword.STATIC)) {
                    if (fd.isPrivate()) {
                        staticPrivateFields.add(classNameTmp);
                        fd.setPrivate(false);
                    }
                    fd.getVariables().forEach(v -> staticFields.put(v.getNameAsString(), classNameTmp));
                }
            });
        });

        // Replace implicit static field accesses with explicit ones (staticField -> ClassName.staticField)
        cu.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(NameExpr n, Void arg) {
                super.visit(n, arg);

                String name = n.getNameAsString();
                if (staticFields.containsKey(name)) {
                    String className = staticFields.get(name);
                    n.replace(new FieldAccessExpr(new NameExpr(className), name));
                }
            }
        }, null);


        cu.accept(new BlockTestConversionVisitor(), null);

        cu.accept(new LambdaTransformationVisitor(), null);

        cu.accept(new ThisFieldReplacementVisitor(), null);

        ExtractionVisitor visitor = new ExtractionVisitor(this.blockTests);
        visitor.visit(cu, new ExtractionVisitor.Context());

        if (cu.getPackageDeclaration().isPresent()) {
            this.packageName = cu.getPackageDeclaration().get().getNameAsString();
        }

        this.imports = cu.getImports();
        NodeList<ImportDeclaration> tmp = new NodeList<>();
        Set<String> inputStrings = this.imports.stream().map((NodeWithName::getNameAsString)).collect(Collectors.toSet());

        System.out.println("Compile is " + this.compile);
        if (!this.compile) {
            // remove non standard library's import
            for (ImportDeclaration dec : this.imports) {
                System.out.println("DEC is " + dec.getNameAsString());
                if (dec.getNameAsString().startsWith("java.")) {
                    tmp.add(dec);
                }
            }

            System.out.println("TMP:");
            System.out.println(tmp);
            this.imports = tmp;
        } else {
            // remove JUnit and TestNG
            for (ImportDeclaration dec : this.imports) {
                if (!dec.getNameAsString().startsWith("org.junit") && !dec.getNameAsString().startsWith("org.testng")
                    && !dec.getNameAsString().startsWith("net.jqwik")) {
                    tmp.add(dec);
                }
            }
            this.imports = tmp;
        }

        NodeList<ImportDeclaration> testImports = new NodeList<>(imports);
        if (Util.junitVersion.equals("junit4")) {
            testImports.add(new ImportDeclaration("org.junit.Test", false, false));
            testImports.add(new ImportDeclaration("org.junit.Assert", true, true));
            testImports.add(new ImportDeclaration("org.junit.runners.Parameterized", false, false));
            testImports.add(new ImportDeclaration(" org.junit.runners.Parameterized.Parameter", false, false));
            testImports.add(new ImportDeclaration("org.junit.runner.RunWith", false, false));
            testImports.add(new ImportDeclaration("java.util.Collection", false, false));
        } else if (Util.junitVersion.equals("testng")) {
            testImports.add(new ImportDeclaration("org.testng.annotations.Test", false, false));
            testImports.add(new ImportDeclaration("org.testng.Assert", true, true));
        } else if (Util.junitVersion.equals("jqwik")) {
            testImports.add(new ImportDeclaration("org.junit.jupiter.api.Assertions", true, true));
            if (this.blockTests.stream().anyMatch(blockTest -> !blockTest.givens.isEmpty())) {
                testImports.add(new ImportDeclaration("net.jqwik.api.Example", false, false));
            }
            if (this.blockTests.stream().anyMatch(blockTest -> !blockTest.givenAlls.isEmpty())) {
                testImports.add(new ImportDeclaration("net.jqwik.api.ForAll", false, false));
                testImports.add(new ImportDeclaration("net.jqwik.api.Property", false, false));
            }
        } else {
            testImports.add(new ImportDeclaration("org.junit.jupiter.api.Test", false, false));
            testImports.add(new ImportDeclaration("org.junit.jupiter.api.Assertions", true, true));
        }

        if (!inputStrings.contains("org.blocktest.BTest")) {
            // If src imported BTest, we should not add it again
            testImports.add(new ImportDeclaration("org.blocktest.BTest", false, false));
        }

        if (this.compile) {
            if (this.packageName != null) {
                testImports.add(new ImportDeclaration(this.packageName + "." + this.testedClassName, true, true));
            } else {
                testImports.add(new ImportDeclaration(this.testedClassName, true, true));
            }
        }

        this.imports = testImports;

        System.out.println("STATIC METHODS: " + this.staticMethods);

        this.globalSymbolTable = visitor.globalSymbolTable;

        java.util.Iterator<BlockTest> blockTestIterator = this.blockTests.iterator();
        while (blockTestIterator.hasNext()) {
            BlockTest blockTest = blockTestIterator.next();
            if (blockTest.testName.isEmpty()) {
                blockTest.testName = "testLine" + blockTest.lineNo;
            }

            if (!staticPrivateFields.isEmpty()) {
                // We convert all private static fields to non-private
                changedToPublic = true;
            }

            if (this.rewrite) {
                originalCU.findAll(ExpressionStmt.class).forEach(stmt -> {
                    if (stmt.getExpression() instanceof MethodCallExpr) {
                        MethodCallExpr call = (MethodCallExpr) stmt.getExpression();
                        if (Util.isTestStatement(call)) {
                            System.out.println("FOUND AND REMOVE BLOCKTEST");
                            stmt.remove();
                        }
                    }
                });
            }

            if (changedToPublic || this.coverage || this.rewrite) {
                try {
                    Path path = Paths.get(this.inputFileSource);
                    Files.write(path, originalCU.toString().getBytes());
                    System.out.println("Writing to " + this.inputFileSource);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void extractBlockTestConfigs() {
        java.util.Iterator<BlockTest> blockTestIterator = blockTests.iterator();

        if (this.blockTestConfigs == null) {
            this.blockTestConfigs = new java.util.HashMap<>();
        }

        while (blockTestIterator.hasNext()) {
            BlockTest blockTest = blockTestIterator.next();

            if (blockTest.is_config) {
                System.out.println("Extracted Config BlockTest");

                BlockTest blockTestTmp = blockTest.clone();

                if (blockTestTmp == null || blockTestTmp.configs == null) {
                    throw new IllegalStateException(
                        "BlockTest clone or configs is null"
                    );
                }

                this.blockTestConfigs.putAll(blockTestTmp.configs);

                blockTestIterator.remove();
            }
        }
    }

    public void applyBlockTestConfigs() {
        java.util.Iterator<BlockTest> blockTestIterator = blockTests.iterator();

        while (blockTestIterator.hasNext()) {
            BlockTest blockTest = blockTestIterator.next();

            if (blockTest.is_configed) {
                BlockTestParamConfig blockTestConfig =
                        this.blockTestConfigs.get(blockTest.label);

                if (blockTestConfig == null) {
                    throw new IllegalStateException(
                        "No configuration found for BlockTest label: "
                        + blockTest.label
                    );
                }

                if (blockTest.parameterizedGivenVariables.size() != blockTestConfig.parameterizedGivenTypes.size()) {
                    throw new RuntimeException(Constant.PARAMETERIZED
                        + " number of parameterized variables is not equal to number of columns or types in parameters");
                }

                blockTest.parameterValues = blockTestConfig.parameterValues;
                blockTest.parameterizedGivens = blockTestConfig.getParameterizedGivens(blockTest.parameterizedGivenVariables);
            }
        }
    }

    // The way this function executes depends a lot on the version of junit etc we should refactor to call 
    // specific function as needed for each tasting framework
    public void extractTestsToSameClass(String testOutputFile, String className, int duplicatedTestCount) {
        FileInputStream in;
        try {
            in = new FileInputStream(this.inputFileSource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        CompilationUnit cu = StaticJavaParser.parse(in); // parse input file
        CompilationUnit originalCU = cu.clone();
        boolean changedToPublic = false; // If changedToPublic is true, this mean we need to update the original file

        // Collect all static methods
        for (MethodDeclaration method : cu.findAll(MethodDeclaration.class, MethodDeclaration::isStatic)) {
            Optional<String> name = method.findAncestor(ClassOrInterfaceDeclaration.class)
                    .map(ClassOrInterfaceDeclaration::getNameAsString);
            if (name.isPresent()) {
                String klassName = name.get();
                String methodName = method.getNameAsString();
                int argumentCount = method.getParameters().size();
                staticMethods.add(new StaticMethod(klassName, methodName, argumentCount));
            }
        }
        
        // Collect all static variables
        originalCU.findAll(ClassOrInterfaceDeclaration.class).forEach(cls -> {
            String classNameTmp = cls.getNameAsString();
            cls.findAll(FieldDeclaration.class).forEach(fd -> {
                if (fd.hasModifier(Modifier.Keyword.STATIC)) {
                    if (fd.isPrivate()) {
                        staticPrivateFields.add(classNameTmp);
                        fd.setPrivate(false);
                    }
                    fd.getVariables().forEach(v -> staticFields.put(v.getNameAsString(), classNameTmp));
                }
            });
        });

        // Replace implicit static field accesses with explicit ones (staticField -> ClassName.staticField)
        cu.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(NameExpr n, Void arg) {
                super.visit(n, arg);

                String name = n.getNameAsString();
                if (staticFields.containsKey(name)) {
                    String className = staticFields.get(name);
                    n.replace(new FieldAccessExpr(new NameExpr(className), name));
                }
            }
        }, null);

        cu.accept(new BlockTestConversionVisitor(), null);

        cu.accept(new LambdaTransformationVisitor(), null);

        cu.accept(new ThisFieldReplacementVisitor(), null);

        if (cu.getPackageDeclaration().isPresent()) {
            packageName = cu.getPackageDeclaration().get().getNameAsString();
        }

        CompilationUnit newCU = new CompilationUnit();
        if (packageName != null && compile) {
            newCU.setPackageDeclaration(new PackageDeclaration(new Name(packageName)));
        }

        System.out.println("Compile is " + compile);

        newCU.setImports(this.imports);
        ClassOrInterfaceDeclaration testClass = newCU.addClass(className+"BlockTest").setPublic(true);

        System.out.println("STATIC METHODS: " + staticMethods);

        Set<StaticMethod> usedStaticMethods = new HashSet<>();

        java.util.Iterator<BlockTest> blockTestIterator = blockTests.iterator();
        while (blockTestIterator.hasNext()) {
            BlockTest blockTest = blockTestIterator.next();

            // In the future we might have this filter to handle "unlabeled" block tests
            // This function `extractTest` can also return the block tests that still need test extraction
            // but to their own java class
            // Change this to "if there are no configs associated with a test (no labels)." 
            // Filters out non-vanilla blocktests (i.e. Config. BT and Param. BT in junit4)
            if (!(blockTest.is_parameterized && Util.junitVersion.equals("junit4"))) {
                
                System.out.println("Extracted BlockTest");
                BlockTest blockTestTmp = blockTest.clone();
                MethodDeclaration method = new TestConverter(this.globalSymbolTable).toJUnit(blockTest, coverage);

                if (coverage) {
                    testClass.addMember(method.setPublic(true));
                    method = new TestConverter(this.globalSymbolTable).toSourceCode(blockTestTmp);
                    System.out.println("toSrcCode");
                    System.out.println(method);
                }

                // replace implicit static method calls with direct static method calls
                for (StaticMethod sm : staticMethods) {
                    for (MethodCallExpr call : method.findAll(MethodCallExpr.class,
                            mc -> mc.getNameAsString().equals(sm.methodName) &&
                                    !mc.getScope().isPresent() && mc.getArguments().size() == sm.argumentCount)) {
                        System.out.println("Replacing static method call: " + sm);
                        MethodCallExpr replacement = new MethodCallExpr(
                                new NameExpr(sm.className), // scope
                                sm.methodName,
                                call.getArguments()
                        );
                        call.replace(replacement);
                        usedStaticMethods.add(sm);
                    }
                }
            
                if (coverage) {
                    MethodDeclaration method2 = method.setPublic(true).clone();
                    originalCU.findAll(ClassOrInterfaceDeclaration.class).forEach(cls -> {
                        String classNameTmp = cls.getNameAsString();
                        System.out.println("Coverage mode: found " + blockTest.className + " vs " + classNameTmp);
                        cls.addMember(method2);
                    });
                } else {
                    testClass.addMember(method.setPublic(true));
                }
            
                if (duplicatedTestCount > 1) {
                    for (int i = 1; i < duplicatedTestCount; i++) {
                        MethodDeclaration methodDup = method.clone().setPublic(true);
                        methodDup.setName(methodDup.getNameAsString() + "_dup" + i);
                        testClass.addMember(methodDup);
                    }
                }
                blockTestIterator.remove();
            } else {
                // Because all JUNIT4 tests in the same class
                // share the same parameters we need to create a new 
                // test class for each block assuming blocks do not share parameters
                continue;
            }
        }

        if (duplicatedTestCount > 1) {
            // Generate an empty method
             MethodDeclaration method = new MethodDeclaration();
             method.setPublic(true);
             method.setName("testEmptyForDuplicateExp").setType(new VoidType()).setBody(new BlockStmt()).addMarkerAnnotation("Test");
             testClass.addMember(method.setPublic(true));
        }

        if (!usedStaticMethods.isEmpty()) {
            for (MethodDeclaration method : originalCU.findAll(MethodDeclaration.class, MethodDeclaration::isStatic)) {
                Optional<String> name = method.findAncestor(ClassOrInterfaceDeclaration.class)
                        .map(ClassOrInterfaceDeclaration::getNameAsString);
                if (name.isPresent()) {
                    String klassName = name.get();
                    String methodName = method.getNameAsString();
                    int argumentCount = method.getParameters().size();

                    for (StaticMethod sm : usedStaticMethods) {
                        if (sm.className.equals(klassName) && sm.methodName.equals(methodName)
                                && sm.argumentCount == argumentCount && !method.isPublic()) {
                            System.out.println("Making static method " + sm + " public");
                            method.setPrivate(false);
                            changedToPublic = true;
                        }
                    }
                }
            }
        }

        if (!staticPrivateFields.isEmpty()) {
            // We convert all private static fields to non-private
            changedToPublic = true;
        }

        // Copy static methods to newCU
//        List<MethodDeclaration> staticMethods =
//                originalCU.findAll(MethodDeclaration.class, MethodDeclaration::isStatic);
//        for (MethodDeclaration method : staticMethods) {
//            testClass.addMember(method.clone());
//        }
//
//        // Copy static variables to newCU
//        List<FieldDeclaration> staticFields =
//                originalCU.findAll(FieldDeclaration.class, FieldDeclaration::isStatic);
//        for (FieldDeclaration field : staticFields) {
//            testClass.addMember(field.clone());
//        }

        System.out.println("\n===== Generated JUnit Test Class =====");
        System.out.println(newCU);

        if (testOutputFile == null || testOutputFile.isEmpty()) {
            System.out.println("No output file specified, skipping file write.");
            return;
        }
        
        if (rewrite) {
            originalCU.findAll(ExpressionStmt.class).forEach(stmt -> {
                if (stmt.getExpression() instanceof MethodCallExpr) {
                    MethodCallExpr call = (MethodCallExpr) stmt.getExpression();
                    if (Util.isTestStatement(call)) {
                        System.out.println("FOUND AND REMOVE BLOCKTEST");
                        stmt.remove();
                    }
                }
            });
        }

        try {
            Path path = Paths.get(testOutputFile);
            Files.createDirectories(path.getParent());
            Files.write(path, newCU.toString().getBytes());
            System.out.println("Writing to " + testOutputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (changedToPublic || coverage || rewrite) {
            try {
                Path path = Paths.get(inputFileSource);
                Files.write(path, originalCU.toString().getBytes());
                System.out.println("Writing to " + inputFileSource);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void extractTestsToNewClasses(String testOutputFile, String className, int duplicatedTestCount) {
        FileInputStream in;
        try {
            in = new FileInputStream(inputFileSource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        CompilationUnit cu = StaticJavaParser.parse(in); // parse input file
        CompilationUnit originalCU = cu.clone();
        boolean changedToPublic = false; // If changedToPublic is true, this mean we need to update the original file

        // Collect all static methods
        for (MethodDeclaration method : cu.findAll(MethodDeclaration.class, MethodDeclaration::isStatic)) {
            Optional<String> name = method.findAncestor(ClassOrInterfaceDeclaration.class)
                    .map(ClassOrInterfaceDeclaration::getNameAsString);
            if (name.isPresent()) {
                String klassName = name.get();
                String methodName = method.getNameAsString();
                int argumentCount = method.getParameters().size();
                staticMethods.add(new StaticMethod(klassName, methodName, argumentCount));
            }
        }
        
        // Collect all static variables
        originalCU.findAll(ClassOrInterfaceDeclaration.class).forEach(cls -> {
            String classNameTmp = cls.getNameAsString();
            cls.findAll(FieldDeclaration.class).forEach(fd -> {
                if (fd.hasModifier(Modifier.Keyword.STATIC)) {
                    if (fd.isPrivate()) {
                        staticPrivateFields.add(classNameTmp);
                        fd.setPrivate(false);
                    }
                    fd.getVariables().forEach(v -> staticFields.put(v.getNameAsString(), classNameTmp));
                }
            });
        });

        // Replace implicit static field accesses with explicit ones (staticField -> ClassName.staticField)
        cu.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(NameExpr n, Void arg) {
                super.visit(n, arg);

                String name = n.getNameAsString();
                if (staticFields.containsKey(name)) {
                    String className = staticFields.get(name);
                    n.replace(new FieldAccessExpr(new NameExpr(className), name));
                }
            }
        }, null);

        cu.accept(new BlockTestConversionVisitor(), null);

        cu.accept(new LambdaTransformationVisitor(), null);

        cu.accept(new ThisFieldReplacementVisitor(), null);

        //String packageName = null;
        if (cu.getPackageDeclaration().isPresent()) {
            packageName = cu.getPackageDeclaration().get().getNameAsString();
        }

        for (BlockTest blockTest : this.blockTests) {
            CompilationUnit newCU = new CompilationUnit();
            if (packageName != null && compile) {
                newCU.setPackageDeclaration(new PackageDeclaration(new Name(packageName)));
            }

            System.out.println("Compile is " + compile);

            newCU.setImports(this.imports);
            ClassOrInterfaceDeclaration testClass = newCU.addClass(className+"BlockTest"+blockTest.testName).setPublic(true);

            System.out.println("STATIC METHODS: " + staticMethods);

            Set<StaticMethod> usedStaticMethods = new HashSet<>();
            if (packageName != null && compile) {
                newCU.setPackageDeclaration(new PackageDeclaration(new Name(packageName)));
            }

            ParseResult<AnnotationExpr> result =
                new JavaParser().parseAnnotation("@RunWith(Parameterized.class)");

            testClass.addAnnotation(result.getResult().get());

            System.out.println("Extracted BlockTest");
            BlockTest blockTestTmp = blockTest.clone();

            List<BodyDeclaration<?>> declarations =
                new TestConverter(this.globalSymbolTable)
                    .toParamJUnit(blockTest, coverage);

            for (BodyDeclaration<?> declaration : declarations) {
                testClass.addMember(declaration);
            }
            MethodDeclaration method = new TestConverter(this.globalSymbolTable)
                                           .toJUnit(blockTest, coverage);
            if (coverage) {
                testClass.addMember(method.setPublic(true));
                method = new TestConverter(this.globalSymbolTable).toSourceCode(blockTestTmp);
                System.out.println("toSrcCode");
                System.out.println(method);
            }

            // replace implicit static method calls with direct static method calls
            for (StaticMethod sm : staticMethods) {
                for (MethodCallExpr call : method.findAll(MethodCallExpr.class,
                        mc -> mc.getNameAsString().equals(sm.methodName) &&
                                !mc.getScope().isPresent() && mc.getArguments().size() == sm.argumentCount)) {
                    System.out.println("Replacing static method call: " + sm);
                    MethodCallExpr replacement = new MethodCallExpr(
                            new NameExpr(sm.className), // scope
                            sm.methodName,
                            call.getArguments()
                    );
                    call.replace(replacement);
                    usedStaticMethods.add(sm);
                }
            }
        
            if (coverage) {
                MethodDeclaration method2 = method.setPublic(true).clone();
                originalCU.findAll(ClassOrInterfaceDeclaration.class).forEach(cls -> {
                    String classNameTmp = cls.getNameAsString();
                    System.out.println("Coverage mode: found " + blockTest.className + " vs " + classNameTmp);
                    cls.addMember(method2);
                });
            } else {
                testClass.addMember(method.setPublic(true));
            }
        
            if (duplicatedTestCount > 1) {
                for (int i = 1; i < duplicatedTestCount; i++) {
                    MethodDeclaration methodDup = method.clone().setPublic(true);
                    methodDup.setName(methodDup.getNameAsString() + "_dup" + i);
                    testClass.addMember(methodDup);
                }
            }

            if (duplicatedTestCount > 1) {
                // Generate an empty method
                 MethodDeclaration paramMethod = new MethodDeclaration();
                 paramMethod.setPublic(true);
                 paramMethod.setName("testEmptyForDuplicateExp").setType(new VoidType()).setBody(new BlockStmt()).addMarkerAnnotation("Test");
                 testClass.addMember(method.setPublic(true));
            }

            if (!usedStaticMethods.isEmpty()) {
                for (MethodDeclaration paramMethod : originalCU.findAll(MethodDeclaration.class, MethodDeclaration::isStatic)) {
                    Optional<String> name = paramMethod.findAncestor(ClassOrInterfaceDeclaration.class)
                            .map(ClassOrInterfaceDeclaration::getNameAsString);
                    if (name.isPresent()) {
                        String klassName = name.get();
                        String methodName = method.getNameAsString();
                        int argumentCount = method.getParameters().size();

                        for (StaticMethod sm : usedStaticMethods) {
                            if (sm.className.equals(klassName) && sm.methodName.equals(methodName)
                                    && sm.argumentCount == argumentCount && !method.isPublic()) {
                                System.out.println("Making static method " + sm + " public");
                                method.setPrivate(false);
                                changedToPublic = true;
                            }
                        }
                    }
                }
            }

            if (!staticPrivateFields.isEmpty()) {
                // We convert all private static fields to non-private
                changedToPublic = true;
            }

            // Copy static methods to newCU
//            List<MethodDeclaration> staticMethods =
//                    originalCU.findAll(MethodDeclaration.class, MethodDeclaration::isStatic);
//            for (MethodDeclaration method : staticMethods) {
//                testClass.addMember(method.clone());
//            }
//
//            // Copy static variables to newCU
//            List<FieldDeclaration> staticFields =
//                    originalCU.findAll(FieldDeclaration.class, FieldDeclaration::isStatic);
//            for (FieldDeclaration field : staticFields) {
//                testClass.addMember(field.clone());
//            }

            System.out.println("\n===== Generated JUnit Test Class =====");
            System.out.println(newCU);

            String paramTestOutputFile;
            if (testOutputFile == null || testOutputFile.isEmpty()) {
                System.out.println("No output file specified, skipping file write.");
                continue;
            } else {
                paramTestOutputFile = testOutputFile.replace(".java", "")+blockTest.testName + ".java";
            }
        
            if (rewrite) {
                originalCU.findAll(ExpressionStmt.class).forEach(stmt -> {
                    if (stmt.getExpression() instanceof MethodCallExpr) {
                        MethodCallExpr call = (MethodCallExpr) stmt.getExpression();
                        if (Util.isTestStatement(call)) {
                            System.out.println("FOUND AND REMOVE BLOCKTEST");
                            stmt.remove();
                        }
                    }
                });
            }

            try {
                Path path = Paths.get(paramTestOutputFile);
                Files.createDirectories(path.getParent());
                Files.write(path, newCU.toString().getBytes());
                System.out.println("Writing to " + paramTestOutputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (changedToPublic || coverage || rewrite) {
                try {
                    Path path = Paths.get(inputFileSource);
                    Files.write(path, originalCU.toString().getBytes());
                    System.out.println("Writing to " + inputFileSource);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
