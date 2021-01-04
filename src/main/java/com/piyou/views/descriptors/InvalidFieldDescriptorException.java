package com.piyou.views.descriptors;

public class InvalidFieldDescriptorException extends Exception {
	
	String message;
	
	public InvalidFieldDescriptorException(String message) {
		this.message = message;
	}

	
	@Override
	public String getLocalizedMessage() {
		// TODO Auto-generated method stub
		return message;
	}
}
