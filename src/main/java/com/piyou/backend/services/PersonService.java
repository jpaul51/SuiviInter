package com.piyou.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import com.piyou.backend.model.Displayable;
import com.piyou.backend.model.Person;
import com.piyou.backend.repository.PersonRepository;

@Service("Person")
public class PersonService extends CrudService<Person, Long> implements DisplayableService{

	
	@Autowired
	private PersonRepository personRepo;
	
	
	
	
	public void addPerson(Person p) {
		
		personRepo.save(p);
	}

	@Override
	protected JpaRepository<Person, Long> getRepository() {
		return personRepo;
	}


	@Override
	public <T extends Displayable> void update(T d) {
		personRepo.save((Person)d);
	}

	@Override
	public <T extends Displayable> List<T> getAll() {
		return (List<T>) personRepo.findAll();
	}

	@Override
	public <T extends Displayable> void delete(T d) {
		personRepo.delete((Person) d);		
	}
	
}
