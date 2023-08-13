package com.lwkl.valuate.domain;

import com.lwkl.valuate.enums.ErrorFormat;
import com.lwkl.valuate.enums.OperatorSymbol;
import com.lwkl.valuate.enums.TokenKind;
import com.lwkl.valuate.eval.StagePlanner;
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
