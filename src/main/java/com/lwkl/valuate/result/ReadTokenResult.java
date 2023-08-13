package com.lwkl.valuate.result;

import com.lwkl.valuate.domain.ExpressionToken;
import com.lwkl.valuate.enums.TokenKind;
import lombok.Data;

@Data
public class ReadTokenResult {
    public ExpressionToken token;
    public TokenKind kind;

    public ReadTokenResult(TokenKind kind, Object tokenValue) {
        this.token = new ExpressionToken(kind, tokenValue);
        this.kind = kind;
    }
}
