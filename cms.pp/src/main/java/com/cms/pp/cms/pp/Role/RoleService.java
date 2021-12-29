package com.cms.pp.cms.pp.Role;

import com.cms.pp.cms.pp.Priviliges.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Role getRole(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public int createRole(String name, List<Privilege> privileges) {
        Role role = new Role();
        role.setName(name);
        role.setPrivileges(privileges);
        roleRepository.save(role);
        return 2001;
    }

    public int editRole(Long id, List<Privilege> privileges) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            return HttpStatus.NOT_FOUND.value();
        }
        role.setPrivileges(privileges);
        roleRepository.save(role);
        return 2001;
    }

    public int removeRole(Long id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            return HttpStatus.NOT_FOUND.value();
        }
        roleRepository.deleteById(id);
        return 2001;
    }
}
