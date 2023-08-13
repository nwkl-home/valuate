package com.lwkl.valuate.check;

public class ConditionStrategy {

    public static final Condition IS_HEX_DIGIT = new Condition() {
        public boolean check(char character) {
            character = Character.toLowerCase(character);
            return Character.isDigit(character) ||
                    character == 'a' ||
                    character == 'b' ||
                    character == 'c' ||
                    character == 'd' ||
                    character == 'e' ||
                    character == 'f';
        }
    };

    public static final Condition IS_NOT_CLOSING_BRACKET = new Condition() {
        public boolean check(char character) {
            return character != ']';
        }
    };

    public static final Condition IS_NOT_QUOTE = new Condition() {
        public boolean check(char character) {
            return character != '\'' && character != '"';
        }
    };

    public static final Condition IS_NUMERIC = new Condition() {
        public boolean check(char character) {
            return Character.isDigit(character) || character == '.';
        }
    };

    public static final Condition IS_VARIABLE_NAME = new Condition() {
        public boolean check(char character) {
            return Character.isLetter(character) ||
                    Character.isDigit(character) ||
                    character == '_' ||
                    character == '.';
        }
    };

    public static final Condition ISNOTALPHANUMERIC = new Condition() {
        public boolean check(char character) {
            return !(Character.isDigit(character) ||
                    Character.isLetter(character) ||
                    character == '(' ||
                    character == ')' ||
                    character == '[' ||
                    character == ']' ||
                    !IS_NOT_QUOTE.check(character));
        }
    };

}

