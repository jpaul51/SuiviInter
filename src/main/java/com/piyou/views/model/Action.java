package com.piyou.views.model;

import java.util.HashMap;
import java.util.Map;

public class Action {

	ActionOnSubmit actionOnSubmit;
	
	ActionType actionType;
	
	Map<FieldDetail, Object> updates;

	public ActionOnSubmit getActionOnSubmit() {
		return actionOnSubmit;
	}

	public void setActionOnSubmit(ActionOnSubmit actionOnSubmit) {
		this.actionOnSubmit = actionOnSubmit;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
	public void addFieldUpdate(FieldDetail field, Object value) {
		if(updates == null) {
			updates = new HashMap<FieldDetail, Object>();
		}
		
		updates.put(field, value);
		
	}

	public Map<FieldDetail, Object> getUpdates() {
		return updates;
	}
	
	

	
	
}


