package com.cms.pp.cms.pp.Article;


import com.cms.pp.cms.pp.Comment.Comment;
import com.cms.pp.cms.pp.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Data
@Entity
@Table(name = "Articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, length = 128)
    private int id;

    @OneToMany(mappedBy = "article")
    private Collection<ArticleContent> articleContents;

    @OneToOne
    private User user;

    @Column(name = "article_date")
    private java.sql.Date date;

    @Column(name = "article_published")
    private boolean published;

    @ManyToMany(mappedBy = "articles")
    private Collection<ArticleTag> articleTags;

    @OneToMany(mappedBy="article")
    private Collection<Comment> comments;


}
