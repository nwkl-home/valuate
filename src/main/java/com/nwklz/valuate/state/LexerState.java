package com.nwklz.valuate.state;

import com.nwklz.valuate.domain.ExpressionToken;
import com.nwklz.valuate.enums.TokenKind;
import lombok.Data;

import java.util.List;

@Data
public class LexerState {
    private boolean isEOF;
    private boolean isNullable;
    private TokenKind kind;
    private TokenKind[] validNextKinds;

    LexerState(TokenKind kind, boolean isEOF, boolean isNullable, TokenKind[] validNextKinds) {
        this.kind = kind;
        this.isEOF = isEOF;
        this.isNullable = isNullable;
        this.validNextKinds = validNextKinds;
    }

    public final static LexerState[] VALIDLEXERSTATES = new LexerState[]{

            new LexerState(TokenKind.UNKNOWN, false, true, new TokenKind[]{
                    TokenKind.PREFIX,
                    TokenKind.NUMERIC,
                    TokenKind.BOOLEAN,
                    TokenKind.VARIABLE,
                    TokenKind.PATTERN,
                    TokenKind.FUNCTION,
                    TokenKind.ACCESSOR,
                    TokenKind.STRING,
                    TokenKind.TIME,
                    TokenKind.CLAUSE,}),

            new LexerState(TokenKind.CLAUSE, false, true, new TokenKind[]{
                    TokenKind.PREFIX,
                    TokenKind.NUMERIC,
                    TokenKind.BOOLEAN,
                    TokenKind.VARIABLE,
                    TokenKind.PATTERN,
                    TokenKind.FUNCTION,
                    TokenKind.ACCESSOR,
                    TokenKind.STRING,
                    TokenKind.TIME,
                    TokenKind.CLAUSE,
                    TokenKind.CLAUSE_CLOSE,}),

            new LexerState(TokenKind.CLAUSE_CLOSE, true, true, new TokenKind[]{
                    TokenKind.COMPARATOR,
                    TokenKind.MODIFIER,
                    // TokenKind.NUMERIC,
                    // TokenKind.BOOLEAN,
                    TokenKind.VARIABLE,
                    // TokenKind.STRING,
                    TokenKind.PATTERN,
                    // TokenKind.TIME,
                    TokenKind.CLAUSE,
                    TokenKind.CLAUSE_CLOSE,
                    TokenKind.LOGICALOP,
                    TokenKind.TERNARY,
                    TokenKind.SEPARATOR,}),

            new LexerState(TokenKind.NUMERIC, true, false, new TokenKind[]{
                    TokenKind.MODIFIER,
                    TokenKind.COMPARATOR,
                    TokenKind.LOGICALOP,
                    TokenKind.CLAUSE_CLOSE,
                    TokenKind.TERNARY,
                    TokenKind.SEPARATOR,}),

            new LexerState(TokenKind.BOOLEAN, true, false, new TokenKind[]{
                    TokenKind.MODIFIER,
                    TokenKind.COMPARATOR,
                    TokenKind.LOGICALOP,
                    TokenKind.CLAUSE_CLOSE,
                    TokenKind.TERNARY,
                    TokenKind.SEPARATOR,}),

            new LexerState(TokenKind.STRING, true, false, new TokenKind[]{
                    TokenKind.MODIFIER,
                    TokenKind.COMPARATOR,
                    TokenKind.LOGICALOP,
                    TokenKind.CLAUSE_CLOSE,
                    TokenKind.TERNARY,
                    TokenKind.SEPARATOR,}),

            new LexerState(TokenKind.TIME, true, false, new TokenKind[]{
                    TokenKind.MODIFIER,
                    TokenKind.COMPARATOR,
                    TokenKind.LOGICALOP,
                    TokenKind.CLAUSE_CLOSE,
                    TokenKind.SEPARATOR,}),

            new LexerState(TokenKind.PATTERN, true, false, new TokenKind[]{
                    TokenKind.MODIFIER,
                    TokenKind.COMPARATOR,
                    TokenKind.LOGICALOP,
                    TokenKind.CLAUSE_CLOSE,
                    TokenKind.SEPARATOR,}),

            new LexerState(TokenKind.VARIABLE, true, false, new TokenKind[]{
                    TokenKind.MODIFIER,
                    TokenKind.COMPARATOR,
                    TokenKind.LOGICALOP,
                    TokenKind.CLAUSE_CLOSE,
                    TokenKind.TERNARY,
                    TokenKind.SEPARATOR,}),

            new LexerState(TokenKind.MODIFIER, false, false, new TokenKind[]{
                    TokenKind.PREFIX,
                    TokenKind.NUMERIC,
                    TokenKind.VARIABLE,
                    TokenKind.FUNCTION,
                    TokenKind.ACCESSOR,
                    TokenKind.STRING,
                    TokenKind.BOOLEAN,
                    TokenKind.CLAUSE,
                    TokenKind.CLAUSE_CLOSE,}),

            new LexerState(TokenKind.COMPARATOR, false, false, new TokenKind[]{
                    TokenKind.PREFIX,
                    TokenKind.NUMERIC,
                    TokenKind.BOOLEAN,
                    TokenKind.VARIABLE,
                    TokenKind.FUNCTION,
                    TokenKind.ACCESSOR,
                    TokenKind.STRING,
                    TokenKind.TIME,
                    TokenKind.CLAUSE,
                    TokenKind.CLAUSE_CLOSE,
                    TokenKind.PATTERN,}),

            new LexerState(TokenKind.LOGICALOP, false, false, new TokenKind[]{
                    TokenKind.PREFIX,
                    TokenKind.NUMERIC,
                    TokenKind.BOOLEAN,
                    TokenKind.VARIABLE,
                    TokenKind.FUNCTION,
                    TokenKind.ACCESSOR,
                    TokenKind.STRING,
                    TokenKind.TIME,
                    TokenKind.CLAUSE,
                    TokenKind.CLAUSE_CLOSE,}),

            new LexerState(TokenKind.PREFIX, false, false, new TokenKind[]{
                    TokenKind.NUMERIC,
                    TokenKind.BOOLEAN,
                    TokenKind.VARIABLE,
                    TokenKind.FUNCTION,
                    TokenKind.ACCESSOR,
                    TokenKind.CLAUSE,
                    TokenKind.CLAUSE_CLOSE,}),

            new LexerState(TokenKind.TERNARY, false, false, new TokenKind[]{
                    TokenKind.PREFIX,
                    TokenKind.NUMERIC,
                    TokenKind.BOOLEAN,
                    TokenKind.STRING,
                    TokenKind.TIME,
                    TokenKind.VARIABLE,
                    TokenKind.FUNCTION,
                    TokenKind.ACCESSOR,
                    TokenKind.CLAUSE,
                    TokenKind.SEPARATOR,}),

            new LexerState(TokenKind.FUNCTION, false, false, new TokenKind[]{
                    TokenKind.CLAUSE,}),

            new LexerState(TokenKind.ACCESSOR, true, false, new TokenKind[]{
                    TokenKind.CLAUSE,
                    TokenKind.MODIFIER,
                    TokenKind.COMPARATOR,
                    TokenKind.LOGICALOP,
                    TokenKind.CLAUSE_CLOSE,
                    TokenKind.TERNARY,
                    TokenKind.SEPARATOR,}),

            new LexerState(TokenKind.SEPARATOR, false, true, new TokenKind[]{
                    TokenKind.PREFIX,
                    TokenKind.NUMERIC,
                    TokenKind.BOOLEAN,
                    TokenKind.STRING,
                    TokenKind.TIME,
                    TokenKind.VARIABLE,
                    TokenKind.FUNCTION,
                    TokenKind.ACCESSOR,
                    TokenKind.CLAUSE,}),
    };

