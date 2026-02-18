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
 * ALL lines in the entire project.
 */
public class CoverageChecker {
    private final File execFile;
    private final File classesDir;
    private ExecFileLoader execFileLoader;

    /** Create a new checker based for the given project. */
    public CoverageChecker(final File execFile, final File classesDir) {
        this.execFile = execFile;
        this.classesDir = classesDir;
    }

    public void check(String outputFile) {
        Map<String, Map<Integer, Integer>> covMap = new HashMap<>();
        execFileLoader = new ExecFileLoader();
        try {
            execFileLoader.load(execFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        final CoverageBuilder coverageBuilder = new CoverageBuilder();
        final Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);
        try {
            analyzer.analyzeAll(classesDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        for (IClassCoverage icc : coverageBuilder.getClasses()) {
            covMap.put(icc.getName().replace('/', '.'), new HashMap<>());
            for (int i = icc.getFirstLine(); i <= icc.getLastLine(); i++) {
                int status = icc.getLine(i).getStatus();
                covMap.get(icc.getName().replace('/', '.')).put(i, status);
            }
        }

        PrintWriter writer;
        try {
            writer = new PrintWriter(outputFile);
            writer.println("file,line,status");
            for (String className : covMap.keySet()) {
                for (int line : covMap.get(className).keySet()) {
                    int status = covMap.get(className).get(line);
                    writer.println(className + "," + line + "," + getStatus(status));
                }
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
