package com.ppxb.la.base.common.swagger;

import com.ppxb.la.base.common.enumeration.BaseEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SchemaEnum {

    Class<? extends BaseEnum> value();

    String example() default "";

    boolean hidden() default false;

    boolean required() default false;

    String dataType() default "";

    String desc() default "";
}
