package org.blocktest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.tools.ExecFileLoader;

/**
 * This class takes a jacoco.exec file and outputs the coverage information for
 * each line in a specified range of a source file.
 */
public class CoverageChecker {
    private final String relpathToSrc;
    private final int startLine;
    private final int endLine;
    private final File execFile;
    private final File classesDir;
    private ExecFileLoader execFileLoader;

    /** Create a new checker based for the given project. */
    public CoverageChecker(final String relpathToSrc, final int startLine, final int endLine, final File execFile, final File classesDir) {
        this.relpathToSrc = relpathToSrc;
        this.startLine = startLine;
        this.endLine = endLine;
        this.execFile = execFile;
        this.classesDir = classesDir;
    }

    public void check(String outputFile) {
        execFileLoader = new ExecFileLoader();
        try {
            execFileLoader.load(execFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String className = relpathToSrc.replace("src/main/java/", "").replace(".java", "").replace(File.separatorChar, '.');
        // System.out.println("Target class: " + className);
        final CoverageBuilder coverageBuilder = new CoverageBuilder();
        final Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);
        try {
            analyzer.analyzeAll(classesDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        IClassCoverage targetClass = null;
        for (IClassCoverage icc : coverageBuilder.getClasses()) {
            // System.out.println("Class: " + icc.getName().replace('/', '.'));
            if (icc.getName().replace('/', '.').equals(className)) {
                targetClass = icc;
                break;
            }
        }

        Map<Integer, Integer> lineMap = new HashMap<>();
        if (targetClass != null) {
            for (int i = targetClass.getFirstLine(); i <= targetClass.getLastLine(); i++) {
                int status = targetClass.getLine(i).getStatus();
                lineMap.put(i, status);
            }
        }

        PrintWriter writer;
        try {
            writer = new PrintWriter(outputFile);
            writer.println("file,line,status");
            for (int i = startLine; i <= endLine; i++) {
                int status = lineMap.get(i);
                writer.println(relpathToSrc + "," + i + "," + getStatus(status));
            }
            writer.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private String getStatus(int status) {
        switch (status) {
            case ICounter.FULLY_COVERED:
                return "FULLY_COVERED";
            case ICounter.NOT_COVERED:
                return "NOT_COVERED";
            case ICounter.PARTLY_COVERED:
                return "PARTLY_COVERED";
            case ICounter.EMPTY:
                return "EMPTY";
            default:
                return "UNKNOWN";
        }
    }
}
