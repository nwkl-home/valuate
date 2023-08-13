package com.nwklz.valuate.eval;

import com.nwklz.valuate.check.TypeCheck;
import com.nwklz.valuate.check.CombinedTypeCheckStrategy;
import com.nwklz.valuate.check.TypeCheckStrategy;
import com.nwklz.valuate.domain.ExpressionToken;
import com.nwklz.valuate.domain.PrecedencePlanner;
import com.nwklz.valuate.enums.ErrorFormat;
import com.nwklz.valuate.enums.OperatorPrecedence;
import com.nwklz.valuate.enums.OperatorSymbol;
import com.nwklz.valuate.enums.TokenKind;
import com.nwklz.valuate.function.Function;
import com.nwklz.valuate.operator.*;
import com.nwklz.valuate.result.EvaluationResult;
import com.nwklz.valuate.stream.TokenStream;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder
public class EvaluationStagePlanner implements StagePlanner {

    public static final Map<OperatorSymbol, Operator> STAGESYMBOLMAP = new HashMap<OperatorSymbol, Operator>();

    static {
        STAGESYMBOLMAP.put(OperatorSymbol.EQ, EvaluationOperator.EQUAL_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.NEQ, EvaluationOperator.NOT_EQUAL_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.GT, EvaluationOperator.GT_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.LT, EvaluationOperator.LT_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.GTE, EvaluationOperator.GTE_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.LTE, EvaluationOperator.LTE_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.REQ, EvaluationOperator.REGEX_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.NREQ, EvaluationOperator.NOT_REGEX_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.AND, EvaluationOperator.AND_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.OR, EvaluationOperator.OR_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.IN, EvaluationOperator.IN_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.BITWISE_OR, EvaluationOperator.BITWISE_OR_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.BITWISE_AND, EvaluationOperator.BITWISE_AND_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.BITWISE_XOR, EvaluationOperator.BITWISE_XOR_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.BITWISE_LSHIFT, EvaluationOperator.LEFT_SHIFT_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.BITWISE_RSHIFT, EvaluationOperator.RIGHT_SHIFT_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.PLUS, EvaluationOperator.ADD_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.MINUS, EvaluationOperator.SUBTRACT_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.MULTIPLY, EvaluationOperator.MULTIPLY_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.DIVIDE, EvaluationOperator.DIVIDE_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.MODULUS, EvaluationOperator.MODULUS_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.EXPONENT, EvaluationOperator.EXPONENT_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.NEGATE, EvaluationOperator.NEGATE_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.INVERT, EvaluationOperator.INVERT_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.BITWISE_NOT, EvaluationOperator.BITWISE_NOT_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.TERNARY_TRUE, EvaluationOperator.TERNARY_IF_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.TERNARY_FALSE, EvaluationOperator.TERNARY_ELSE_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.COALESCE, EvaluationOperator.TERNARY_ELSE_STAGE);
        STAGESYMBOLMAP.put(OperatorSymbol.SEPARATE, EvaluationOperator.SEPARATOR_STAGE);
    }

    private PrecedencePlanner precedencePlanner;

    public EvaluationStage plan(TokenStream stream) throws Exception {
        StagePlanner nextRight;
        if (precedencePlanner.getNextRight() != null) {
            nextRight = precedencePlanner.getNextRight();
        } else {
            nextRight = this;
        }
        return planPrecedenceLevel(stream,
                precedencePlanner.getTypeErrorFormat(),
                precedencePlanner.getValidSymbols(),
                precedencePlanner.getValidKinds(),
                nextRight,
                precedencePlanner.getNext());
    }

