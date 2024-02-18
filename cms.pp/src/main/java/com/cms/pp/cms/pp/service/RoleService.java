package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.model.ErrorProvidedDataHandler;
import com.cms.pp.cms.pp.model.entity.Privilege;
import com.cms.pp.cms.pp.model.entity.Role;
import com.cms.pp.cms.pp.repository.RoleRepository;
import com.cms.pp.cms.pp.enums.Code;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@RequiredArgsConstructor
@Service
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Role getRole(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Object createRole(String name, List<Privilege> privileges) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        Role role = new Role();

        if (name.isEmpty()) {
            errorProvidedDataHandler.setError(Code.CODE_3021.getValue());
            return errorProvidedDataHandler;
        }
        if (privileges.isEmpty()) {
            errorProvidedDataHandler.setError(Code.CODE_3022.getValue());
            return errorProvidedDataHandler;
        }

        role.setName(name);
        role.setPrivileges(privileges);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        roleRepository.save(role);
        return errorProvidedDataHandler;
    }

    public Object editRole(Long id, List<Privilege> privileges) {
        Role role = roleRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (role == null) {
            errorProvidedDataHandler.setError(Code.CODE_3020.getValue());
            return errorProvidedDataHandler;
        }
        role.setPrivileges(privileges);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        roleRepository.save(role);
        return errorProvidedDataHandler;
    }

    public Object removeRole(Long id) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            errorProvidedDataHandler.setError(Code.CODE_3020.getValue());
            return errorProvidedDataHandler;
        }
        roleRepository.deleteById(id);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        return errorProvidedDataHandler;
    }
}
