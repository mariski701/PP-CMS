package com.cms.pp.cms.pp.user;


import com.cms.pp.cms.pp.Article.ArticleContent;
import com.cms.pp.cms.pp.Article.Language;
import com.cms.pp.cms.pp.Role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "User")
public class User {
    @Id
    @SequenceGenerator(name = "MY_USER_SEQ", sequenceName = "MY_USER_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_USER_SEQ" )
    @Column(unique = true, length = 128, updatable = false, nullable = false)
    private int id;
    @Column(name = "user_name", unique = true)
    private String userName;
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "user_mail", unique = true)
    private String userMail;
    @Column(name = "user_active")
    private boolean enabled;
    private boolean tokenExpired;

    @ManyToMany
    @JoinTable(name = "users_roles",
    joinColumns = @JoinColumn(
            name = "user_id", referencedColumnName = "id" ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;


    @JsonIgnore
    @Version
    private Long version;

}
