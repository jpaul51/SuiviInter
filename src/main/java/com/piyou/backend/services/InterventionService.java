package com.piyou.backend.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.piyou.backend.model.Intervention;
import com.piyou.backend.repository.InterventionRepository;

@Service
public class InterventionService {

	
	@Autowired
	InterventionRepository interRepo;
	
	
	@Transactional
	public void saveInter(Intervention inter) {
		interRepo.save(inter);
	}
	
	public List<Intervention> getInters(){
		return interRepo.findAll(Sort.by("createdDate")); 
	}
	
}
