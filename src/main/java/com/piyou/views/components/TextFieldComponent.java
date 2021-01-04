package com.piyou.views.components;

import com.piyou.views.model.FieldDetail;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

public class TextFieldComponent extends AbstractSimpleSuperComponent<String>{

	TextField component;
	
	public TextFieldComponent(FieldDetail field, String label) {
		component = new TextField(label);
	}
	
	@Override
	public TextField getComponent() {
		return component;
	}

	

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return component.getValue();
	}

	@Override
	public void setValue(String value) {
		if(value == null)
			value = "";
		
		component.setValue(value);
		
	}

	@Override
	public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<String>> listener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRequiredIndicatorVisible() {
		// TODO Auto-generated method stub
		return false;
	}

}
