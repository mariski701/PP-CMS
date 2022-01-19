package com.cms.pp.cms.pp.Article;

import com.cms.pp.cms.pp.ErrorProvidedDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    @Autowired
    private LanguageRepository languageRepository;

    public Object addLanguage(Language language) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        Language langTemp = languageRepository.findByName(language.getName());
        if (language.getName().equals(""))
        {
            errorProvidedDataHandler.setError("3037"); //lang name empty
            return errorProvidedDataHandler;
        }
        if (langTemp != null) {
            errorProvidedDataHandler.setError("3039"); //lang already exists in db
            return errorProvidedDataHandler;
        }
        if (language.getLanguageCode().equals("")) {
            errorProvidedDataHandler.setError("3038");//langcode empty
        }
        languageRepository.save(language);
        errorProvidedDataHandler.setError("2001"); //success
        return errorProvidedDataHandler;
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

    public Language getLanguageById(int id) {
        return languageRepository.findById(id).orElse(null);
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
