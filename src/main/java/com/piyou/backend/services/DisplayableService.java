package com.piyou.backend.services;

import java.util.List;

import com.piyou.backend.model.Displayable;

public interface DisplayableService {

	public <T extends Displayable> List<T> getAll();
	
	public <T extends Displayable> void  update(T d);
	
	public <T extends Displayable> void delete(T d);
	
	/**
	 * Used for refreshing context data from newly inserted values
	 * For example: reload translation list after edition
	 */
	default public  void reloadContext() {
		
	}
	
}
