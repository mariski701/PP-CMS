package com.cms.pp.cms.pp.Comment;

import com.cms.pp.cms.pp.Article.Article;
import com.cms.pp.cms.pp.Article.ArticleContent;
import com.cms.pp.cms.pp.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    private User user;

    @Column(name = "comment_content")
    private String content;

    @Column(name = "comment_date")
    private java.sql.Date date;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "article_content_id")
    private ArticleContent articleContent;
}