    public static final StagePlanner PLAN_FUNCTION = new StagePlanner() {
        public EvaluationStage plan(TokenStream stream) throws Exception {
            ExpressionToken token = stream.next();

            if (!token.getKind().equals(TokenKind.FUNCTION)) {
                stream.rewind();
                return EvaluationStagePlanner.planAccessor(stream);
            }

            EvaluationStage rightStage = EvaluationStagePlanner.planAccessor(stream);

            return EvaluationStage.builder()
                    .symbol(OperatorSymbol.FUNCTIONAL)
                    .rightStage(rightStage)
                    .operator(EvaluationFunctionOperator.builder().expressionFunction((Function)token.getValue()).build())
                    .typeErrorFormat(ErrorFormat.functionErrorFormat)
                    .build();
        }
    };

    public static final StagePlanner PLAN_PREFIX = new StagePlanner() {
        private final PrecedencePlanner precedencePlanner = PrecedencePlanner.builder()
                .validSymbols(Symbol.PREFIXSYMBOLS)
                .validKinds(new TokenKind[]{TokenKind.PREFIX})
                .typeErrorFormat(ErrorFormat.prefixErrorFormat)
                .nextRight(PLAN_FUNCTION)
                .build();
        public EvaluationStage plan(TokenStream stream) throws Exception {
            return EvaluationStagePlanner.builder().precedencePlanner(precedencePlanner).build().plan(stream);
        }
    };

    public static final StagePlanner PLAN_EXPONENTIAL = new StagePlanner() {
        private final PrecedencePlanner precedencePlanner = PrecedencePlanner.builder()
                .validSymbols(Symbol.EXPONENTIALSYMBOLSS)
                .validKinds(new TokenKind[]{TokenKind.MODIFIER})
                .typeErrorFormat(ErrorFormat.modifierErrorFormat)
                .next(PLAN_FUNCTION)
                .build();
        public EvaluationStage plan(TokenStream stream) throws Exception {
            return EvaluationStagePlanner.builder().precedencePlanner(precedencePlanner).build().plan(stream);
        }
    };

    public static final StagePlanner PLAN_MULTIPLICATIVE = new StagePlanner() {
        private final PrecedencePlanner precedencePlanner = PrecedencePlanner.builder()
                .validSymbols(Symbol.MULTIPLICATIVESYMBOLS)
                .validKinds(new TokenKind[]{TokenKind.MODIFIER})
                .typeErrorFormat(ErrorFormat.modifierErrorFormat)
                .next(PLAN_EXPONENTIAL)
                .build();
        public EvaluationStage plan(TokenStream stream) throws Exception {
            return EvaluationStagePlanner.builder().precedencePlanner(precedencePlanner).build().plan(stream);
        }
    };

    public static final StagePlanner PLAN_ADDITIVE = new StagePlanner() {
        private final PrecedencePlanner precedencePlanner = PrecedencePlanner.builder()
                .validSymbols(Symbol.ADDITIVESYMBOLS)
                .validKinds(new TokenKind[]{TokenKind.MODIFIER})
                .typeErrorFormat(ErrorFormat.modifierErrorFormat)
                .next(PLAN_MULTIPLICATIVE)
                .build();
        public EvaluationStage plan(TokenStream stream) throws Exception {
            return EvaluationStagePlanner.builder().precedencePlanner(precedencePlanner).build().plan(stream);
        }
    };

    public static final StagePlanner PLAN_SHIFT = new StagePlanner() {
        private final PrecedencePlanner precedencePlanner = PrecedencePlanner.builder()
                .validSymbols(Symbol.BITWISESHIFTSYMBOLS)
                .validKinds(new TokenKind[]{TokenKind.MODIFIER})
                .typeErrorFormat(ErrorFormat.modifierErrorFormat)
                .next(PLAN_ADDITIVE)
                .build();
        public EvaluationStage plan(TokenStream stream) throws Exception {
            return EvaluationStagePlanner.builder().precedencePlanner(precedencePlanner).build().plan(stream);
        }
    };

