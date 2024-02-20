package com.cms.pp.cms.pp.controller;

import com.cms.pp.cms.pp.configuration.CustomCorsConfigAnnotation;
import com.cms.pp.cms.pp.model.dto.RoleDTO;
import com.cms.pp.cms.pp.model.entity.Privilege;
import com.cms.pp.cms.pp.model.entity.Role;
import com.cms.pp.cms.pp.repository.PrivilegeRepository;
import com.cms.pp.cms.pp.service.IRoleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@RestController
@CustomCorsConfigAnnotation
@RequestMapping("/api/cms/role")
public class RoleController {

	private final IRoleService roleService;

	private final PrivilegeRepository privilegeRepository;

	@GetMapping("all")
	public List<Role> getRoles() {
		return roleService.getRoles();
	}

	@GetMapping("find/{id}")
	public Role getRole(@PathVariable Long id) {
		return roleService.getRole(id);
	}

	@PostMapping("create")
	public Object createRole(@RequestBody RoleDTO roleDTO) {
		List<Privilege> privileges = roleDTO.getPrivileges()
			.stream()
			.map(privilegeMap -> privilegeMap.get("privilegeName"))
			.map(privilegeRepository::findByName)
			.collect(Collectors.toList());
		return roleService.createRole(roleDTO.getName(), privileges);
	}

	@PutMapping("edit/{id}")
	public Object editRole(@PathVariable Long id, @RequestBody List<Map<String, String>> body) {
		List<Privilege> privileges = body.stream()
			.map(stringStringMap -> stringStringMap.get("privilegeName"))
			.map(privilegeRepository::findByName)
			.collect(Collectors.toList());
		return roleService.editRole(id, privileges);
	}

	@DeleteMapping("remove/{id}")
	public Object removeRole(@PathVariable Long id) {
		return roleService.removeRole(id);
	}

}
