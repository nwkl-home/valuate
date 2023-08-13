package com.lwkl.valuate.operator;

import com.lwkl.valuate.check.TypeCheckStrategy;
import com.lwkl.valuate.domain.MapParameters;
import com.lwkl.valuate.result.EvaluationResult;
import lombok.Builder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@Builder
public class EvaluationAccessorOperator implements Operator {

    private String[] pairs;

    private boolean isFunction;

    public EvaluationResult operator(Object left, Object right, Object leftStage, Object rightStage, MapParameters parameters) throws Exception {
        Object value = parameters.get(pairs[0]);
        for (int i=1; i<pairs.length; i++) {
            if (value == null) {
                throw new Exception("No field '" + pairs[i] + "' present on parameter '" + pairs[i-1] + "'");
            }
            if (value instanceof Map) {
                if (!isFunction) {
                    Map<?, ?> map = (Map<?, ?>) value;
                    value = map.get(pairs[i]);
                    if (value == null) {
                        throw new Exception("No field '" + pairs[i] + "' present on parameter '" + pairs[i-1] + "'");
                    }
                } else {
                    throw new Exception("No method '" + pairs[i] + "' present on parameter '" + pairs[i-1] + "'");
                }

            } else {
                if (!isFunction) {
                    Field[] fields = value.getClass().getDeclaredFields();
                    boolean isField = false;
                    for (Field field:fields) {
                        if (field.getName().equals(pairs[i])) {
                            field.setAccessible(true);
                            value = field.get(value);
                            isField = true;
                            break;
                        }
                    }
                    if (isField) {
                        continue;
                    }
                    throw new Exception("No field '" + pairs[i] + "' present on parameter '" + pairs[i-1] + "'");
                } else {
                    Method[] methods = value.getClass().getMethods();
                    boolean isMethod = false;
                    for (Method method:methods) {
                        if (method.getName().equals(pairs[i])) {
                            method.setAccessible(true);
                            try {
                                if (right == null) {
                                    value = method.invoke(value);
                                } else if (TypeCheckStrategy.IS_ARRAY.check(right)){
                                    value = method.invoke(value, (Object[]) right);
                                } else {
                                    value = method.invoke(value, right);
                                }
                            } catch (Exception e) {
                                throw new Exception("method '" + pairs[i] + "' present on parameter '" + pairs[i-1] + "' invoke error : " + e.getMessage());
                            }

                            isMethod = true;
                            break;
                        }
                    }
                    if (isMethod) {
                        continue;
                    }
                    throw new Exception("No method '" + pairs[i] + "' present on parameter '" + pairs[i-1] + "'");
                }
            }
        }

        return EvaluationResult.builder().result(value).leftStage(leftStage).rightStage(rightStage).build();
    }

}
