package com.cms.pp.cms.pp.Article;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "Article_languages")
public class Language {
    @Id
    @GeneratedValue
    @Column(unique = true, length = 128)
    private int id;

    @Column(name = "language_name")
    private String name;

    @ManyToMany
    @JoinTable(name = "Article_languages_content_relation",
            joinColumns = @JoinColumn(
                    name="language_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "articleContent_id", referencedColumnName = "id")
    )
    private Collection<ArticleContent> articleContents;

}
