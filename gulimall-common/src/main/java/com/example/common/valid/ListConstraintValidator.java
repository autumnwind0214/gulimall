package com.example.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Huang
 * @description TODO
 * @date 2024年02月07日
 * @version: 1.0
 */
public class ListConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private Set<Integer> set = new HashSet<>();

    // 初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {

        int[] values = constraintAnnotation.values();

        for (int value : values) {
            set.add(value);
        }
    }

    // 判断是否校验成功

    /**
     * @param value   需要校验的值
     * @param context
     * @return boolean
     **/
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {

        return set.contains(value);
    }
}
