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

    @OneToMany
    private Collection<ArticleContent> articleContents;

}
