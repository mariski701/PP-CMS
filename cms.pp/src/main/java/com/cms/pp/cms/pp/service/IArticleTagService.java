package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.model.entity.ArticleTag;

import java.util.List;

public interface IArticleTagService {
    List<ArticleTag> getArticleTags();
    ArticleTag getArticleTag(int id);
    Object addTag(String language, String name);
    Object removeTag(int id);
    Object modifyTag(int id, ArticleTag articleTag);
    List<ArticleTag> findByLanguage(String lang);
}
