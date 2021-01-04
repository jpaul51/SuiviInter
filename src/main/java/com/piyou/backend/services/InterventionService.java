package com.piyou.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piyou.backend.model.Displayable;
import com.piyou.backend.repository.InterventionRepository;

@Service("Intervention")
public class InterventionService implements DisplayableService {

	
	@Autowired
	InterventionRepository interRepo;

	@Override
	public <Intervention extends Displayable> List<Intervention> getAll() {
		return (List<Intervention>) interRepo.findAll();
	}

	@Override
	public <I extends Displayable> void update(I d) {
		interRepo.saveOne( (com.piyou.backend.model.Intervention) d);
	}

	@Override
	public <T extends Displayable> void delete(T d) {
		interRepo.delete((com.piyou.backend.model.Intervention) d);
	}
}
