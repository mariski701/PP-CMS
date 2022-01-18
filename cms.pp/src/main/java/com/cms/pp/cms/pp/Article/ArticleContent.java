package com.cms.pp.cms.pp.Article;

import com.cms.pp.cms.pp.Comment.Comment;
import com.cms.pp.cms.pp.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

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
    @SequenceGenerator(name = "MY_ARTICLECONTENT_SEQ", sequenceName = "MY_ARTICLECONTENT_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_ARTICLECONTENT_SEQ" )
    @Column(unique = true, length = 128, updatable = false, nullable = false)
    private int id;

    @Column(name = "content")
    private String content;

    @Column(name = "title")
    private String title;

    @Column(name = "image")
    private String image;

    @Column(name = "article_published")
    private String published;

    @Column(name = "views")
    private long views = 0;

    @Column(name = "comments_allowed")
    private boolean commentsAllowed;

    @OneToOne
    private User user;

    @Column(name = "article_date")
    private java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());

    @ManyToOne
    @JoinColumn(name ="language_id")
    private Language language;


    @ManyToMany
    @JoinTable(
            name = "article_tags",
            joinColumns = @JoinColumn(name = "articlecontent_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tags_id", referencedColumnName = "id")

    )
    private Collection<ArticleTag> articleTags;

    @OneToMany(mappedBy="articleContent")
    private Collection<Comment> comments;

    @JsonIgnore
    @Version
    private Long version;
}
