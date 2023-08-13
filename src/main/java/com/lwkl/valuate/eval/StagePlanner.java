package com.lwkl.valuate.eval;

import com.lwkl.valuate.stream.TokenStream;

public interface StagePlanner {

    public EvaluationStage plan(TokenStream stream) throws Exception;
}
