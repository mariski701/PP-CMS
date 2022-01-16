package com.cms.pp.cms.pp.Priviliges;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/privileges")
@RestController
public class PrivilegeController {
    @Autowired
    private PrivilegeService privilegeService;

    @GetMapping("findAll")
    public List<Privilege> findAllPrivileges() {
        return privilegeService.findAllPrivileges();
    }

    @GetMapping("find/name/{name}")
    public Privilege findByName(@PathVariable String name) {
        return privilegeService.findByName(name);
    }

    @GetMapping("find/id/{id}")
    public Privilege findById(@PathVariable Long id) {
        return privilegeService.findById(id);
    }
}
