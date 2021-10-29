package com.cms.pp.cms.pp.Article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Table(name = "Article_tags")
public class ArticleTag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToMany
    @JoinTable(
    joinColumns = @JoinColumn(name = "articletag_id"),
    inverseJoinColumns = @JoinColumn(name = "article_id"))
    private Collection<Article> articles;
}
