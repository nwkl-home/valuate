package com.lwkl.valuate.operator;

import com.lwkl.valuate.check.TypeCheckStrategy;
import com.lwkl.valuate.domain.MapParameters;
import com.lwkl.valuate.function.Function;
import com.lwkl.valuate.result.EvaluationResult;
import lombok.Builder;

import java.util.Arrays;
import java.util.Collection;

@Builder
public class EvaluationFunctionOperator implements Operator {

    private Function expressionFunction;

    public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception {
        Object res = null;
        if (right == null) {
            res = expressionFunction.execute();
            return EvaluationResult.builder().result(res).leftStage(leftStage).rightStage(rightStage).build();
        }

        if (right.getClass().isArray()) {
            res = expressionFunction.execute((Object[]) right);
        }  else if (right instanceof Collection){
            res = expressionFunction.execute(right);
        } else {
            res = expressionFunction.execute(right);
        }
        return EvaluationResult.builder().result(res).leftStage(leftStage).rightStage(rightStage).build();
    }
}
