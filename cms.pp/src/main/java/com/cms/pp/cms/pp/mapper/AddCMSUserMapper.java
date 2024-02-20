package com.cms.pp.cms.pp.mapper;

import com.cms.pp.cms.pp.model.dto.CMSUserDTO;
import com.cms.pp.cms.pp.model.entity.Role;
import com.cms.pp.cms.pp.model.entity.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Data
@Component
public class AddCMSUserMapper {

	private final PasswordEncoder passwordEncoder;

	public User mapCMSUserDTOToUser(CMSUserDTO cmsUserDTO, List<Role> roles) {
		return new User().setUserName(cmsUserDTO.getUserName())
			.setUserMail(cmsUserDTO.getUserMail())
			.setRoles(roles)
			.setUserPassword(passwordEncoder.encode(cmsUserDTO.getUserPassword()))
			.setEnabled(true);
	}

}
