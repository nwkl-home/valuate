package com.lwkl.valuate;

import com.lwkl.valuate.domain.ExpressionToken;
import com.lwkl.valuate.domain.MapParameters;
import com.lwkl.valuate.eval.EvaluationStage;
import com.lwkl.valuate.eval.EvaluationStagePlanner;
import com.lwkl.valuate.function.Function;
import com.lwkl.valuate.parse.Parse;
import com.lwkl.valuate.result.EvaluationResult;
import com.lwkl.valuate.state.LexerState;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class EvaluableExpression {
    private final static String ISODATEFORMAT = "2006-01-02T15:04:05.999999999Z0700";
    private final String queryDateFormat;
    private final boolean checksTypes;
    private final List<ExpressionToken> tokens;
    private final EvaluationStage evaluationStages;
    private final String inputExpression;

    private final Integer shortCircuitHolder = -1;

    public EvaluableExpression(String expression) throws Exception {
        this.queryDateFormat = ISODATEFORMAT;
        this.inputExpression = expression;

        this.tokens = Parse.parseTokens(expression, new HashMap<String, Function>());

        Parse.checkBalance(this.tokens);

        LexerState.checkExpressionSyntax(this.tokens);

        Parse.optimizeTokens(this.tokens);

        this.evaluationStages = EvaluationStagePlanner.planStages(this.tokens);

        this.checksTypes = true;
    }

    public EvaluableExpression(String expression, Map<String, Function> functions) throws Exception {
        this.queryDateFormat = ISODATEFORMAT;
        this.inputExpression = expression;

        this.tokens = Parse.parseTokens(expression, functions);

        Parse.checkBalance(this.tokens);

        LexerState.checkExpressionSyntax(this.tokens);

        Parse.optimizeTokens(this.tokens);

        this.evaluationStages = EvaluationStagePlanner.planStages(this.tokens);

        this.checksTypes = true;
    }

    public Object evaluate(Map<String, Object> parameters) throws Exception {

        if (parameters == null) {
            return this.eval(null);
        }

        return this.eval(MapParameters.builder().parameters(parameters).build());
    }

    private Object eval(MapParameters parameters) throws Exception {

        if (this.evaluationStages == null) {
            return null;
        }

        if (parameters == null) {
            parameters = MapParameters.builder().parameters(new HashMap<String, Object>()).build();
        }
        EvaluationResult result = this.evaluateStage(this.evaluationStages, parameters);
        return result.result;
    }

    private EvaluationResult evaluateStage(EvaluationStage stage, MapParameters parameters) throws Exception {

        Object left = null, right = null, leftStageValue = null, rightStageValue = null;

        if (stage.getLeftStage() != null) {
            EvaluationResult result = this.evaluateStage(stage.getLeftStage(), parameters);
            left = result.getResult();
            leftStageValue = result.getLeftStage();
        }

        if (stage.isShortCircuitable()) {
            switch (stage.getSymbol()) {
                case AND: {
                    if (Boolean.FALSE.equals(left)){
                        return EvaluationResult.builder().result(false).build();
                    }
                    break;
                }
                case OR: {
                    if (Boolean.TRUE.equals(left)){
                        return EvaluationResult.builder().result(true).build();
                    }
                    break;
                }
                case COALESCE: {
                    if (left != null) {
                        return EvaluationResult.builder().result(left).build();
                    }
                    break;
                }
                case TERNARY_TRUE: {
                    if (Boolean.FALSE.equals(left)){
                        right = shortCircuitHolder;
                    }
                    break;
                }
                case TERNARY_FALSE: {
                    if (left != null) {
                        right = shortCircuitHolder;
                    }
                    break;
                }
            }
        }

        if (!shortCircuitHolder.equals(right) && stage.getRightStage() != null) {
            EvaluationResult result =  this.evaluateStage(stage.getRightStage(), parameters);
            right = result.getResult();
            rightStageValue = result.getRightStage();
        }

        if (this.checksTypes) {
            if (stage.getCombinedTypeCheck() == null) {

                EvaluationStagePlanner.typeCheck(stage.getLeftTypeCheck(), left, stage.getSymbol(), stage.getTypeErrorFormat());

                EvaluationStagePlanner.typeCheck(stage.getRightTypeCheck(), right, stage.getSymbol(), stage.getTypeErrorFormat());
            } else {
                if (!stage.getCombinedTypeCheck().check(left, right)) {
                    throw new Exception(String.format(stage.getTypeErrorFormat().getFormat(), left, stage.getSymbol().toString()));
                }
            }
        }

        return stage.getOperator().operator(left, right, leftStageValue, rightStageValue, parameters);
    }
}