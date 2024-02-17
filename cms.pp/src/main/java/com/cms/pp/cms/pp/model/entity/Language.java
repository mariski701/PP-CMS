package com.cms.pp.cms.pp.model.entity;

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
    @SequenceGenerator(name = "MY_LANGUAGE_SEQ", sequenceName = "MY_LANGUAGE_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_LANGUAGE_SEQ" )
    @Column(unique = true, length = 128, updatable = false, nullable = false)
    private int id;

    @Column(name = "language_name")
    private String name;

    @Column(name = "language_code")
    private String languageCode;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "language")
    private Collection<ArticleContent> articleContents;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "language")
    private Collection<ArticleTag> articleTags;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "language")
    private Collection<AlertTranslation> alertTranslations;

    @JsonIgnore
    @Version
    private Long version;

}
