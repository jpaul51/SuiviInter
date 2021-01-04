package com.piyou.views.descriptors;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.piyou.backend.model.Displayable;

@Retention(RetentionPolicy.RUNTIME)
public @interface MainEntity {

	public Class<? extends Displayable> value();
	
}
