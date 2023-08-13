package com.nwklz.valuate.operator;

import com.alibaba.fastjson.JSON;
import com.nwklz.valuate.check.TypeCheckStrategy;
import com.nwklz.valuate.domain.MapParameters;
import com.nwklz.valuate.result.EvaluationResult;
import com.nwklz.valuate.utils.Convert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EvaluationOperator {

    public static final Operator NOOP_STAGE_RIGHT = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(right).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator ADD_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            if (TypeCheckStrategy.IS_STRING.check(left) || TypeCheckStrategy.IS_STRING.check((right))) {
                return EvaluationResult.builder().result(Convert.convertString(left) + Convert.convertString(right)).leftStage(leftStage).rightStage(rightStage).build();
            }
            return EvaluationResult.builder().result(Convert.convertBigDecimal(left).add(Convert.convertBigDecimal(right))).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator SUBTRACT_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(Convert.convertBigDecimal(left).subtract(Convert.convertBigDecimal(right))).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator MULTIPLY_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(Convert.convertBigDecimal(left).multiply(Convert.convertBigDecimal(right))).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator DIVIDE_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(Convert.convertBigDecimal(left).divide(Convert.convertBigDecimal(right), 10, RoundingMode.HALF_UP)).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator EXPONENT_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            BigDecimal leftBigDecimal = Convert.convertBigDecimal(left);
            BigDecimal rightBigDecimal = Convert.convertBigDecimal(right);
            return EvaluationResult.builder().result(leftBigDecimal.pow(rightBigDecimal.intValue())).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator MODULUS_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            BigDecimal leftBigDecimal = Convert.convertBigDecimal(left);
            BigDecimal rightBigDecimal = Convert.convertBigDecimal(right);
            return EvaluationResult.builder().result(leftBigDecimal.remainder(rightBigDecimal)).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator GTE_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(compareTo(left, right) >= 0).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator GT_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(compareTo(left, right) > 0).leftStage(leftStage).rightStage(rightStage).build();
        }
    };


    public static final Operator LTE_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(compareTo(left, right) <= 0).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator LT_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception {
            return EvaluationResult.builder().result(compareTo(left, right) < 0).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator EQUAL_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception {
            return EvaluationResult.builder().result(isEquals(left, right)).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator NOT_EQUAL_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(!isEquals(left, right)).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator AND_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result((Boolean) left && (Boolean) right).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator OR_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result((Boolean) left || (Boolean) right).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator NEGATE_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(Convert.convertBigDecimal(right).negate()).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator INVERT_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(!(Boolean)right).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator BITWISE_NOT_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(new BigDecimal(~Convert.convertBigDecimal(right).intValue())).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator TERNARY_IF_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            if ((Boolean)left) {
                return EvaluationResult.builder().result(right).leftStage(leftStage).rightStage(rightStage).build();
            }
            return EvaluationResult.builder().leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator TERNARY_ELSE_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            if (left != null) {
                return EvaluationResult.builder().result(left).leftStage(leftStage).rightStage(rightStage).build();
            }
            return EvaluationResult.builder().result(right).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator REGEX_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{

            Pattern pattern = null;
          if (right instanceof Pattern) {
              pattern = (Pattern) right;
            } else {
              pattern = Pattern.compile(Convert.convertString(right));
          }
            Matcher matcher = pattern.matcher(Convert.convertString(left));
            return EvaluationResult.builder().result(matcher.find()).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator NOT_REGEX_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            EvaluationResult evaluationResult = REGEX_STAGE.operator(left, right, leftStage, rightStage, parameters);
            return EvaluationResult.builder().result(!(Boolean) evaluationResult.getResult()).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator BITWISE_OR_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(new BigDecimal(Convert.convertBigDecimal(left).intValue() | Convert.convertBigDecimal(right).intValue())).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator BITWISE_AND_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(new BigDecimal(Convert.convertBigDecimal(left).intValue() & Convert.convertBigDecimal(right).intValue())).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator BITWISE_XOR_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(new BigDecimal(Convert.convertBigDecimal(left).intValue() ^ Convert.convertBigDecimal(right).intValue())).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator LEFT_SHIFT_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(new BigDecimal(Convert.convertBigDecimal(left).intValue() << Convert.convertBigDecimal(right).intValue())).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator RIGHT_SHIFT_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            return EvaluationResult.builder().result(new BigDecimal(Convert.convertBigDecimal(left).intValue() >> Convert.convertBigDecimal(right).intValue())).leftStage(leftStage).rightStage(rightStage).build();
        }
    };

    public static final Operator SEPARATOR_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            List<Object> objects = new ArrayList<Object>();
            boolean isOk = false;
            if (leftStage != null) {
                if (leftStage.getClass().isArray()) {
                    isOk = true;
                    objects = Arrays.asList((Object[]) leftStage);
                } else if (leftStage instanceof Collection){
                    isOk = true;
                    objects.addAll((Collection<?>) leftStage);
                }
            }

            if (!isOk) {
                objects.add(left);
            }
            objects.add(right);

            Object[] ret = objects.toArray();
            return EvaluationResult.builder().result(ret).leftStage(ret).build();
        }
    };

    public static final Operator IN_STAGE = new Operator() {
        public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception{
            List<Object> objects = new ArrayList<Object>();
            if (right != null) {
                if (right.getClass().isArray()) {
                    objects = Arrays.asList((Object[]) right);
                } else if (right instanceof Collection){
                    objects.addAll((Collection<?>) right);
                }
            }
            for (Object object : objects) {
                if (isEquals(left, object)) {
                    return EvaluationResult.builder().result(true).leftStage(leftStage).rightStage(rightStage).build();
                }
            }
            return EvaluationResult.builder().result(false).leftStage(leftStage).rightStage(rightStage).build();
        }
    };


    private static boolean isEquals(Object left, Object right) {
        boolean leftString = TypeCheckStrategy.IS_STRING.check(left);
        boolean rightString = TypeCheckStrategy.IS_STRING.check(right);
        if (leftString && rightString) {
            return ((String)left).compareTo((String) right) == 0;
        }
        boolean leftNumber = TypeCheckStrategy.IS_NUMBER.check(left);
        boolean rightNumber = TypeCheckStrategy.IS_NUMBER.check(right);
        if (leftNumber && rightNumber) {
            return Convert.convertBigDecimal(left).compareTo(Convert.convertBigDecimal(right)) == 0;
        }
        boolean leftBool = TypeCheckStrategy.IS_BOOL.check(left);
        boolean rightBool = TypeCheckStrategy.IS_BOOL.check(right);
        if (leftBool && rightBool) {
            return (Boolean) left && (Boolean) right;
        }
        if (leftString != rightString || leftNumber != rightNumber || leftBool != rightBool) {
            return false;
        }
        return JSON.toJSONString(left).compareTo(JSON.toJSONString(right)) == 0;
    }

    private static int compareTo(Object left, Object right) throws Exception {
        boolean leftString = TypeCheckStrategy.IS_STRING.check(left);
        boolean rightString = TypeCheckStrategy.IS_STRING.check(right);
        if (leftString && rightString) {
            return ((String)left).compareTo((String) right);
        }
        boolean leftNumber = TypeCheckStrategy.IS_NUMBER.check(left);
        boolean rightNumber = TypeCheckStrategy.IS_NUMBER.check(right);
        if (leftNumber && rightNumber) {
            return Convert.convertBigDecimal(left).compareTo(Convert.convertBigDecimal(right));
        }
        boolean leftBool = TypeCheckStrategy.IS_BOOL.check(left);
        boolean rightBool = TypeCheckStrategy.IS_BOOL.check(right);
        if (leftBool && rightBool) {
            return ((Boolean) left).compareTo((Boolean) right);
        }
        if (leftString != rightString || leftNumber != rightNumber || leftBool != rightBool) {
            throw new Exception(String.format("Operator cannot be applied %s, %s", left, right));
        }
        return JSON.toJSONString(left).compareTo(JSON.toJSONString(right));
    }
}
