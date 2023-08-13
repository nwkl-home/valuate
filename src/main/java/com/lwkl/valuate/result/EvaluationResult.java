package com.lwkl.valuate.result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluationResult {
    public Object result;
    public Object leftStage;
    public Object rightStage;
}
