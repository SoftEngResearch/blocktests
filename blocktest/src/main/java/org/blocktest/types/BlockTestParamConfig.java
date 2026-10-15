package org.blocktest.types;

import java.util.ArrayList;
import java.util.List;

import org.blocktest.BlockTest;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.Type;

public class BlockTestParamConfig extends BlockTest {
    
    // Variables are duplicates from BlockTest, might need to refactor later

    // Used from BlockTest Class
    //public Expression parameterValues;
    //public List<Type> parameterizedGivenTypes;

    public List<GivenParameterizedVariable> getParameterizedGivens(List<String> variableNames) {
        List<GivenParameterizedVariable> parameterizedGivens = new ArrayList<>();

        for (int i = 0; i < variableNames.size(); i++) {
            String variableName = variableNames.get(i);
            Type variableType = this.parameterizedGivenTypes.get(i);

            parameterizedGivens.add(new GivenParameterizedVariable(
                    variableType, variableName, new NameExpr(variableName), i));
        }
        return parameterizedGivens;
    }



}
