package com.cms.pp.cms.pp.user;


import com.cms.pp.cms.pp.Role.Role;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue
    @Column(unique = true, length = 128)
    private int id;
    @Column(name = "user_name", unique = true)
    private String userName;
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "user_mail")
    private String userMail;
    @Column(name = "user_active")
    private boolean enabled;
    private boolean tokenExpired;


    @ManyToMany
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(
            name = "user_id", referencedColumnName = "id" ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @Version
    private Long version;

}