    public static final StagePlanner PLAN_BITWISE = new StagePlanner() {
        private final PrecedencePlanner precedencePlanner = PrecedencePlanner.builder()
                .validSymbols(Symbol.BITWISESYMBOLS)
                .validKinds(new TokenKind[]{TokenKind.MODIFIER})
                .typeErrorFormat(ErrorFormat.modifierErrorFormat)
                .next(PLAN_SHIFT)
                .build();
        public EvaluationStage plan(TokenStream stream) throws Exception {
            return EvaluationStagePlanner.builder().precedencePlanner(precedencePlanner).build().plan(stream);
        }
    };

    public static final StagePlanner PLAN_COMPARATOR = new StagePlanner() {
        private final PrecedencePlanner precedencePlanner = PrecedencePlanner.builder()
                .validSymbols(Symbol.COMPARATORSYMBOLS)
                .validKinds(new TokenKind[]{TokenKind.COMPARATOR})
                .typeErrorFormat(ErrorFormat.comparatorErrorFormat)
                .next(PLAN_BITWISE)
                .build();
        public EvaluationStage plan(TokenStream stream) throws Exception {
            return EvaluationStagePlanner.builder().precedencePlanner(precedencePlanner).build().plan(stream);
        }
    };

    public static final StagePlanner PLAN_LOGICAL_AND = new StagePlanner() {
        private final PrecedencePlanner precedencePlanner = PrecedencePlanner.builder()
                .validSymbols(Symbol.ANDSYMBOLS)
                .validKinds(new TokenKind[]{TokenKind.LOGICALOP})
                .typeErrorFormat(ErrorFormat.logicalErrorFormat)
                .next(PLAN_COMPARATOR)
                .build();
        public EvaluationStage plan(TokenStream stream) throws Exception {
            return EvaluationStagePlanner.builder().precedencePlanner(precedencePlanner).build().plan(stream);
        }
    };

    public static final StagePlanner PLAN_LOGICAL_OR = new StagePlanner() {
        private final PrecedencePlanner precedencePlanner = PrecedencePlanner.builder()
                .validSymbols(Symbol.ORSYMBOLS)
                .validKinds(new TokenKind[]{TokenKind.LOGICALOP})
                .typeErrorFormat(ErrorFormat.logicalErrorFormat)
                .next(PLAN_LOGICAL_AND)
                .build();
        public EvaluationStage plan(TokenStream stream) throws Exception {
            return EvaluationStagePlanner.builder().precedencePlanner(precedencePlanner).build().plan(stream);
        }
    };

    public static final StagePlanner PLAN_TERNARY = new StagePlanner() {
        private final PrecedencePlanner precedencePlanner = PrecedencePlanner.builder()
                .validSymbols(Symbol.TERNARYSYMBOLS)
                .validKinds(new TokenKind[]{TokenKind.TERNARY})
                .typeErrorFormat(ErrorFormat.ternaryErrorFormat)
                .next(PLAN_LOGICAL_OR)
                .build();
        public EvaluationStage plan(TokenStream stream) throws Exception {
            return EvaluationStagePlanner.builder().precedencePlanner(precedencePlanner).build().plan(stream);
        }
    };

    public static final StagePlanner PLAN_SEPARATOR = new StagePlanner() {
        private final PrecedencePlanner precedencePlanner = PrecedencePlanner.builder()
                .validSymbols(Symbol.SEPARATORSYMBOLS)
                .validKinds(new TokenKind[]{TokenKind.SEPARATOR})
                .next(PLAN_TERNARY)
                .build();
        public EvaluationStage plan(TokenStream stream) throws Exception {
            return EvaluationStagePlanner.builder().precedencePlanner(precedencePlanner).build().plan(stream);
        }
    };

