package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.RoleName;
import com.cms.pp.cms.pp.model.entity.Privilege;
import com.cms.pp.cms.pp.model.entity.Role;
import com.cms.pp.cms.pp.model.entity.User;
import com.cms.pp.cms.pp.repository.RoleRepository;
import com.cms.pp.cms.pp.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@RequiredArgsConstructor
@Service("UserDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = userRepository.findByUserMail(mail);
        if (user == null) {
            return new org.springframework.security.core.userdetails.User(" ", " ",true, true, true, true, getAuthorities(Arrays.asList(roleRepository.findByName(RoleName.ROLE_GUEST.getRoleName()))));

        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getUserPassword(), user.isEnabled(), true, true, true, getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        roles.forEach(role -> {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        });
        collection.forEach(privilege ->
            privileges.add(privilege.getName())
        );
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        privileges.forEach(privilege -> authorities.add(new SimpleGrantedAuthority(privilege)));
        return authorities;
    }
}
