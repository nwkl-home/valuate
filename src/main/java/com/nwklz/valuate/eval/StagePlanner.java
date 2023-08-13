package com.nwklz.valuate.eval;

import com.nwklz.valuate.stream.TokenStream;

public interface StagePlanner {

    public EvaluationStage plan(TokenStream stream) throws Exception;
}
