package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.model.entity.*;
import com.cms.pp.cms.pp.repository.*;
import com.cms.pp.cms.pp.model.ErrorProvidedDataHandler;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
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
        Language langTemp = languageRepository.findByName(language.getName());
        Language langTemp2 = languageRepository.findByLanguageCode(language.getLanguageCode());
        if (language.getName().isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3037.getValue());
        if (langTemp2 != null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3039.getValue());
        if (langTemp != null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3039.getValue());
        if (language.getLanguageCode().isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3038.getValue());
        languageRepository.save(language);
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
    }

    public Object removeLanguage(int id) {
        Language language = languageRepository.findById(id).orElse(null);
        if (language == null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3018.getValue());
        if (id<=1)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3040.getValue());
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
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());

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
        Language checkLangName = languageRepository.findByName(lang.getName());
        Language checkLangCode = languageRepository.findByLanguageCode(lang.getLanguageCode());
        if (language == null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3018.getValue());

        if (checkLangCode != null || checkLangName != null) {
            if (!language.getName().equals(lang.getName()) || !language.getLanguageCode().equals(lang.getLanguageCode())) {
                return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3039.getValue());
            }
        }
        if (lang.getName().isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3037.getValue());

        if (lang.getLanguageCode().isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3038.getValue());

        language.setName(lang.getName());
        language.setLanguageCode(lang.getLanguageCode());
        languageRepository.save(language);
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
    }
}
