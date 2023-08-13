package com.nwklz.valuate.domain;

import com.nwklz.valuate.enums.ErrorFormat;
import com.nwklz.valuate.enums.OperatorSymbol;
import com.nwklz.valuate.enums.TokenKind;
import com.nwklz.valuate.eval.StagePlanner;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class PrecedencePlanner {
    private Map<String, OperatorSymbol> validSymbols;
    private TokenKind[] validKinds;
    private ErrorFormat typeErrorFormat;

    private StagePlanner next;
    private StagePlanner nextRight;
}
