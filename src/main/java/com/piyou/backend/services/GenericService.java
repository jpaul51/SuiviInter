package com.piyou.backend.services;

import java.util.List;

import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.piyou.backend.model.Displayable;

@Service
public class GenericService<T extends Displayable> {

	@Autowired
	Provider<JpaRepository<T,Long>> repositoryProvider;
	
	public void persistObject(T obj) {
		repositoryProvider.get().save(obj);
	}
	
	public List<T> getAll() {
		return (List<T>) repositoryProvider.get().findAll();
	}
	
}
