package com.nwklz.valuate.check;

public class CombinedTypeCheckStrategy {

    public static final CombinedTypeCheck ADDITION = new CombinedTypeCheck() {
        public boolean check(Object left, Object right) {
            if (TypeCheckStrategy.IS_NUMBER.check(left) && TypeCheckStrategy.IS_NUMBER.check((right))) {
                return true;
            }
            return TypeCheckStrategy.IS_STRING.check(left) || TypeCheckStrategy.IS_STRING.check(right);
        }
    };

    public static final CombinedTypeCheck COMPARATOR = new CombinedTypeCheck() {
        public boolean check(Object left, Object right) {
            if (TypeCheckStrategy.IS_NUMBER.check(left) && TypeCheckStrategy.IS_NUMBER.check((right))) {
                return true;
            }
            return TypeCheckStrategy.IS_STRING.check(left) && TypeCheckStrategy.IS_STRING.check(right);
        }
    };

    public static final CombinedTypeCheck SAME = new CombinedTypeCheck() {
        public boolean check(Object left, Object right) {
            boolean leftBool = TypeCheckStrategy.IS_STRING.check(left);
            boolean rightBool = TypeCheckStrategy.IS_STRING.check(right);
            if ((!leftBool && rightBool) || (leftBool && !rightBool)) {
                return false;
            }
            leftBool = TypeCheckStrategy.IS_NUMBER.check(left);
            rightBool = TypeCheckStrategy.IS_NUMBER.check(right);
            if ((!leftBool && rightBool) || (leftBool && !rightBool)) {
                return false;
            }
            leftBool = TypeCheckStrategy.IS_BOOL.check(left);
            rightBool = TypeCheckStrategy.IS_BOOL.check(right);
            return (leftBool || !rightBool) && (!leftBool || rightBool);
        }
    };
}
