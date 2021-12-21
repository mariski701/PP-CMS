package com.cms.pp.cms.pp.Article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Table(name = "tags")
public class ArticleTag {
    @Id
    @SequenceGenerator(name = "MY_ARTICLETAG_SEQ", sequenceName = "MY_ARTICLETAG_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_ARTICLETAG_SEQ" )
    @Column(unique = true, length = 128, updatable = false, nullable = false)
    private int id;

    private String name;


    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(mappedBy = "articleTags")
    private Collection<ArticleContent> articlesContent;

    @ManyToOne
    @JoinColumn(name ="language_id")
    private Language language;
}
