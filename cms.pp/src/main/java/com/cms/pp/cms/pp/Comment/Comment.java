package com.cms.pp.cms.pp.Comment;

import com.cms.pp.cms.pp.Article.Article;
import com.cms.pp.cms.pp.user.User;
import lombok.Data;
import lombok.Generated;

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
    private Date date;

    @OneToOne
    private Article article;
}
