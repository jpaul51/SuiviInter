package com.piyou.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import com.piyou.backend.model.Project;
import com.piyou.backend.repository.ProjectRepository;

@Service
public class ProjectService  extends CrudService<Project, Long>{

	
	@Autowired
	private ProjectRepository projectRepo;
	
	public List<Project> getAll(){
		return projectRepo.findAll();
	}
	
	@Override
	protected JpaRepository<Project, Long> getRepository() {
			return projectRepo;
	}

}
