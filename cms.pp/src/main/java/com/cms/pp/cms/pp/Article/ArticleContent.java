package com.cms.pp.cms.pp.Article;

import lombok.Data;

import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;

@Data
@Entity
@Table(name = "ArticleContent")
public class ArticleContent {
    @Id
    @GeneratedValue
    @Column(unique = true, length = 128)
    private int id;

    @Column(name = "content")
    private String Content;

    @ManyToMany
    @JoinTable(name = "Article_languages_content_relation",
    joinColumns = @JoinColumn(
            name="articleContent_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "language_id", referencedColumnName = "id")
    )
    private Collection<Language> languages;

    @ManyToOne
    private Article article;
}
