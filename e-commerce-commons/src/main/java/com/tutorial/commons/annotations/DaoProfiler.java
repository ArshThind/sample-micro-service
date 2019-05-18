package com.tutorial.commons.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta annotation used by {@link com.tutorial.commons.aspects.CommonAspects} to log method timings.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DaoProfiler {

    @AliasFor("value")
    String queryName() default "";

    @AliasFor("queryName")
    String value() default "";
}