    public static EvaluationStage planPrecedenceLevel(TokenStream stream, ErrorFormat typeErrorFormat, Map<String, OperatorSymbol> validSymbols, TokenKind[] validKinds, StagePlanner rightPrecedent, StagePlanner leftPrecedent) throws Exception {

        ExpressionToken token;
        OperatorSymbol symbol = OperatorSymbol.VALUE;
        EvaluationStage leftStage = null, rightStage = null;
        EvaluationTypeChecks checks;
        boolean keyFound;

        if (leftPrecedent != null) {
            leftStage = leftPrecedent.plan(stream);
        }

        while (stream.hasNext()) {

            token = stream.next();

            if (validKinds.length > 0) {

                keyFound = false;
                for (TokenKind kind : validKinds) {
                    if (kind.equals(token.getKind())){
                        keyFound = true;
                        break;
                    }
                }

                if (!keyFound) {
                    break;
                }
            }

            if (validSymbols != null) {

                if (!TypeCheckStrategy.IS_STRING.check(token.getValue())) {
                    break;
                }

                symbol = validSymbols.get(token.getValue().toString());
                if (symbol == null) {
                    break;
                }
            }

            if (rightPrecedent != null) {
                rightStage = rightPrecedent.plan(stream);
            }

            checks = findEvaluationTypeChecks(symbol);

            return EvaluationStage.builder()
                    .symbol(symbol)
                    .leftStage(leftStage)
                    .rightStage(rightStage)
                    .operator(EvaluationStagePlanner.STAGESYMBOLMAP.get(symbol))
                    .leftTypeCheck(checks.getLeft())
                    .rightTypeCheck(checks.getRight())
                    .combinedTypeCheck(checks.getCombined())
                    .typeErrorFormat(typeErrorFormat)
                    .build();
        }

        stream.rewind();
        return leftStage;
    }


    public static EvaluationTypeChecks findEvaluationTypeChecks(OperatorSymbol symbol) {
        if (symbol == null) {
            return EvaluationTypeChecks.builder().build();
        }

        switch (symbol) {
//            case GT:
//            case LT:
//            case GTE:
//            case LTE: {
//                return EvaluationTypeChecks.builder().combined(CombinedTypeCheckStrategy.COMPARATOR).build();
//            }
            case REQ:
            case NREQ: {
                return EvaluationTypeChecks.builder().left(TypeCheckStrategy.IS_STRING).right(TypeCheckStrategy.IS_REGEX_OR_STRING).build();
            }
            case AND:
            case OR: {
                return EvaluationTypeChecks.builder().left(TypeCheckStrategy.IS_BOOL).right(TypeCheckStrategy.IS_BOOL).build();
            }
            case IN: {
                return EvaluationTypeChecks.builder().right(TypeCheckStrategy.IS_ARRAY).build();
            }
            case BITWISE_LSHIFT:
            case BITWISE_RSHIFT:
            case BITWISE_OR:
            case BITWISE_AND:
            case BITWISE_XOR:
            case MINUS:
            case MULTIPLY:
            case DIVIDE:
            case MODULUS:
            case EXPONENT: {
                return EvaluationTypeChecks.builder().left(TypeCheckStrategy.IS_NUMBER).right(TypeCheckStrategy.IS_NUMBER).build();
            }
            case PLUS: {
                return EvaluationTypeChecks.builder().combined(CombinedTypeCheckStrategy.ADDITION).build();
            }
            case NEGATE:
            case BITWISE_NOT: {
                return EvaluationTypeChecks.builder().right(TypeCheckStrategy.IS_NUMBER).build();
        }
            case INVERT:
                return EvaluationTypeChecks.builder().right(TypeCheckStrategy.IS_BOOL).build();
            case TERNARY_TRUE: {
                return EvaluationTypeChecks.builder().left(TypeCheckStrategy.IS_BOOL).build();
            }
            case TERNARY_FALSE:
            case COALESCE:
            default: {
                return EvaluationTypeChecks.builder().build();
            }
        }
    }

    public static EvaluationStage planStages(List<ExpressionToken> tokens) throws Exception {

        TokenStream stream = new TokenStream(tokens);

        EvaluationStage stage = planTokens(stream);

        reorderStages(stage);

        stage = elideLiterals(stage);
        return stage;
    }

