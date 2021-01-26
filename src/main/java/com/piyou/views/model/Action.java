package com.piyou.views.model;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.piyou.backend.services.DisplayableService;
import com.vaadin.flow.function.ValueProvider;

public class Action {

	ActionOnSubmit actionOnSubmit;
	
	ActionType actionType;
	
	Map<FieldDetail, Object> updates;
	
	
	boolean before = true;

	private Consumer<DisplayableService> serviceAction;

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

	
	public boolean isBefore() {
		return before;
	}

	public void setBefore(boolean before) {
		this.before = before;
	}

	public Map<FieldDetail, Object> getUpdates() {
		return updates;
	}

	public void setServiceAction(Consumer<DisplayableService> consumer) {
		this.serviceAction = consumer;		
	}

	public Consumer<DisplayableService> getServiceAction() {
		return serviceAction;
	}

	
	
	

	
	
}


