package com.piyou;

import com.piyou.backend.model.Displayable;

public class UserContext {

	private Class<? extends Displayable> currentClass;

	public Class<? extends Displayable> getCurrentClass() {
		return currentClass;
	}

	public void setCurrentClass(Class<? extends Displayable> currentClass) {
		this.currentClass = currentClass;
	}
	
	
}
