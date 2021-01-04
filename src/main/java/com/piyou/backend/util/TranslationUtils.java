package com.piyou.backend.util;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.piyou.backend.model.Translation;
import com.piyou.backend.services.TranslationService;

@Component
public class TranslationUtils {

	@Autowired
	private TranslationService translationServiceInstance;
	
	private static TranslationService translationService;
	
	private static List<Translation> translations;
	
	public static Locale locale = Locale.FRANCE;
	
	
	@PostConstruct
	public void init() {
		TranslationUtils.translationService = translationServiceInstance;
		translations = translationService.getAll();
	}
	
	public static String translate(String key) {
				
		Optional<Translation> translation = translations.stream().filter(t ->{
			return t.getLoc().equals(TranslationUtils.locale);	
		}).filter(t -> {
			return t.getKey().equals(key);
		}).findFirst();
		
		if(translation.isPresent()) {
			return translation.get().getValue();
		}else {
			return key;
		}
		
	}
	
	
	
	
	
	
	
}
