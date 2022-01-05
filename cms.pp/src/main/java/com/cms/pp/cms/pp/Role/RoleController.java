package com.cms.pp.cms.pp.Role;

import com.cms.pp.cms.pp.Priviliges.Privilege;
import com.cms.pp.cms.pp.Priviliges.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/cms/role")
@RestController
public class RoleController {
    @Autowired
    RoleService roleService;
    @Autowired
    PrivilegeRepository privilegeRepository;

    @GetMapping("all")
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    @GetMapping("{id}")
    public Role getRole(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    @PostMapping("create")
    public String createRole(@RequestBody RoleDTO roleDTO) {
        List<String> privilegeName = new ArrayList<>();
        List<Privilege> privileges = new ArrayList<>();

        for (int i = 0; i < roleDTO.getPrivileges().size(); i++) {
            privilegeName.add(roleDTO.getPrivileges().get(i).get("privilegeName"));
        }

        System.out.println(privilegeName);
        for (int i = 0; i < privilegeName.size(); i++) {
            privileges.add(privilegeRepository.findByName(privilegeName.get(i)));
        }

        return roleService.createRole(roleDTO.getName(), privileges);
    }

    @PutMapping("edit/{id}")
    public String editRole(@PathVariable Long id, @RequestBody List<Map<String, String>> body) {
        List<String> privilegeName = new ArrayList<>();
        List<Privilege> privileges = new ArrayList<>();
        for (int i = 0; i < body.size(); i++) {
            privilegeName.add(body.get(i).get("privilegeName"));
        }
        System.out.println(privilegeName);
        for (int i = 0; i < privilegeName.size(); i++) {
            privileges.add(privilegeRepository.findByName(privilegeName.get(i)));
        }
        return roleService.editRole(id, privileges);
    }

    @DeleteMapping("remove/{id}")
    public String removeRole(@PathVariable Long id) {
        return roleService.removeRole(id);
    }
}