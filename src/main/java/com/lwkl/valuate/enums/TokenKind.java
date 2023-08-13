package com.lwkl.valuate.enums;

import lombok.Getter;

@Getter
public enum TokenKind {

    UNKNOWN,
    PREFIX,
    NUMERIC,
    BOOLEAN,
    STRING,
    PATTERN,
    TIME,
    VARIABLE,
    FUNCTION,
    SEPARATOR,
    ACCESSOR,
    COMPARATOR,
    LOGICALOP,
    MODIFIER,
    CLAUSE,
    CLAUSE_CLOSE,
    TERNARY;
}
