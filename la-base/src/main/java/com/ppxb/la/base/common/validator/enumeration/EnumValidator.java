package com.ppxb.la.base.common.validator.enumeration;

import com.ppxb.la.base.common.enumeration.BaseEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class EnumValidator implements ConstraintValidator<CheckEnum, Object> {

    private List<Object> enumValList;

    private boolean required;

    @Override
    public void initialize(CheckEnum constraintAnnotation) {
        required = constraintAnnotation.required();
        Class<? extends BaseEnum> enumClass = constraintAnnotation.value();
        enumValList = Stream.of(enumClass.getEnumConstants()).map(BaseEnum::getValue).toList();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return !required;
        }

        if (value instanceof List) {
            return this.checkList((List<Object>) value);
        }

        return enumValList.contains(value);
    }

    private boolean checkList(List<Object> list) {
        if (required && list.isEmpty()) {
            return false;
        }
        long count = list.stream().distinct().count();
        if (count != list.size()) {
            return false;
        }
        return new HashSet<>(enumValList).containsAll(list);
    }
}
