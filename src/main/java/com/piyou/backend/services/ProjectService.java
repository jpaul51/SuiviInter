package com.piyou.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piyou.backend.model.Displayable;
import com.piyou.backend.model.Project;
import com.piyou.backend.repository.ProjectRepository;

@Service("Project")
public class ProjectService implements DisplayableService {

	
	@Autowired
	private ProjectRepository projectRepo;
	
	public List<Project> getAll(){
		return (List<Project>) projectRepo.findAll();
	}

	@Override
	public <T extends Displayable> void update(T d) {
		projectRepo.save((Project) d);
	}

	@Override
	public <T extends Displayable> void delete(T d) {
		projectRepo.delete((Project) d);
	}
	
//	protected GenericRepository<Project> getRepository() {
//			return projectRepo;
//	}

}
