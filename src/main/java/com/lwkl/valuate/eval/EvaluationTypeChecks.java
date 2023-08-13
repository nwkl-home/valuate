package com.lwkl.valuate.eval;

import com.lwkl.valuate.check.CombinedTypeCheck;
import com.lwkl.valuate.check.TypeCheck;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluationTypeChecks {
    private TypeCheck left;
    private TypeCheck right;
    private CombinedTypeCheck combined;
}
