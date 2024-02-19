package com.cms.pp.cms.pp.mapper;

import com.cms.pp.cms.pp.model.entity.Privilege;
import com.cms.pp.cms.pp.model.entity.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Data
public class CreateRoleMapper {
    public Role mapToRole(String name, List<Privilege> privileges) {
        return new Role()
                .setName(name)
                .setPrivileges(privileges);
    }
}
