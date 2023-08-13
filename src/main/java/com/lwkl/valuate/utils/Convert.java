package com.lwkl.valuate.utils;

import com.alibaba.fastjson.JSON;
import com.lwkl.valuate.check.TypeCheckStrategy;

import java.math.BigDecimal;

public class Convert {

    public static String convertString(Object value) {
        if (value == null) {
            return "";
        }
        if (TypeCheckStrategy.IS_STRING.check(value)) {
            return value.toString();
        } else if (TypeCheckStrategy.IS_NUMBER.check(value)) {
            if (value instanceof BigDecimal) {
                BigDecimal bigDecimal = (BigDecimal) value;
                return bigDecimal.toString();
            } else {
                return value.toString();
            }
        } else if (TypeCheckStrategy.IS_BOOL.check(value)) {
            return ((Boolean) value).toString();
        }
        return JSON.toJSONString(value);
    }

    public static BigDecimal convertBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        return new BigDecimal(convertString(value));
    }

    public static boolean convertBoolean(Object value) {
        return Boolean.parseBoolean(convertString(value));
    }

    public static String convertObject(Object value) {
        return JSON.toJSONString(value);
    }
}
