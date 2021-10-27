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

    @ManyToOne
    private Language languages;

    @ManyToOne
    private Article article;
}
