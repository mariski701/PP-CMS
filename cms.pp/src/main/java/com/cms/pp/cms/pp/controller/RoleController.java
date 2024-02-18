package com.cms.pp.cms.pp.controller;

import com.cms.pp.cms.pp.configuration.CustomCorsConfigAnnotation;
import com.cms.pp.cms.pp.model.entity.Privilege;
import com.cms.pp.cms.pp.model.entity.Role;
import com.cms.pp.cms.pp.model.dto.RoleDTO;
import com.cms.pp.cms.pp.repository.PrivilegeRepository;
import com.cms.pp.cms.pp.service.IRoleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        List<String> privilegeName = new ArrayList<>();
        List<Privilege> privileges = new ArrayList<>();

        for (int i = 0; i < roleDTO.getPrivileges().size(); i++) {
            privilegeName.add(roleDTO.getPrivileges().get(i).get("privilegeName"));
        }

        System.out.println(privilegeName);
        for (String s : privilegeName) {
            privileges.add(privilegeRepository.findByName(s));
        }

        return roleService.createRole(roleDTO.getName(), privileges);
    }

    @PutMapping("edit/{id}")
    public Object editRole(@PathVariable Long id, @RequestBody List<Map<String, String>> body) {
        List<String> privilegeName = new ArrayList<>();
        List<Privilege> privileges = new ArrayList<>();
        for (Map<String, String> stringStringMap : body) {
            privilegeName.add(stringStringMap.get("privilegeName"));
        }
        System.out.println(privilegeName);
        for (String s : privilegeName) {
            privileges.add(privilegeRepository.findByName(s));
        }
        return roleService.editRole(id, privileges);
    }

    @DeleteMapping("remove/{id}")
    public Object removeRole(@PathVariable Long id) {
        return roleService.removeRole(id);
    }
}
