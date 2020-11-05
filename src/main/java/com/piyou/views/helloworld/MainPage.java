package com.piyou.views.helloworld;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("main-page")
@JsModule("./CustomTextArea.js")
public class MainPage extends PolymerTemplate<TemplateModel> {

    @Id("textarea")
    private TextArea content;
    

    public void setContent(String content) {
//        this.content.removeAll();
//        this.content.add(content);
    	this.content.setLabel("TEST");
    	this.content.setValue(content);
    	this.content.setId("youpi");
    	System.out.println("TEST----------------|--");
    	System.out.println(this.content.getElement().getComponent().get().getElement().getOuterHTML());
    }
}

