package com.piyou.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import com.piyou.backend.model.Person;
import com.piyou.backend.repository.PersonRepository;

@Service
public class PersonService extends CrudService<Person, Long> {

	
	@Autowired
	private PersonRepository personRepo;
	
	
	
	
	public void addPerson(Person p) {
		
		personRepo.save(p);
	}
	
	
	public List<Person> getAll(){
		return personRepo.findAll();
	}


	@Override
	protected JpaRepository<Person, Long> getRepository() {
		return personRepo;
	}
	
}