    public static EvaluationStage planTokens(TokenStream stream) throws Exception {

        if (!stream.hasNext()) {
            return null;
        }

        return PLAN_SEPARATOR.plan(stream);
    }

    public static EvaluationStage planAccessor(TokenStream stream) throws Exception {

        if (!stream.hasNext()) {
            return null;
        }
        EvaluationStage rightStage = null;
        ExpressionToken token = stream.next();

        if (token.getKind() != TokenKind.ACCESSOR) {
            stream.rewind();
            return planValue(stream);
        }

        boolean isFunction = false;
        if (stream.hasNext()) {

            ExpressionToken otherToken = stream.next();
            if (TokenKind.CLAUSE.equals(otherToken.getKind())) {
                isFunction = true;
                stream.rewind();

                rightStage = planValue(stream);
            } else {
                stream.rewind();
            }
        }

        return EvaluationStage.builder()
                .symbol(OperatorSymbol.ACCESS)
                .rightStage(rightStage)
                .operator(EvaluationAccessorOperator.builder().pairs((String[]) token.getValue()).isFunction(isFunction).build())
                .typeErrorFormat(ErrorFormat.accessorErrorFormat)
                .build();
    }

    public static EvaluationStage planValue(TokenStream stream) throws Exception {

        OperatorSymbol symbol = OperatorSymbol.VALUE;
        Operator operator = null;

        if (!stream.hasNext()) {
            return null;
        }

        ExpressionToken token = stream.next();

        switch (token.getKind()) {

            case CLAUSE: {
                EvaluationStage ret = planTokens(stream);
                stream.next();
                return EvaluationStage.builder().rightStage(ret).operator(EvaluationOperator.NOOP_STAGE_RIGHT).symbol(OperatorSymbol.NOOP).build();

            }
            case CLAUSE_CLOSE: {
                stream.rewind();
                return null;
            }
            case VARIABLE: {
                operator = EvaluationParameterOperator.builder().parameterName((String) token.getValue()).build();
                break;
            }
            case NUMERIC:
            case STRING:
            case PATTERN:
            case BOOLEAN: {
                symbol = OperatorSymbol.LITERAL;
                operator = EvaluationLiteralOperator.builder().literal(token.getValue()).build();
                break;
            }
            case TIME: {
                symbol = OperatorSymbol.LITERAL;
                operator = EvaluationLiteralOperator.builder().literal((double) ((Date) token.getValue()).getTime()).build();
                break;
            }
            case PREFIX: {
                stream.rewind();
                return EvaluationStagePlanner.PLAN_PREFIX.plan(stream);
            }
        }

        if (operator == null) {
            throw new Exception(String.format("Unable to plan token kind: '%s', value: '%s'", token.getKind().toString(), token.getValue()));
        }

        return EvaluationStage.builder().symbol(symbol).operator(operator).build();
    }

    public static void reorderStages(EvaluationStage rootStage) {

        List<EvaluationStage> identicalPrecedences = new ArrayList<EvaluationStage>();
        EvaluationStage currentStage, nextStage;
        OperatorPrecedence precedence, currentPrecedence;

        nextStage = rootStage;
        precedence = OperatorSymbol.findOperatorPrecedenceForSymbol(rootStage.getSymbol());

        while (nextStage != null) {

            currentStage = nextStage;
            nextStage = currentStage.getRightStage();

            if (currentStage.getLeftStage() != null) {
                reorderStages(currentStage.getLeftStage());
            }

            currentPrecedence = OperatorSymbol.findOperatorPrecedenceForSymbol(currentStage.getSymbol());

            if (currentPrecedence == precedence) {
                identicalPrecedences.add(currentStage);
                continue;
            }

            if (identicalPrecedences.size() > 1) {
                mirrorStageSubtree(identicalPrecedences);
            }

            identicalPrecedences = new ArrayList<EvaluationStage>();
            identicalPrecedences.add(currentStage);
            precedence = currentPrecedence;
        }

        if (identicalPrecedences.size() > 1) {
            mirrorStageSubtree(identicalPrecedences);
        }
    }

