package com.cms.pp.cms.pp.Article;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ArticleDto implements Serializable {
    private Article article;
    private List<ArticleContent> articleContents;
    private List<ArticleTag> articleTags;
    private int userId;
}
