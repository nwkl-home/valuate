package com.lwkl.valuate.check;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.regex.Pattern;

public class TypeCheckStrategy {

    public static final TypeCheck IS_STRING = new TypeCheck() {
        public boolean check(Object value) {
            return value instanceof String;
        }
    };

    public static final TypeCheck IS_REGEX_OR_STRING = new TypeCheck() {
        public boolean check(Object value) {
            if (IS_STRING.check(value)) {
                return true;
            }
            return value instanceof Pattern;
        }
    };

    public static final TypeCheck IS_BOOL = new TypeCheck() {
        public boolean check(Object value) {
            return value instanceof Boolean;
        }
    };

    public static final TypeCheck IS_NUMBER = new TypeCheck() {
        public boolean check(Object value) {
            if (IS_STRING.check(value)) {
                return false;
            }
            if (value instanceof BigDecimal) {
                return true;
            }
            try {
                new BigDecimal(value.toString());
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    };

    public static final TypeCheck IS_ARRAY = new TypeCheck() {
        public boolean check(Object value) {
           return value.getClass().isArray() || value instanceof Collection;
        }
    };
}