    public static void mirrorStageSubtree(List<EvaluationStage> stages) {

        EvaluationStage rootStage, inverseStage, carryStage, frontStage;

        int stagesLength = stages.size();

        // reverse all right/left
        for (EvaluationStage stage : stages) {
            carryStage = stage.getRightStage();
            stage.setRightStage(stage.getLeftStage());
            stage.setLeftStage(carryStage);
        }

        rootStage = stages.get(0);
        frontStage = stages.get(stagesLength-1);

        carryStage = frontStage.getLeftStage();
        frontStage.setLeftStage(rootStage.getRightStage());
        rootStage.setRightStage(carryStage);

        for (int i = 0; i < (stagesLength-2)/2+1; i++) {

            frontStage = stages.get(i + 1);
            inverseStage = stages.get(stagesLength-i-1);

            carryStage = frontStage.getRightStage();
            frontStage.setRightStage(inverseStage.getRightStage());
            inverseStage.setRightStage(carryStage);
        }

        for (int i = 0; i < stagesLength/2; i++) {
            frontStage = stages.get(i);
            inverseStage = stages.get(stagesLength-i-1);
            frontStage.swapWith(inverseStage);
        }
    }

    public static EvaluationStage elideLiterals(EvaluationStage root) {

        if (root.getLeftStage() != null) {
            root.setLeftStage(elideLiterals(root.getLeftStage()));
        }

        if (root.getRightStage() != null) {
            root.setRightStage(elideLiterals(root.getRightStage()));
        }

        return elideStage(root);
    }

    public static EvaluationStage elideStage(EvaluationStage root) {

        Object leftValue, rightValue, leftStageValue = null, rightStageValue = null, result;

        EvaluationResult evaluationResult = null;

        // right side must be a non-nil value. Left side must be nil or a value.
        if (root.getRightStage() == null ||
                root.getRightStage().getSymbol() != OperatorSymbol.LITERAL ||
                root.getLeftStage() == null ||
                root.getLeftStage().getSymbol() != OperatorSymbol.LITERAL) {
            return root;
        }

        // don't elide some operators
        switch (root.getSymbol()) {
            case SEPARATE:
            case IN:
                return root;
        }

        try {
            evaluationResult = root.getLeftStage().getOperator().operator(null, null, null, null, null);
            leftValue = evaluationResult.getResult();

            evaluationResult = root.getRightStage().getOperator().operator(null, null, null, null, null);
            rightValue = evaluationResult.getResult();

            typeCheck(root.getLeftTypeCheck(), leftValue, root.getSymbol(), root.getTypeErrorFormat());

            typeCheck(root.getRightTypeCheck(), rightValue, root.getSymbol(), root.getTypeErrorFormat());

            if (root.getCombinedTypeCheck() != null && !root.getCombinedTypeCheck().check(leftValue, rightValue)) {
                return root;
            }

            // pre-calculate, and return a new stage representing the result.
            evaluationResult = root.getOperator().operator(leftValue, rightValue, leftStageValue, rightStageValue, null);
            result = evaluationResult.result;
        } catch (Exception e) {
            return root;
        }

        return EvaluationStage.builder().symbol(OperatorSymbol.LITERAL).operator(EvaluationLiteralOperator.builder().literal(result).build()).build();
    }

    public static void typeCheck(TypeCheck check, Object value, OperatorSymbol symbol, ErrorFormat format) throws Exception {

        if (check == null) {
            return;
        }

        if (check.check(value)) {
            return;
        }
        throw new Exception(String.format(format.getFormat(), value, symbol.toString()));
    }

}
