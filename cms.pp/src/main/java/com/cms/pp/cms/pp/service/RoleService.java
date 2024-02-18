package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.model.entity.Privilege;
import com.cms.pp.cms.pp.model.entity.Role;
import com.cms.pp.cms.pp.repository.RoleRepository;
import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
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
        if (name.isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3021.getValue());
        if (privileges.isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3022.getValue());
        Role role = new Role()
                .setName(name)
                .setPrivileges(privileges);
        roleRepository.save(role);
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
    }

    public Object editRole(Long id, List<Privilege> privileges) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3020.getValue());
        role.setPrivileges(privileges);
        roleRepository.save(role);
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
    }

    public Object removeRole(Long id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3020.getValue());
        }
        roleRepository.deleteById(id);
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
    }
}
