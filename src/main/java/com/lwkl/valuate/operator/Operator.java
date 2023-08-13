package com.lwkl.valuate.operator;

import com.lwkl.valuate.domain.MapParameters;
import com.lwkl.valuate.result.EvaluationResult;

public interface Operator {

    EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception;
}
