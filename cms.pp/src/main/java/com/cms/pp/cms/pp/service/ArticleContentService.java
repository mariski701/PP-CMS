package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.ArticleStatus;
import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.enums.PrivilegeName;
import com.cms.pp.cms.pp.mapper.AddArticleMapper;
import com.cms.pp.cms.pp.model.dto.ArticleContentDTO;
import com.cms.pp.cms.pp.model.entity.*;
import com.cms.pp.cms.pp.repository.*;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import com.cms.pp.cms.pp.utils.PrincipalUtils;
import com.cms.pp.cms.pp.validator.AddArticleContentRequestValidator;
import com.cms.pp.cms.pp.validator.EditArticleRequestValidator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@Service("ArticleContentService")
public class ArticleContentService implements IArticleContentService {

	private static final String ANONYMOUS_USER = "anonymousUser";

	private final ArticleContentRepository articleContentRepository;

	private final LanguageRepository languageRepository;

	private final ArticleTagRepository articleTagRepository;

	private final UserRepository userRepository;

	private final CommentRepository commentRepository;

	private final RoleRepository roleRepository;

	private final PrivilegeRepository privilegeRepository;

	private final AddArticleMapper addArticleMapper;

	private final AddArticleContentRequestValidator addArticleContentRequestValidator;

	private final EditArticleRequestValidator editArticleRequestValidator;

	@Override
	public Object addArticleContent(ArticleContentDTO articleContentDTO) {
		String username = PrincipalUtils
			.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Object validateRequest = addArticleContentRequestValidator.validateAddArticleContent(articleContentDTO,
				username);
		if (validateRequest != null)
			return validateRequest;
		articleContentRepository
			.save(addArticleMapper.mapToArticleContent(articleContentDTO, userRepository.findByUserName(username),
					languageRepository.findByName(articleContentDTO.getLanguage())));
		return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
	}

	@Override
	@Nullable
	public ArticleContent getArticleContent(int id) {
		ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
		if (articleContent == null)
			return null;
		long views = articleContent.getViews();
		views++;
		articleContent.setViews(views);
		articleContentRepository.save(articleContent);
		return articleContent;
	}

