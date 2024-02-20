package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.model.dto.ArticleContentDTO;
import com.cms.pp.cms.pp.model.entity.ArticleContent;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IArticleContentService {

	Object addArticleContent(ArticleContentDTO articleContentDTO);

	@Nullable
	ArticleContent getArticleContent(int id);

	Object changeArticleStatus(int id, String articleStatus);

	Object removeArticle(int id);

	Object editArticle(ArticleContentDTO articleContentDTO);

	List<ArticleContent> findAll();

	List<ArticleContent> findAllByLanguage(String lang);

	List<ArticleContent> findAllByUser(int id);

	List<ArticleContent> findSomeArticlesByViews(int count, String lang);

	List<ArticleContent> findByTitleIgnoreCaseContaining(String title);

	List<ArticleContent> findByTitleIgnoreCaseContainingOrByTags(String language, String title,
			List<Map<String, String>> tagNames);

	ArticleContent findByTitle(String title);

	List<ArticleContent> findSomeArticlesByLazyLoading(int page, int size, String title);

	Object allowCommentsInArticle(int id, boolean allowComments);

	ArticleContent getArticleContentByCommentId(Long id);

	List<ArticleContent> findByTag(String tagName);

	List<ArticleContent> getAllForCMS();

	List<ArticleContent> getAllByUsersInCMS();

}
