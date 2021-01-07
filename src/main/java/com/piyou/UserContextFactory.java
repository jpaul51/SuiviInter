package com.piyou;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class UserContextFactory implements ApplicationContextAware{

	private static UserContext userContext;
	
	public static UserContext getCurrentUserContext() {
		if(userContext == null) {
			userContext = new UserContext();
		}
		return userContext;
	}
	
	public void setApplicationContext(Class<? extends com.piyou.views.model.Application> clazz) {
		if(userContext == null) {
			userContext = new UserContext();
		}
		userContext.setCurrentClass(clazz);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		applicationContext.
	}
	
	
	
}