	@Override
	public Object changeArticleStatus(int id, String articleStatus) {
		ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
		if (articleStatus != null && (articleStatus.equals(ArticleStatus.PUBLISHED.getStatus())
				|| articleStatus.equals(ArticleStatus.UNPUBLSHED.getStatus()))) {
			if (articleContent == null)
				return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3016.getValue());
			if (articleStatus.equals(ArticleStatus.PUBLISHED.getStatus()))
				articleContent.setDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			articleContent.setPublished(articleStatus);
			articleContentRepository.save(articleContent);
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
		}
		return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3034.getValue());
	}

	@Override
	public Object removeArticle(int id) {
		ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
		if (articleContent == null)
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3016.getValue());
		List<Comment> comments = commentRepository.findByArticleContent(articleContent);
		commentRepository.deleteAll(comments);
		articleContentRepository.deleteById(id);
		return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());

	}

	@Override
	public Object editArticle(ArticleContentDTO articleContentDTO) {
		String username = PrincipalUtils
			.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Object requestValidator = editArticleRequestValidator.validateEditArticle(articleContentDTO, username);
		if (requestValidator != null)
			return requestValidator;
		ArticleContent articleContent = articleContentRepository.findById(articleContentDTO.getId()).orElse(null);
		if (articleContent == null)
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3030.getValue());
		User editedArticleOfUser = userRepository.findById(articleContent.getUser().getId()).orElse(null);
		if (editedArticleOfUser == null)
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3030.getValue());
		Role editedArticleUserRole = editedArticleOfUser.getRoles().stream().findAny().orElse(null);
		Collection<Privilege> editedArticleUserPrivileges = roleRepository.findByName(editedArticleUserRole.getName())
			.getPrivileges();
		User principalUser = userRepository.findByUserName(username);
		Role principalRole = principalUser.getRoles().stream().findAny().orElse(null);
		Collection<Privilege> principalPrivileges = roleRepository.findByName(principalRole.getName()).getPrivileges();
		boolean canEditArticle = canEditArticle(editedArticleUserPrivileges, principalPrivileges);
		if (canEditArticle) {
			articleContent.setTitle(articleContentDTO.getTitle())
				.setLanguage(languageRepository.findByName(articleContentDTO.getLanguage()))
				.setArticleTags(articleContentDTO.getTags()
					.stream()
					.map(names -> articleTagRepository.findByName(names.get("name")))
					.collect(Collectors.toList()))
				.setContent(articleContentDTO.getContent())
				.setImage(articleContentDTO.getImage());
			articleContentRepository.save(articleContent);
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
		}
		return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3033.getValue());
	}

	private boolean canEditArticle(Collection<Privilege> editedArticleUserPrivileges,
			Collection<Privilege> principalPrivileges) {
		boolean isAdmin = principalPrivileges
			.contains(privilegeRepository.findByName(PrivilegeName.EDIT_ADMINS_ARTICLE.getPrivilegeName()));
		boolean isModeratorAndEditor = principalPrivileges
			.contains(privilegeRepository.findByName(PrivilegeName.EDIT_MODERATORS_ARTICLE.getPrivilegeName()))
				&& principalPrivileges
					.contains(privilegeRepository.findByName(PrivilegeName.EDIT_EDITORS_ARTICLE.getPrivilegeName()))
				&& !isAdmin;
		boolean isEditor = principalPrivileges
			.contains(privilegeRepository.findByName(PrivilegeName.EDIT_EDITORS_ARTICLE.getPrivilegeName()))
				&& !principalPrivileges
					.contains(privilegeRepository.findByName(PrivilegeName.EDIT_MODERATORS_ARTICLE.getPrivilegeName()))
				&& !isAdmin && editedArticleUserPrivileges
					.contains(privilegeRepository.findByName(PrivilegeName.EDIT_EDITORS_ARTICLE.getPrivilegeName()));
		return isAdmin || isModeratorAndEditor || isEditor;
	}

	@Override
	public List<ArticleContent> findAll() {
		return articleContentRepository.findAll();
	}

	@Override
	public List<ArticleContent> findAllByLanguage(String lang) {
		Language language = languageRepository.findByName(lang);
		if (lang == null)
			return null;
		return articleContentRepository.findAllByLanguageAndPublished(language, ArticleStatus.PUBLISHED.getStatus(),
				Sort.by("id").descending());
	}

	@Override
	public List<ArticleContent> findAllByUser(int id) {
		User user = userRepository.findById(id).orElse(null);
		if (user == null)
			return null;
		return articleContentRepository.findAllByUser(user, Sort.by("id").descending());

	}

	@Override
	public List<ArticleContent> findSomeArticlesByViews(int count, String lang) {
		Language language = languageRepository.findByName(lang);
		if (language == null)
			return null;
		Pageable pageableWithElements = PageRequest.of(0, count, Sort.by("views").descending());
		return articleContentRepository.findAllByLanguageAndPublished(language, ArticleStatus.PUBLISHED.getStatus(),
				pageableWithElements);
	}

	@Override
	public List<ArticleContent> findByTitleIgnoreCaseContaining(String title) {
		return articleContentRepository.findByTitleIgnoreCaseContaining(title, Sort.by("id").descending());
	}

	@Override
	public List<ArticleContent> findByTitleIgnoreCaseContainingOrByTags(String language, String title,
			List<Map<String, String>> tagNames) {
		Language lang = languageRepository.findByName(language);
		if (lang == null)
			return null;
		if (title.isEmpty() && tagNames != null) {
			List<ArticleTag> articleTagList = tagNames.stream()
				.map(tagName -> articleTagRepository.findByName(tagName.get("name")))
				.collect(Collectors.toList());
			return articleTagList.stream()
				.map(articleTag -> articleContentRepository.findByPublishedAndArticleTagsAndLanguage(
						ArticleStatus.PUBLISHED.getStatus(), articleTag, lang, Sort.by("id").descending()))
				.flatMap(List::stream)
				.collect(Collectors.toList());
		}
		if (tagNames == null)
			return articleContentRepository.findByTitleIgnoreCaseContainingAndPublishedAndLanguage(title,
					ArticleStatus.PUBLISHED.getStatus(), lang, Sort.by("id").descending());
		List<ArticleTag> articleTagList = tagNames.stream()
			.map(tag -> articleTagRepository.findByName(tag.get("name")))
			.collect(Collectors.toList());
		return articleTagList.stream()
			.map(articleTag -> articleContentRepository
				.findByTitleIgnoreCaseContainingAndPublishedAndArticleTagsAndLanguage(title,
						ArticleStatus.PUBLISHED.getStatus(), articleTag, lang, Sort.by("title").ascending()))
			.flatMap(List::stream)
			.collect(Collectors.toList());
	}

	@Override
	public ArticleContent findByTitle(String title) {
		return articleContentRepository.findByTitle(title);
	}

	@Override
	public List<ArticleContent> findSomeArticlesByLazyLoading(int page, int size, String title) {
		Pageable pageableWithElements = PageRequest.of(page, size, Sort.by("id").descending());
		return articleContentRepository.findByTitleIgnoreCaseContaining(title, pageableWithElements);
	}

	@Override
	public Object allowCommentsInArticle(int id, boolean allowComments) {
		ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
		if (articleContent == null)
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3030.getValue());
		articleContent.setCommentsAllowed(allowComments);
		articleContentRepository.save(articleContent);
		return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
	}

	@Override
	public ArticleContent getArticleContentByCommentId(Long id) {
		Comment comment = commentRepository.findById(id).orElse(null);
		if (comment == null)
			return null;
		return articleContentRepository.findArticleContentByComments(comment);
	}

	@Override
	public List<ArticleContent> findByTag(String tagName) {
		ArticleTag articleTag = articleTagRepository.findByName(tagName);
		if (articleTag == null)
			return null;
		return articleContentRepository.findByPublishedAndArticleTags(ArticleStatus.PUBLISHED.getStatus(), articleTag,
				Sort.by("id").descending());
	}

	@Override
	public List<ArticleContent> getAllForCMS() {
		return articleContentRepository.findAll(Sort.by("id").descending());
	}

	@Override
	public List<ArticleContent> getAllByUsersInCMS() {
		String username = PrincipalUtils
			.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		if (username.equals(ANONYMOUS_USER)) {
			return null;
		}
		return articleContentRepository.findAllByUser(userRepository.findByUserName(username),
				Sort.by("id").descending());
	}

}
