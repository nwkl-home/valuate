package com.lwkl.valuate.enums;

import lombok.Getter;

@Getter
public enum ErrorFormat {

    logicalErrorFormat("Value '%s' cannot be used with the logical operator '%s', it is not a bool"),
    modifierErrorFormat("Value '%s' cannot be used with the modifier '%s', it is not a number"),
    comparatorErrorFormat("Value '%s' cannot be used with the comparator '%s', it is not a number"),
    ternaryErrorFormat("Value '%s' cannot be used with the ternary operator '%s', it is not a bool"),
    prefixErrorFormat("Value '%s' cannot be used with the prefix '%s'"),
    functionErrorFormat("Unable to run function '%s': %s"),
    accessorErrorFormat("Unable to access parameter field or method '%s': %s");

    private final String format;

    ErrorFormat(String format) {
        this.format = format;
    }

}
