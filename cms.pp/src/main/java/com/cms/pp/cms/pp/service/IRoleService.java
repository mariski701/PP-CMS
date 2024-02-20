package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.model.entity.Privilege;
import com.cms.pp.cms.pp.model.entity.Role;

import java.util.List;

public interface IRoleService {

	List<Role> getRoles();

	Role getRole(Long id);

	Object createRole(String name, List<Privilege> privileges);

	Object editRole(Long id, List<Privilege> privileges);

	Object removeRole(Long id);

}
