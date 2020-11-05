package com.piyou.backend.model;

import java.io.Serializable;

import com.vaadin.flow.function.ValueProvider;

public interface Displayable<T> extends Serializable{

	
	public String getLabel();
	
	public ValueProvider<T,?> getPropertyValue( ValueProvider<? extends T,?> provider);

//	ValueProvider<? extends Serializable, ?> getPropertyValue(ValueProvider<?, ?> provider);
	
}
