package com.cms.pp.cms.pp.Role;

import com.cms.pp.cms.pp.ErrorProvidedDataHandler;
import com.cms.pp.cms.pp.Priviliges.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Object createRole(String name, List<Privilege> privileges) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        Role role = new Role();

        if (name.equals("")) {
            errorProvidedDataHandler.setError("3021");
            return errorProvidedDataHandler;
        }
        if (privileges.isEmpty()) {
            errorProvidedDataHandler.setError("3022");
            return errorProvidedDataHandler;
        }

        role.setName(name);
        role.setPrivileges(privileges);
        errorProvidedDataHandler.setError("2001");
        roleRepository.save(role);
        return errorProvidedDataHandler;
    }

    public Object editRole(Long id, List<Privilege> privileges) {
        Role role = roleRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (role == null) {
            errorProvidedDataHandler.setError("3020");
            return errorProvidedDataHandler;
        }
        role.setPrivileges(privileges);
        errorProvidedDataHandler.setError("2001");
        roleRepository.save(role);
        return errorProvidedDataHandler;
    }

    public Object removeRole(Long id) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            errorProvidedDataHandler.setError("3020");
            return errorProvidedDataHandler;
        }
        roleRepository.deleteById(id);
        errorProvidedDataHandler.setError("2001");
        return errorProvidedDataHandler;
    }
}
