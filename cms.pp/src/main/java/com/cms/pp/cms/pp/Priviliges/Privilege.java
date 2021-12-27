package com.cms.pp.cms.pp.Priviliges;

import com.cms.pp.cms.pp.Role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
@Data
@Entity
public class Privilege {

    @Id
    @SequenceGenerator(name = "MY_PRIVILEGE_SEQ", sequenceName = "MY_PRIVILEGE_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_PRIVILEGE_SEQ" )
    @Column(unique = true, length = 128, updatable = false, nullable = false)
    private Long id;

    private String name;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    @Version
    private Long version;
}
