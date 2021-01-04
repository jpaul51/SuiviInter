package com.piyou.views.components;

import org.vaadin.alump.lazylayouts.LazyVerticalLayout;

import com.piyou.views.model.FieldDetail;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.shared.Registration;
import com.wontlost.ckeditor.VaadinCKEditor;

public class RichTextEditorComponent extends AbstractSuperCustomField 
{
	
//	TextField data;
	VaadinCKEditor ckEditor; 
	
	public RichTextEditorComponent(FieldDetail field) {
		
		ckEditor = RichTextEditorBuilder.richTextEditor("");
		this.add(ckEditor);
	
//		ckEditor.addValueChangeListener(v ->{
//			data.setValue(ckEditor.getValue());
//		});
		
	}
	
	@Override
	public VaadinCKEditor getComponent() {
		return ckEditor;
	}



	

	@Override
	public Registration addValueChangeListener(ValueChangeListener listener) {
		// TODO Auto-generated method stub
		return ckEditor.addValueChangeListener(listener);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		ckEditor.setReadOnly(readOnly);
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



	@Override
	public FieldDetail getFieldDetail() {
		// TODO Auto-generated method stub
		return field;
	}

	@Override
	public void setFieldDetail(FieldDetail field) {
		this.field = field;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return ckEditor.getValue();
	}

	@Override
	public void setValue(String value) {
		ckEditor.setValue(value);
	}

	@Override
		public void add(Component... components) {
			super.add(components);
		}
	
	
	
}
