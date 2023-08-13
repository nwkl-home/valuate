package com.nwklz.valuate.operator;

import com.nwklz.valuate.domain.MapParameters;
import com.nwklz.valuate.result.EvaluationResult;
import lombok.Builder;

@Builder
public class EvaluationParameterOperator implements Operator {

    private String parameterName;

    public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception {
        return EvaluationResult.builder().result(parameters.get(parameterName)).leftStage(leftStage).rightStage(rightStage).build();
    }
}
