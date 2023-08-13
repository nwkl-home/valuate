package com.lwkl.valuate.operator;

import com.lwkl.valuate.domain.MapParameters;
import com.lwkl.valuate.result.EvaluationResult;
import lombok.Builder;

@Builder
public class EvaluationLiteralOperator implements Operator {

    private Object literal;

    public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception {
        return EvaluationResult.builder().result(literal).leftStage(leftStage).rightStage(rightStage).build();
    }
}
