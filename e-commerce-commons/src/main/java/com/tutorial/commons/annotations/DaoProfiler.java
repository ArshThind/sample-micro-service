package com.tutorial.commons.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DaoProfiler {

    @AliasFor("value")
    String queryName() default "";

    @AliasFor("queryName")
    String value() default "";
}
