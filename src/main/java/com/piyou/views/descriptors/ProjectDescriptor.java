package com.piyou.views.descriptors;


import java.util.Arrays;

import com.piyou.backend.model.Project;
import com.piyou.views.model.Application;
import com.piyou.views.model.Bloc;
import com.piyou.views.model.Detail;
import com.piyou.views.model.DetailLayoutManager;
import com.piyou.views.model.FieldDetail;
import com.piyou.views.model.Input;
import com.piyou.views.model.Line;


@MainEntity(Project.class)
public class ProjectDescriptor extends Application{

	
	
	public ProjectDescriptor() {
		
		this.setAppLabelKey(EAppTranslation.APP_LABEL_PROJECT.name());
		this.setAppName("projects");
		
		
		FieldDetail labelField = new FieldDetail();
		labelField.setType(Input.TEXT_INPUT);
		labelField.setName("name");
		labelField.setTranslationKey(EAppFieldsTranslation.APP_FIELDS_LABEL.name());
		
		FieldDetail description = new FieldDetail();
		description.setType(Input.TEXT_AREA);
		description.setName("description");
		description.setTranslationKey(EAppFieldsTranslation.APP_FIELDS_DESCRIPTION.name());

		FieldDetail projectManager = new FieldDetail();
		projectManager.setType(Input.SELECT);
		projectManager.setName("projectManager");
		projectManager.setTranslationKey(EAppFieldsTranslation.APP_FIELDS_MANAGER.name());
		projectManager.setEntityDescriptor(PersonDescriptor.class);
		
		
		Line headLine = new Line();
		headLine.setFields(Arrays.asList(labelField, description, projectManager));
		
		
		Bloc bHeader = new Bloc();
		bHeader.setLines(Arrays.asList(headLine));
		
		DetailLayoutManager dl = new DetailLayoutManager();
		Detail defaultDetail = new Detail();
		defaultDetail.setBlocs(Arrays.asList(bHeader));
		
		dl.setDefaultDetail(defaultDetail);
		
		this.setDlManager(dl);
		
	}
	
}
