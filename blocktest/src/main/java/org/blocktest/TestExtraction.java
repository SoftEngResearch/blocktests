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

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;import com.github.javaparser.ast.stmt.ExpressionStmt;import com.github.javaparser.ast.type.VoidType;import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;import org.blocktest.types.StaticMethod;
import org.blocktest.utils.Util;
import org.blocktest.visitors.BlockTestConversionVisitor;
import org.blocktest.visitors.DeclarationRemovalVisitor;
import org.blocktest.visitors.ExtractionVisitor;
import org.blocktest.visitors.LambdaTransformationVisitor;
import org.blocktest.visitors.ThisFieldReplacementVisitor;

public class TestExtraction {

    public static Map<String, String> staticFields = new HashMap<>();
    public static Set<String> staticPrivateFields = new HashSet<>();

    static void extractTest(String inputFileSource, String testOutputFile, String testedClassName,
                            String className, int duplicatedTestCount, boolean coverage, boolean rewrite, boolean compile) {
        FileInputStream in;
        try {
            in = new FileInputStream(inputFileSource);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        List<BlockTest> blockTests = new ArrayList<>();
        List<StaticMethod> staticMethods = new ArrayList<>();
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

        ExtractionVisitor visitor = new ExtractionVisitor(blockTests);
        visitor.visit(cu, new ExtractionVisitor.Context());

        String packageName = null;
        if (cu.getPackageDeclaration().isPresent()) {
            packageName = cu.getPackageDeclaration().get().getNameAsString();
        }

        CompilationUnit newCU = new CompilationUnit();
        if (packageName != null && compile) {
            newCU.setPackageDeclaration(new PackageDeclaration(new Name(packageName)));
        }

        NodeList<ImportDeclaration> imports = cu.getImports();
        NodeList<ImportDeclaration> tmp = new NodeList<>();
        Set<String> inputStrings = imports.stream().map((NodeWithName::getNameAsString)).collect(Collectors.toSet());

        System.out.println("Compile is " + compile);
        if (!compile) {
            // remove non standard library's import
            for (ImportDeclaration dec : imports) {
                System.out.println("DEC is " + dec.getNameAsString());
                if (dec.getNameAsString().startsWith("java.")) {
                    tmp.add(dec);
                }
            }

            System.out.println("TMP:");
            System.out.println(tmp);
            imports = tmp;
        } else {
            // remove JUnit and TestNG
            for (ImportDeclaration dec : imports) {
                if (!dec.getNameAsString().startsWith("org.junit") && !dec.getNameAsString().startsWith("org.testng")) {
                    tmp.add(dec);
                }
            }
            imports = tmp;
        }

        NodeList<ImportDeclaration> testImports = new NodeList<>(imports);
        if (Util.junitVersion.equals("junit4")) {
            testImports.add(new ImportDeclaration("org.junit.Test", false, false));
            testImports.add(new ImportDeclaration("org.junit.Assert", true, true));
        } else if (Util.junitVersion.equals("testng")) {
            testImports.add(new ImportDeclaration("org.testng.annotations.Test", false, false));
            testImports.add(new ImportDeclaration("org.testng.Assert", true, true));
        } else {
            testImports.add(new ImportDeclaration("org.junit.jupiter.api.Test", false, false));
            testImports.add(new ImportDeclaration("org.junit.jupiter.api.Assertions", true, true));
        }

        if (!inputStrings.contains("org.blocktest.BTest")) {
            // If src imported BTest, we should not add it again
            testImports.add(new ImportDeclaration("org.blocktest.BTest", false, false));
        }

        if (compile) {
            if (packageName != null) {
                testImports.add(new ImportDeclaration(packageName + "." + testedClassName, true, true));
            } else {
                testImports.add(new ImportDeclaration(testedClassName, true, true));
            }
        }

        newCU.setImports(testImports);
        ClassOrInterfaceDeclaration testClass = newCU.addClass(className).setPublic(true);

        System.out.println("STATIC METHODS: " + staticMethods);

        Set<StaticMethod> usedStaticMethods = new HashSet<>();
        for (BlockTest blockTest : blockTests) {
            if (blockTest.testName.isEmpty()) {
                blockTest.testName = "testLine" + blockTest.lineNo;
            }

            System.out.println("Extracted BlockTest");
            BlockTest blockTestTmp = blockTest.clone();
            MethodDeclaration method = new TestConverter(visitor.globalSymbolTable).toJUnit(blockTest, coverage);
            if (coverage) {
                testClass.addMember(method.setPublic(true));
                method = new TestConverter(visitor.globalSymbolTable).toSourceCode(blockTestTmp);
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
}
