package com.lwkl.valuate.parse;

import com.lwkl.valuate.check.Condition;
import com.lwkl.valuate.domain.ExpressionToken;
import com.lwkl.valuate.enums.OperatorSymbol;
import com.lwkl.valuate.enums.TokenKind;
import com.lwkl.valuate.function.Function;
import com.lwkl.valuate.eval.Symbol;
import com.lwkl.valuate.state.LexerState;
import com.lwkl.valuate.stream.LexerStream;
import com.lwkl.valuate.result.ReadUntilFalseResult;
import com.lwkl.valuate.stream.TokenStream;
import com.lwkl.valuate.check.ConditionStrategy;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Parse {

    public static List<ExpressionToken> parseTokens(String expression, Map<String, Function> functions) throws Exception {
        List<ExpressionToken> tokens = new ArrayList<ExpressionToken>();
        LexerStream stream = new LexerStream(expression);
        LexerState state = LexerState.VALIDLEXERSTATES[0];

        while (stream.canRead()) {
            ExpressionToken token = readToken(stream, state, functions);
            if (TokenKind.UNKNOWN.equals(token.getKind())) {
                break;
            }

            state = LexerState.getLexerStateForToken(token.getKind());

            tokens.add(token);
        }
         checkBalance(tokens);

        return tokens;
    }

    public static ExpressionToken readToken(LexerStream stream, LexerState state, Map<String, Function> functions) throws Exception {

        String tokenString;
        TokenKind kind = TokenKind.UNKNOWN;
        Object tokenValue = null;
        char character;
        while (stream.canRead()) {
            character = stream.readCharacter();

            if (Character.isSpaceChar(character)) {
                continue;
            }
            if (Character.isDigit(character)) {
                if (stream.canRead() && character == '0') {
                    character = stream.readCharacter();
                    if (stream.canRead() && character == 'x') {
                        ReadUntilFalseResult readUntilFalseResult = readUntilFalse(stream, false, true, true, ConditionStrategy.IS_HEX_DIGIT);
                        tokenString = readUntilFalseResult.getResult();

                        try {
                            int tokenValueInt = Integer.parseInt(tokenString);
                            kind = TokenKind.NUMERIC;
                            tokenValue = new BigDecimal(tokenValueInt);
                            break;
                        } catch (NumberFormatException e) {
                            throw new NumberFormatException(String.format("Unable to parse hex value '%s' to int", tokenString));
                        }
                    } else {
                        stream.rewind(1);
                    }
                }
                tokenString = readTokenUntilFalse(stream, ConditionStrategy.IS_NUMERIC);
                try {
                    tokenValue = new BigDecimal(tokenString);
                    kind = TokenKind.NUMERIC;
                    break;
                } catch (NumberFormatException e) {
                    throw new NumberFormatException(String.format("Unable to parse numeric value '%s' to bigDecimal", tokenString));
                }

            }

            // comma, separator
            if (character == ',') {

                tokenValue = ",";
                kind = TokenKind.SEPARATOR;
                break;
            }

            // escaped variable
            if (character == '[') {

                ReadUntilFalseResult readUntilFalseResult = readUntilFalse(stream, true, false, true, ConditionStrategy.IS_NOT_CLOSING_BRACKET);
                kind = TokenKind.VARIABLE;

                if (!readUntilFalseResult.isConditioned()) {
                    throw new Exception("Unclosed parameter bracket");
                }

                stream.rewind(-1);
                break;
            }

            if (Character.isLetter(character)) {

                tokenString = readTokenUntilFalse(stream, ConditionStrategy.IS_VARIABLE_NAME);
                tokenValue = tokenString;
                kind = TokenKind.VARIABLE;

                // boolean?
                if ("true".equals(tokenString)) {
                    kind = TokenKind.BOOLEAN;
                    tokenValue = true;
                } else if ("false".equals(tokenString)) {
                    kind = TokenKind.BOOLEAN;
                    tokenValue = false;
                }

                // textual operator?
                if ("IN".equals(tokenString.toLowerCase()) || "in".equalsIgnoreCase(tokenString)) {
                    tokenValue = "in";
                    kind = TokenKind.COMPARATOR;
                }

                // function?
                Function function = functions.get(tokenString);
                if (function != null) {
                    kind = TokenKind.FUNCTION;
                    tokenValue = function;
                }

                // accessor?
                int accessorIndex = StringUtils.indexOf(tokenString, ".");
                if (accessorIndex > 0) {
                    // check that it doesn't end with a hanging period
                    if (tokenString.charAt(tokenString.length()-1) == '.') {
                        throw new Exception(String.format("Hanging accessor on token '%s'", tokenString));
                    }

                    kind = TokenKind.ACCESSOR;
                    tokenValue = tokenString.split("\\.");
                }
                break;
            }
            if (!ConditionStrategy.IS_NOT_QUOTE.check(character)) {
                ReadUntilFalseResult readUntilFalseResult = readUntilFalse(stream, true, false, true, ConditionStrategy.IS_NOT_QUOTE);
                if (!readUntilFalseResult.isConditioned()) {
                    throw new Exception("Unclosed string literal");
                }
                tokenValue = readUntilFalseResult.getResult();
                stream.rewind(-1);

                // check to see if this can be parsed as a time.
                try {
                    Date tokenTime = tryParseTime(tokenValue.toString());
                    kind = TokenKind.TIME;
                    tokenValue = tokenTime;
                } catch (Exception e) {
                    kind = TokenKind.STRING;
                }
                break;
            }
            if (character == '(') {
                tokenValue = character;
                kind = TokenKind.CLAUSE;
                break;
            }

            if (character == ')') {
                tokenValue = character;
                kind = TokenKind.CLAUSE_CLOSE;
                break;
            }

            // must be a known symbol
            tokenString = readTokenUntilFalse(stream, ConditionStrategy.ISNOTALPHANUMERIC);
            tokenValue = tokenString;

            // quick hack for the case where "-" can mean "prefixed negation" or "minus", which are used
            // very differently.
            if (state.canTransitionTo(TokenKind.PREFIX)) {
                OperatorSymbol operatorSymbol = Symbol.PREFIXSYMBOLS.get(tokenString);
                if (operatorSymbol != null) {
                    kind = TokenKind.PREFIX;
                    break;
                }
            }
            OperatorSymbol operatorSymbol = Symbol.MODIFIERSYMBOLS.get(tokenString);
            if (operatorSymbol != null) {
                kind = TokenKind.MODIFIER;
                break;
            }

            operatorSymbol = Symbol.LOGICALSYMBOLS.get(tokenString);
            if (operatorSymbol != null) {
                kind = TokenKind.LOGICALOP;
                break;
            }

            operatorSymbol = Symbol.COMPARATORSYMBOLS.get(tokenString);
            if (operatorSymbol != null) {
                kind = TokenKind.COMPARATOR;
                break;
            }

            operatorSymbol = Symbol.TERNARYSYMBOLS.get(tokenString);
            if (operatorSymbol != null) {
                kind = TokenKind.TERNARY;
                break;
            }

            throw new Exception(String.format("Invalid token: '%s'", tokenString));
        }

        return new ExpressionToken(kind, tokenValue);
    }

    public static void optimizeTokens(List<ExpressionToken> tokens) throws ParseException {
        OperatorSymbol symbol;
        int index = 0;

        for (ExpressionToken token : tokens) {
            index ++;

            // if we find a regex operator, and the right-hand value is a constant, precompile and replace with a pattern.
            if (TokenKind.COMPARATOR.equals(token.getKind())) {
                continue;
            }

            symbol = Symbol.COMPARATORSYMBOLS.get(token.getValue().toString());
            if (!OperatorSymbol.REQ.equals(symbol)  && !OperatorSymbol.NREQ.equals(symbol)) {
                continue;
            }

            index++;
            token = tokens.get(index);
            if (TokenKind.STRING.equals(token.getKind())) {
                token.setKind(TokenKind.PATTERN);

                token.setValue(Pattern.compile(token.getValue().toString()));
                tokens.set(index, token);
            }
        }
    }

    public static Date tryParseTime(String candidate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        return simpleDateFormat.parse(candidate);
    }

    public static void checkBalance(List<ExpressionToken> tokens) throws Exception {

        ExpressionToken token;
        int parens = 0;
        TokenStream stream = new TokenStream(tokens);
        while (stream.hasNext()) {

            token = stream.next();
            if (TokenKind.CLAUSE.equals(token.getKind())){
                parens++;
                continue;
            }
            if (TokenKind.CLAUSE_CLOSE.equals(token.getKind())) {
                parens--;
            }
        }
        if (parens != 0) {
            throw new Exception("Unbalanced parenthesis");
        }
    }



    public static ReadUntilFalseResult readUntilFalse(LexerStream stream, boolean includeWhitespace, boolean breakWhitespace, boolean allowEscaping, Condition condition) {
        StringBuilder tokenBuffer = new StringBuilder();
        char character;
        boolean conditioned = false;

        while (stream.canRead()) {

            character = stream.readCharacter();

            if (allowEscaping && character == '\\') {
                character = stream.readCharacter();
                tokenBuffer.append(character);
                continue;
            }

            if (Character.isSpaceChar(character)) {

                if (breakWhitespace && tokenBuffer.length() > 0) {
                    conditioned = true;
                    break;
                }
                if (!includeWhitespace) {
                    continue;
                }
            }
            if (condition.check(character)) {
                tokenBuffer.append(character);
            } else {
                conditioned = true;
                stream.rewind(1);
                break;
            }
        }

        return new ReadUntilFalseResult(tokenBuffer.toString(), conditioned);
    }

    public static String readTokenUntilFalse(LexerStream stream, Condition condition) {
        stream.rewind(1);
        ReadUntilFalseResult readTokenUntilFalse = readUntilFalse(stream, false, true, true, condition);
        return readTokenUntilFalse.getResult();
    }

}
