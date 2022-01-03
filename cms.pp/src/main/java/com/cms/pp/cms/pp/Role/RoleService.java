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

    public String createRole(String name, List<Privilege> privileges) {
        Role role = new Role();
        role.setName(name);
        role.setPrivileges(privileges);
        roleRepository.save(role);
        return "message.2001";
    }

    public String editRole(Long id, List<Privilege> privileges) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            return "message.404";
        }
        role.setPrivileges(privileges);
        roleRepository.save(role);
        return "message.2001";
    }

    public String removeRole(Long id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            return "message.404";
        }
        roleRepository.deleteById(id);
        return "message.2001";
    }
}
