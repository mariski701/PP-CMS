package com.cms.pp.cms.pp.Priviliges;

import com.cms.pp.cms.pp.CustomCorsConfigAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CustomCorsConfigAnnotation
@RequestMapping("/api/privileges")
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
