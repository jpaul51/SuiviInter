package com.piyou.views.descriptors;

import com.piyou.backend.model.Person;
import com.piyou.views.model.Application;
import com.piyou.views.model.DetailLayoutManager;
import com.piyou.views.model.FieldDetail;

@MainEntity(Person.class)
public class PersonDescriptor extends Application {

	
	public PersonDescriptor() {
		
		this.setAppLabelKey(EAppTranslation.APP_LABEL_PERSON.name());
		this.setAppName("persons");

		FieldDetail loginField = new FieldDetail();
		loginField.setName("login");
		loginField.setTranslationKey(EAppFieldsTranslation.APP_FIELDS_LOGIN.name());

		FieldDetail nameField = new FieldDetail();
		nameField .setName("lastName");
		nameField.setTranslationKey(EAppFieldsTranslation.APP_FIELDS_LAST_NAME.name());
		
		FieldDetail firstNameField = new FieldDetail();
		firstNameField.setName("firstName");
		firstNameField.setTranslationKey(EAppFieldsTranslation.APP_FIELDS_FIRST_NAME.name());

		FieldDetail mailField = new FieldDetail();
		mailField.setName("email");
		mailField.setTranslationKey(EAppFieldsTranslation.APP_FIELDS_MAIL_NAME.name());
	
		
		this.setDlManager(DetailLayoutManager.createSimpleDetail(loginField, nameField, firstNameField, mailField));
		
		
	}
	
	
}
