package com.piyou.views.main;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DisplayableField {
	boolean hideInGrid() default false;
	boolean hideInDetail() default false;
	Class<? extends Object> clazz() default String.class;
	
}
