package com.cms.pp.cms.pp.mapper;

import com.cms.pp.cms.pp.model.dto.CMSUserDTO;
import com.cms.pp.cms.pp.model.entity.User;
import com.cms.pp.cms.pp.repository.RoleRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@RequiredArgsConstructor
@Data
@Component
public class AddCMSUserMapper {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User mapCMSUserDTOToUser(CMSUserDTO cmsUserDTO) {
        return new User()
                .setUserName(cmsUserDTO.getUserName())
                .setUserMail(cmsUserDTO.getUserMail())
                .setRoles(Collections.singletonList(roleRepository.findByName(cmsUserDTO.getRole())))
                .setUserPassword(passwordEncoder.encode(cmsUserDTO.getUserPassword()))
                .setEnabled(true);
    }
}
