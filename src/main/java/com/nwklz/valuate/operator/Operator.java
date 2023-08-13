package com.nwklz.valuate.operator;

import com.nwklz.valuate.domain.MapParameters;
import com.nwklz.valuate.result.EvaluationResult;

public interface Operator {

    EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception;
}