    public boolean canTransitionTo(TokenKind kind) {
        for (TokenKind validKind : this.validNextKinds) {
            if (validKind == kind) {
                return true;
            }
        }
        return false;
    }

    public static LexerState getLexerStateForToken(TokenKind kind) throws Exception {
        for (LexerState possibleState : VALIDLEXERSTATES) {
            if (possibleState.getKind() == kind) {
                return possibleState;
            }
        }
        throw new Exception(String.format("No lexer state found for token kind '%s'", kind.toString()));
    }

    public static void checkExpressionSyntax(List<ExpressionToken> tokens) throws Exception {
        LexerState state = VALIDLEXERSTATES[0];
        ExpressionToken lastToken = new ExpressionToken();
        for (ExpressionToken token : tokens) {
            if (!state.canTransitionTo(token.getKind())) {
                if (TokenKind.VARIABLE.equals(lastToken.getKind()) && TokenKind.CLAUSE.equals(token.getKind())) {
                    throw new Exception("Undefined function " + lastToken.getValue());
                }

                String firstStateName = String.format("%s [%s]", state.getKind().toString(), lastToken.getValue());
                String nextStateName = String.format("%s [%s]", token.getKind().toString(), token.getValue());
                throw new Exception(String.format("Cannot transition token types from %s to %s", firstStateName, nextStateName));
            }

            state = getLexerStateForToken(token.getKind());
            if (!state.isNullable && token.getValue() == null) {
                throw new Exception(String.format("Token kind '%s' cannot have a nil value", token.getKind().toString()));
            }
            lastToken = token;
        }
        if (!state.isEOF){
            throw new Exception("Unexpected end of expression");
        }
    }
}
