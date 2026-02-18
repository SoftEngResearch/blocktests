package org.blocktest.utils;

import java.util.*;

import com.github.javaparser.ast.expr.MethodCallExpr;

public class MethodCallPatternGenerator {
    private static class MethodInfo {
        String name;
        String args;  // Either original args or "(..)"
        String originalArgs; // Always the original arguments
        boolean hasArguments;
        boolean isScope; // True if this represents a scope (like variable name)

        MethodInfo(String name, String args, boolean hasArguments) {
            this(name, args, hasArguments, false);
        }

        MethodInfo(String name, String args, boolean hasArguments, boolean isScope) {
            this.name = name;
            this.args = args;
            this.originalArgs = args;
            this.hasArguments = hasArguments;
            this.isScope = isScope;
        }
    }

    public List<String> generateAllPatterns(MethodCallExpr methodCall) {
        // First, collect all method calls in the chain
        List<MethodInfo> methodChain = extractMethodChain(methodCall);

        // Generate all combinations
        List<String> patterns = new ArrayList<>();
        generateCombinations(methodChain, 0, new ArrayList<>(), patterns);

        return patterns;
    }

    private void generateCombinations(List<MethodInfo> methodChain, int index,
                                      List<MethodInfo> currentCombination,
                                      List<String> patterns) {
        if (index == methodChain.size()) {
            // We've processed all methods, build the pattern string
            patterns.add(buildPatternString(currentCombination));
            return;
        }

        MethodInfo currentMethod = methodChain.get(index);

        if (currentMethod.hasArguments) {
            // For methods with arguments, we have two choices:
            // 1. Keep original arguments
            currentCombination.add(new MethodInfo(currentMethod.name, currentMethod.originalArgs, true));
            generateCombinations(methodChain, index + 1, currentCombination, patterns);
            currentCombination.remove(currentCombination.size() - 1);

            // 2. Replace with (..)
            currentCombination.add(new MethodInfo(currentMethod.name, "(BLOCKTEST_ANYTHING)", true));
            generateCombinations(methodChain, index + 1, currentCombination, patterns);
            currentCombination.remove(currentCombination.size() - 1);
        } else {
            // For methods without arguments, only one choice: keep as is
            currentCombination.add(currentMethod);
            generateCombinations(methodChain, index + 1, currentCombination, patterns);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }

    private List<MethodInfo> extractMethodChain(MethodCallExpr methodCall) {
        List<MethodInfo> chain = new ArrayList<>();

        // Build the chain from the deepest call outward
        MethodCallExpr current = methodCall;
        while (current != null) {
            String methodName = current.getNameAsString();
            String args;
            boolean hasArgs;

            if (current.getArguments().isEmpty()) {
                args = "()";
                hasArgs = false;
            } else {
                // Build argument string
                StringBuilder argBuilder = new StringBuilder("(");
                for (int i = 0; i < current.getArguments().size(); i++) {
                    if (i > 0) argBuilder.append(", ");
                    argBuilder.append(current.getArguments().get(i).toString());
                }
                argBuilder.append(")");
                args = argBuilder.toString();
                hasArgs = true;
            }

            chain.add(0, new MethodInfo(methodName, args, hasArgs)); // Add to front

            // Move to the scope (previous method in chain)
            if (current.getScope().isPresent() && current.getScope().get() instanceof MethodCallExpr) {
                current = (MethodCallExpr) current.getScope().get();
            } else {
                // Handle non-method scope (like variable names)
                if (current.getScope().isPresent()) {
                    String scopeName = current.getScope().get().toString();
                    chain.add(0, new MethodInfo(scopeName, "", false, true)); // Mark as scope
                }
                break;
            }
        }

        return chain;
    }

    private String buildPatternString(List<MethodInfo> combination) {
        StringBuilder pattern = new StringBuilder();

        for (int i = 0; i < combination.size(); i++) {
            MethodInfo method = combination.get(i);

            if (i > 0 && !method.isScope) {
                pattern.append(".");
            }

            pattern.append(method.name);
            if (!method.isScope) {
                pattern.append(method.args);
            }
        }

        return pattern.toString();
    }
}
