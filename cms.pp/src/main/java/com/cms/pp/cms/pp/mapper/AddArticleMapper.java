package com.cms.pp.cms.pp.mapper;

import com.cms.pp.cms.pp.enums.ArticleStatus;
import com.cms.pp.cms.pp.model.dto.ArticleContentDTO;
import com.cms.pp.cms.pp.model.entity.ArticleContent;
import com.cms.pp.cms.pp.model.entity.ArticleTag;
import com.cms.pp.cms.pp.model.entity.Language;
import com.cms.pp.cms.pp.model.entity.User;
import com.cms.pp.cms.pp.repository.ArticleTagRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Data
public class AddArticleMapper {
    private final ArticleTagRepository articleTagRepository;
    public ArticleContent mapToArticleContent(ArticleContentDTO articleContentDTO, User user, Language language) {
        return new ArticleContent()
                .setTitle(articleContentDTO.getTitle())
                .setArticleTags(getTags(articleContentDTO.getTags()))
                .setContent(articleContentDTO.getContent())
                .setLanguage(language)
                .setPublished(ArticleStatus.UNPUBLSHED.getStatus())
                .setImage(articleContentDTO.getImage())
                .setCommentsAllowed(true)
                .setUser(user);
    }

    private List<ArticleTag> getTags(Collection<Map<String, String>> tags) {
        return tags.stream()
                .map(names -> articleTagRepository.findByName(names.get("name")))
                .collect(Collectors.toList());
    }
}
