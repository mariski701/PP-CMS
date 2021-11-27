package com.cms.pp.cms.pp.Article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "languages")
public class Language {
    @Id
    @GeneratedValue
    @Column(unique = true, length = 128)
    private int id;

    @Column(name = "language_name")
    private String name;

    @Column(name = "language_code")
    private String languageCode;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "languages")
    private Collection<ArticleContent> articleContents;

}
