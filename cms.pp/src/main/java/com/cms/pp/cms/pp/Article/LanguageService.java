package com.cms.pp.cms.pp.Article;

import com.cms.pp.cms.pp.Alerts.AlertTranslation;
import com.cms.pp.cms.pp.Alerts.AlertTranslationRepository;
import com.cms.pp.cms.pp.Comment.Comment;
import com.cms.pp.cms.pp.Comment.CommentRepository;
import com.cms.pp.cms.pp.ErrorProvidedDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LanguageService {
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private ArticleContentRepository articleContentRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AlertTranslationRepository alertTranslationRepository;
    @Autowired
    private ArticleTagRepository articleTagRepository;



    public Object addLanguage(Language language) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        Language langTemp = languageRepository.findByName(language.getName());
        Language langTemp2 = languageRepository.findByLanguageCode(language.getLanguageCode());
        if (language.getName().equals(""))
        {
            errorProvidedDataHandler.setError("3037"); //lang name empty
            return errorProvidedDataHandler;
        }
        if (langTemp2 != null) {
            errorProvidedDataHandler.setError("3039");
            return errorProvidedDataHandler;
        }
        if (langTemp != null) {
            errorProvidedDataHandler.setError("3039"); //lang already exists in db
            return errorProvidedDataHandler;
        }
        if (language.getLanguageCode().equals("")) {
            errorProvidedDataHandler.setError("3038");//langcode empty
            return errorProvidedDataHandler;
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
        if (id<=1) {
            errorProvidedDataHandler.setError("3040"); // you cant remove main website language
            return errorProvidedDataHandler;
        }
        List<ArticleContent> articleContentList = articleContentRepository.findAllByLanguage(language);
        List<ArticleTag> articleTagList = articleTagRepository.findByLanguage(language);
        List<AlertTranslation> alertTranslationList = alertTranslationRepository.findAlertTranslationByLanguage(language);
        List<List<Comment>> commentList = new ArrayList<>();

        for (ArticleContent articleContent : articleContentList) {
            commentList.add(commentRepository.findByArticleContent(articleContent));
        }

        for (List<Comment> commentList1 : commentList) {
            commentRepository.deleteAll(commentList1);
        }
        articleContentRepository.deleteAll(articleContentList);
        articleTagRepository.deleteAll(articleTagList);
        alertTranslationRepository.deleteAll(alertTranslationList);

        languageRepository.deleteById(id);
        errorProvidedDataHandler.setError("2001");
        return errorProvidedDataHandler;

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
            errorProvidedDataHandler.setError("3018"); //nie ma takiego języka
            return errorProvidedDataHandler;
        }
        if (language.getName().equals(""))
        {
            errorProvidedDataHandler.setError("3037"); //lang name empty
            return errorProvidedDataHandler;
        }
        if (language.getLanguageCode().equals("")) {
            errorProvidedDataHandler.setError("3038");//langcode empty
            return errorProvidedDataHandler;
        }
        if (language.getName().equals(languageRepository.findByName(language.getName()))) {
            errorProvidedDataHandler.setError("3039"); //provided name is already used by other language in database
            return errorProvidedDataHandler;
        }
        if (language.getLanguageCode().equals(languageRepository.findByLanguageCode(language.getLanguageCode()))) {
            errorProvidedDataHandler.setError("3039");
            return errorProvidedDataHandler;
        }
        language.setName(lang.getName());
        language.setLanguageCode(lang.getLanguageCode());
        languageRepository.save(language);
        errorProvidedDataHandler.setError("2001");
        return errorProvidedDataHandler;
    }
}
