package com.ppxb.la.base.common.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;

public class BeanUtil {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    public static <T> T copy(Object source, Class<T> target) {
        if (source == null || target == null) {
            return null;
        }

        try {
            var newInstance = target.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, newInstance);
            return newInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, K> List<K> copyList(List<T> source, Class<K> target) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        return source.stream().map(e -> copy(e, target)).toList();
    }

    public static <T> String verify(T t) {
        var validate = VALIDATOR.validate(t);
        if (validate.isEmpty()) {
            return null;
        }
        var messageList = validate.stream().map(ConstraintViolation::getMessage).toList();
        return messageList.toString();
    }
}
