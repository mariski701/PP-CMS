package com.cms.pp.cms.pp.Article;

import com.cms.pp.cms.pp.Comment.Comment;
import com.cms.pp.cms.pp.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Calendar;
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

    @Column(name = "article_published")
    private boolean published;

    @Column(name = "views")
    private long views = 0;

    @OneToOne
    private User user;

    @Column(name = "article_date")
    private java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());

    @ManyToOne
    @JoinColumn(name ="language_id")
    private Language languages;


    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "articletag_id", referencedColumnName = "id")
    )
    private Collection<ArticleTag> articleTags;

    @OneToMany(mappedBy="articleContent")
    private Collection<Comment> comments;
}
