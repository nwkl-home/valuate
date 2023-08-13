package com.nwklz.valuate.eval;

import com.nwklz.valuate.check.CombinedTypeCheck;
import com.nwklz.valuate.check.TypeCheck;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluationTypeChecks {
    private TypeCheck left;
    private TypeCheck right;
    private CombinedTypeCheck combined;
}
