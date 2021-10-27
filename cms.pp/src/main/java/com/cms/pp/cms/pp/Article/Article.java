package com.cms.pp.cms.pp.Article;


import com.cms.pp.cms.pp.user.User;
import lombok.Data;
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

    @OneToMany
    private Collection<ArticleContent> articleContents;

    @OneToOne
    private User user;

    @Column(name = "article_date")
    private java.sql.Date date;

    @Column(name = "article_published")
    private boolean published;


}
