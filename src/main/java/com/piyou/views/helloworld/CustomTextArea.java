package com.piyou.views.helloworld;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("CustomTextArea")
@JsModule("./CustomTextArea.js")
public class CustomTextArea extends PolymerTemplate<TemplateModel> {

	
	@Id("CustomTextArea")
    private TextArea content;

    public void setContent(String newContent) {
    	content.getElement().setText(newContent);
    }
	
}
