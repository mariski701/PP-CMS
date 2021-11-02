package com.cms.pp.cms.pp.Article;


import com.cms.pp.cms.pp.Comment.Comment;
import com.cms.pp.cms.pp.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Calendar;
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

    @Column(name = "views")
    private long views = 0;

    @OneToMany(mappedBy = "article")
    private Collection<ArticleContent> articleContents;

    @OneToOne
    private User user;

    @Column(name = "article_date")
    private java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());

    @Column(name = "article_published")
    private boolean published;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "articletag_id", referencedColumnName = "id")
    )
    private Collection<ArticleTag> articleTags;

    @OneToMany(mappedBy="article")
    private Collection<Comment> comments;


}
