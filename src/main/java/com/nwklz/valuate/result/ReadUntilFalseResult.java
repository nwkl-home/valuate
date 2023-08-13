package com.nwklz.valuate.result;

import lombok.Data;

@Data
public class ReadUntilFalseResult {

    private String result;
    private boolean conditioned;

    public ReadUntilFalseResult(String result, boolean conditioned) {
        this.result = result;
        this.conditioned = conditioned;
    }
}
