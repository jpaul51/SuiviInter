package com.piyou.backend.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.piyou.backend.model.Translation;
import com.piyou.backend.repository.TranslationRepository;

@Service
public class TranslationService {

	
	@Inject
	TranslationRepository translationRepo;
	


	public List<Translation> getAll(){
		return new ArrayList<Translation>((Collection<Translation>) translationRepo.findAll());
	}
	
	
	
}
