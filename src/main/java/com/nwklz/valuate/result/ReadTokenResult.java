package com.nwklz.valuate.result;

import com.nwklz.valuate.domain.ExpressionToken;
import com.nwklz.valuate.enums.TokenKind;
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
