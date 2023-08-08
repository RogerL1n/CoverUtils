package com.lry.annotation;

/**
 * @program: CoverUtils
 * @description:
 * @author: Pck
 * @create: 2023-08-08 17:12
 **/



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Debounce {
}
