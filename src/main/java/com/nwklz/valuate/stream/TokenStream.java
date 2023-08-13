package com.nwklz.valuate.stream;

import com.nwklz.valuate.domain.ExpressionToken;
import lombok.Data;

import java.util.List;

@Data
public class TokenStream {
    private List<ExpressionToken> tokens;
    private int index;
    private int tokenLength;


    public TokenStream(List<ExpressionToken> tokens) {
        this.tokens = tokens;
        this.tokenLength = tokens.size();
    }

    public void rewind() {
        this.index -= 1;
    }

    public ExpressionToken next() {
        ExpressionToken token = this.tokens.get(this.index);
        this.index += 1;
        return token;
    }

    public boolean hasNext() {
        return this.index < this.tokenLength;
    }

}
