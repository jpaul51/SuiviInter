package com.piyou.views.descriptors;

import java.time.LocalDateTime;

import com.piyou.backend.model.Intervention;
import com.piyou.backend.util.TranslationUtils;
import com.piyou.views.model.Action;
import com.piyou.views.model.ActionType;
import com.piyou.views.model.Application;
import com.piyou.views.model.DetailLayoutManager;
import com.piyou.views.model.FieldDetail;
import com.piyou.views.model.Input;

@MainEntity(Intervention.class)
public class InterventionDescriptor extends Application{

	
	public InterventionDescriptor() {
		
		this.setAppLabelKey(TranslationUtils.translate(EAppTranslation.APP_LABEL_INTERVENTION.name()));
		
		FieldDetail descField = new FieldDetail();
		descField.setType(Input.TEXT_AREA);
		descField.setTranslationKey(EAppFieldsTranslation.APP_FIELDS_DESCRIPTION.name());
		descField.setName("description");
		
		FieldDetail commentField = new FieldDetail();
		commentField.setType(Input.TEXT_RICH);
		commentField.setTranslationKey(EAppFieldsTranslation.APP_FIELDS_COMMENT.name());
		commentField.setName("commentaire");
		
		
		FieldDetail createdDate = new FieldDetail();
		createdDate.setType(Input.DATE_TIME);
		createdDate.setTranslationKey(EAppFieldsTranslation.APP_FIELDS_CREATED_DATE.name());
		createdDate.setName("createdDate");
		createdDate.setReadOnly(true);
		createdDate.setDefaultValue(LocalDateTime.now());

		FieldDetail modifiedDate = new FieldDetail();
		modifiedDate .setType(Input.DATE_TIME);
		modifiedDate .setTranslationKey(EAppFieldsTranslation.APP_FIELDS_MODIFIED_DATE.name());
		modifiedDate.setName("lastModifiedDate");
		modifiedDate.setReadOnly(true);
		modifiedDate.setDefaultValue(LocalDateTime.now());
		
		FieldDetail duration = new FieldDetail();
		duration.setType(Input.DATE_TIME_ONLY);
		duration.setTranslationKey(EAppFieldsTranslation.APP_FIELDS_DURATION.name());
		duration.setName("duration");
		
		FieldDetail projectField = new FieldDetail();
		projectField.setType(Input.SELECT);
		projectField.setTranslationKey(EAppTranslation.APP_LABEL_PROJECT.name());
		projectField.setName("project");
		projectField.setEntityDescriptor(ProjectDescriptor.class);
		
		this.setDlManager(DetailLayoutManager.createSimpleDetail(descField, commentField, createdDate, modifiedDate, duration, projectField));
		
		Action onSubmit = new Action();
		onSubmit.setActionType(ActionType.SUBMIT);
		onSubmit.addFieldUpdate(modifiedDate, LocalDateTime.now());
		
		this.addAction(onSubmit);
		
	}
	
	
	//	
//	
//	private String description;
//	private String commentaire;
//	
//	@ManyToOne
//	private Person owner;
//	private LocalDateTime createdDate;
//	private LocalDateTime lastModifiedDate;
//	private LocalTime duration;
//	@OneToOne
//	private Project project;
	
	
	
}
