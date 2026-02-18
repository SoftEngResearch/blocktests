package org.blocktest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.Type;
import org.blocktest.types.EndAt;
import org.blocktest.types.Flow;
import org.blocktest.types.GivenVariable;
import org.checkerframework.checker.units.qual.A;

import static org.blocktest.types.EndAt.LAST_REFERENCE;

public class BlockTest implements Cloneable{
    public String testName;
    public int lineNo;
    public int startLineNo;
    public int endLineNo;
    public List<GivenVariable> givens;
    public List<String> givenVariables;
    public Set<String> renameGivenVariables;
    public BlockStmt setupBlock;
    public BlockStmt statements;
    public List<Node> junitAssertions;
    public List<Node> junitLambdaAssertions;
    public Set<String> assertedVariables;
    public Set<String> blockVariables;
    public Node exception;
    public HashMap<String, Type> localSymbolTable;
    public HashMap<String, Expression> localAssignments;
    public EndAt endAt;
    public int endAtInstances;
    public boolean endAfter;
    public boolean endReversed;
    public List<Flow> flows;
    public long delay;
    public Map<String, List<Expression>> mockMethods;

    public List<Expression> lambdaParameters;
    
    public String className = "";


    public BlockTest() {
        this.testName = "";
        this.givens = new ArrayList<>();
        this.givenVariables = new ArrayList<>();
        this.renameGivenVariables = new HashSet<>();
        this.setupBlock = null;
        this.statements = new BlockStmt();
        this.junitAssertions = new ArrayList<>();
        this.junitLambdaAssertions = new ArrayList<>();
        this.assertedVariables = new HashSet<>();
        this.blockVariables = new HashSet<>();
        this.exception = null;

        this.localSymbolTable = new HashMap<>();
        this.localAssignments = new HashMap<>();

        this.endAt = LAST_REFERENCE;
        this.endAtInstances = 1;
        this.endAfter = true;
        this.endReversed = false;
        this.flows = new ArrayList<>();
        this.delay = 0;
        this.mockMethods = new HashMap<>();
        this.lambdaParameters = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BlockTest(").append(testName).append(") at line ").append(lineNo).append(" {").append("\n");
        sb.append("Given:\n");
        for (GivenVariable given : givens) {
            sb.append("  ").append(given.toString()).append("\n");
        }

        sb.append("Statement:\n");
        for (Node stmt : statements.getStatements()) {
            sb.append("  ").append(stmt.toString()).append("\n");
        }

        sb.append("Assertions:\n");
        for (Node assertion : junitAssertions) {
            sb.append("  ").append(assertion.toString()).append("\n");
        }

        sb.append("Lambda Assertions:\n");
        for (Node assertion : junitLambdaAssertions) {
            sb.append("  ").append(assertion.toString()).append("\n");
        }

        sb.append("Given Variables:\n");
        for (String var : givenVariables) {
            sb.append("  - ").append(var).append("\n");
        }

        sb.append("Asserted Variables:\n");
        for (String var : assertedVariables) {
            sb.append("  - ").append(var).append("\n");
        }

        sb.append("End at ").append(endAt.toString()).append("\n");

        if (!flows.isEmpty()) {
            sb.append("Required flow:\n");
            for (Flow flow : flows) {
                sb.append(" -> ").append(flow.toString());
            }
            sb.append("\n");
        }
        sb.append("}\n");

        return sb.toString();
    }
    
    @Override
    public BlockTest clone() {
        try {
            BlockTest cloned = (BlockTest) super.clone();
            
            cloned.givens = new ArrayList<>(this.givens);
            cloned.givenVariables = new ArrayList<>(this.givenVariables);
            cloned.renameGivenVariables = new HashSet<>(this.renameGivenVariables);
            cloned.junitAssertions = new ArrayList<>(this.junitAssertions);
            cloned.junitLambdaAssertions = new ArrayList<>(this.junitLambdaAssertions);
            cloned.assertedVariables = new HashSet<>(this.assertedVariables);
            cloned.blockVariables = new HashSet<>(this.blockVariables);
            cloned.localSymbolTable = new HashMap<>(this.localSymbolTable);
            cloned.localAssignments = new HashMap<>(this.localAssignments);
            cloned.flows = new ArrayList<>(this.flows);
            cloned.mockMethods = new HashMap<>();
            for (Map.Entry<String, List<Expression>> entry : this.mockMethods.entrySet()) {
                cloned.mockMethods.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
            cloned.lambdaParameters = new ArrayList<>(this.lambdaParameters);
            
            if (this.setupBlock != null) {
                cloned.setupBlock = this.setupBlock.clone();
            }
            if (this.statements != null) {
                cloned.statements = this.statements.clone();
            }
            if (this.exception != null) {
                cloned.exception = this.exception.clone();
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); 
        }
    }
}
