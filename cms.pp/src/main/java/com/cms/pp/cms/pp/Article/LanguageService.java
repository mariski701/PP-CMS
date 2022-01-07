package com.cms.pp.cms.pp.Article;

import com.cms.pp.cms.pp.ErrorProvidedDataHandler;
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

    public Object removeLanguage(int id) {
        Language language = languageRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (language == null) {
            errorProvidedDataHandler.setError("3018");
            return errorProvidedDataHandler;
        }
        else {
            languageRepository.deleteById(id);
            errorProvidedDataHandler.setError("2001");
            return errorProvidedDataHandler;
        }
    }

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    public Language getLanguage(String name) {
        return languageRepository.findByName(name);
    }

    public Object editLanguage(Language lang) {
        Language language = languageRepository.findById(lang.getId()).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (language == null)
        {
            errorProvidedDataHandler.setError("3018");
            return errorProvidedDataHandler;
        }
        language.setName(lang.getName());
        language.setLanguageCode(lang.getLanguageCode());
        languageRepository.save(language);
        errorProvidedDataHandler.setError("2001");
        return errorProvidedDataHandler;
    }
}
