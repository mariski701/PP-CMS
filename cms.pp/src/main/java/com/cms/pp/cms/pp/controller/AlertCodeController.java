package com.cms.pp.cms.pp.controller;

import com.cms.pp.cms.pp.model.entity.AlertCode;
import com.cms.pp.cms.pp.configuration.CustomCorsConfigAnnotation;
import com.cms.pp.cms.pp.service.AlertCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CustomCorsConfigAnnotation
@RequestMapping("/api/original/alerts/")
public class AlertCodeController {

    @Autowired
    private AlertCodeService alertCodeService;

    @GetMapping("findAll")
    public List<AlertCode> getAlertCodes() {
        return alertCodeService.getAlertCodes();
    }

    @PostMapping("add")
    public Object addAlertCode(@RequestBody Map<String, String> body) {
        return alertCodeService.addAlertCode(body.get("alertCode"), body.get("alertName"));
    }

    @DeleteMapping("remove/{id}")
    public Object removeAlertCode(@PathVariable int id) {
        return alertCodeService.removeAlertCode(id);
    }

    @PutMapping("edit")
    public Object editAlertCode(@RequestBody Map<String,String> body) {
        return alertCodeService.editAlertCode(Integer.parseInt(body.get("id")), body.get("alertCode"), body.get("alertName"));
    }

    @GetMapping("find/{id}")
    public AlertCode findById(@PathVariable int id) {
        return alertCodeService.findById(id);
    }
}
