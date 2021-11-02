package com.cms.pp.cms.pp.Article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
    private String content;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name ="language_id")
    private Language languages;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="article_id")
    private Article article;
}
