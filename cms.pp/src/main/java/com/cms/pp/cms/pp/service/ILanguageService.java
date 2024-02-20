package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.model.entity.Language;

import java.util.List;

public interface ILanguageService {

	Object addLanguage(Language language);

	Object removeLanguage(int id);

	List<Language> getAllLanguages();

	Language getLanguage(String name);

	Language getLanguageById(int id);

	Object editLanguage(Language lang);

}
