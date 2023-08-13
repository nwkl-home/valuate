package com.nwklz.valuate.domain;

import com.nwklz.valuate.enums.TokenKind;
import lombok.Data;

@Data
public class ExpressionToken {
    private TokenKind kind = TokenKind.UNKNOWN;

    private Object value;

    public ExpressionToken() {}

    public ExpressionToken(TokenKind kind, Object value) {
        this.kind = kind;
        this.value = value;
    }
}
