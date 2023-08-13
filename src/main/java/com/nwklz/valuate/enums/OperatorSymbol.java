package com.nwklz.valuate.enums;

public enum OperatorSymbol {


    VALUE,
    LITERAL,
    NOOP,
    EQ,
    NEQ,
    GT,
    LT,
    GTE,
    LTE,
    REQ,
    NREQ,
    IN,
    AND,
    OR,
    PLUS,
    MINUS,
    BITWISE_AND,
    BITWISE_OR,
    BITWISE_XOR,
    BITWISE_LSHIFT,
    BITWISE_RSHIFT,
    MULTIPLY,
    DIVIDE,
    MODULUS,
    EXPONENT,
    NEGATE,
    INVERT,
    BITWISE_NOT,
    TERNARY_TRUE,
    TERNARY_FALSE,
    COALESCE,
    FUNCTIONAL,
    ACCESS,
    SEPARATE;

    public static OperatorPrecedence findOperatorPrecedenceForSymbol(OperatorSymbol symbol) {

        switch (symbol) {
            case NOOP:
                return OperatorPrecedence.noopPrecedence;
            case VALUE:
                return OperatorPrecedence.valuePrecedence;
            case EQ:
            case NEQ:
            case GT:
            case LT:
            case GTE:
            case LTE:
            case REQ:
            case NREQ:
            case IN:
                return OperatorPrecedence.comparatorPrecedence;
            case AND:
                return OperatorPrecedence.logicalAndPrecedence;
            case OR:
                return OperatorPrecedence.logicalOrPrecedence;
            case BITWISE_AND:
            case BITWISE_OR:
            case BITWISE_XOR:
                return OperatorPrecedence.bitwisePrecedence;
            case BITWISE_LSHIFT:
            case BITWISE_RSHIFT:
                return OperatorPrecedence.bitwiseShiftPrecedence;
            case PLUS:
            case MINUS:
                return OperatorPrecedence.additivePrecedence;
            case MULTIPLY:
            case DIVIDE:
            case MODULUS:
                return OperatorPrecedence.multiplicativePrecedence;
            case EXPONENT:
                return OperatorPrecedence.exponentialPrecedence;
            case BITWISE_NOT:
            case NEGATE:
            case INVERT:
                return OperatorPrecedence.prefixPrecedence;
            case COALESCE:
            case TERNARY_TRUE:
            case TERNARY_FALSE:
                return OperatorPrecedence.ternaryPrecedence;
            case ACCESS:
            case FUNCTIONAL:
                return OperatorPrecedence.functionalPrecedence;
            case SEPARATE:
                return OperatorPrecedence.separatePrecedence;
        }

        return OperatorPrecedence.valuePrecedence;
    }

    @Override
    public String toString() {
        switch (this) {
            case NOOP:
                return "NOOP";
            case VALUE:
                return "VALUE";
            case EQ:
                return "=";
            case NEQ:
                return "!=";
            case GT:
                return ">";
            case LT:
                return "<";
            case GTE:
                return ">=";
            case LTE:
                return "<=";
            case REQ:
                return "=~";
            case NREQ:
                return "!~";
            case AND:
                return "&&";
            case OR:
                return "||";
            case IN:
                return "in";
            case BITWISE_AND:
                return "&";
            case BITWISE_OR:
                return "|";
            case BITWISE_XOR:
                return "^";
            case BITWISE_LSHIFT:
                return "<<";
            case BITWISE_RSHIFT:
                return ">>";
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case MULTIPLY:
                return "*";
            case DIVIDE:
                return "/";
            case MODULUS:
                return "%";
            case EXPONENT:
                return "**";
            case NEGATE:
                return "-";
            case INVERT:
                return "!";
            case BITWISE_NOT:
                return "~";
            case TERNARY_TRUE:
                return "?";
            case TERNARY_FALSE:
                return ":";
            case COALESCE:
                return "??";
        }
        return "";
    }
}
