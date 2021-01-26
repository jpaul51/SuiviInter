package com.piyou.backend.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.piyou.backend.model.Displayable;
import com.piyou.backend.model.Translation;
import com.piyou.backend.repository.TranslationRepository;
import com.piyou.backend.util.TranslationUtils;

@Service("Translation")
public class TranslationService implements DisplayableService{

	
	@Inject
	TranslationRepository translationRepo;
	


	public List<Translation> getAll(){
		return new ArrayList<Translation>((Collection<Translation>) translationRepo.findAll());
	}



	@Override
	public <T extends Displayable> void update(T d) {
		translationRepo.save((Translation)d);
	}



	@Override
	public <T extends Displayable> void delete(T d) {
		translationRepo.delete((Translation) d);		
		
	}
	
	@Override
	public void reloadContext() {
		DisplayableService.super.reloadContext();
		TranslationUtils.reload();
	}
	
	
	
}
