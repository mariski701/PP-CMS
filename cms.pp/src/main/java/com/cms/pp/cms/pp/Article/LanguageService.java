package com.cms.pp.cms.pp.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    @Autowired
    private LanguageRepository languageRepository;

    public Language addLanguage(Language language) {
        return  languageRepository.save(language);
    }

    public String removeLanguage(int id) {
        Language language = languageRepository.findById(id).orElse(null);
        if (language == null) {
            return "Error";
        }
        else {
            languageRepository.deleteById(id);
            return "Successfully removed";
        }
    }

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }
}
