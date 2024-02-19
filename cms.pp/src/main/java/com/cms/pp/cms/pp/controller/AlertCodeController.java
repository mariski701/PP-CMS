package com.cms.pp.cms.pp.controller;

import com.cms.pp.cms.pp.configuration.CustomCorsConfigAnnotation;
import com.cms.pp.cms.pp.model.entity.AlertCode;
import com.cms.pp.cms.pp.service.IAlertCodeService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@RestController
@CustomCorsConfigAnnotation
@RequestMapping("/api/original/alerts/")
public class AlertCodeController {
    private final IAlertCodeService IAlertCodeService;

    @GetMapping("findAll")
    public List<AlertCode> getAlertCodes() {
        return IAlertCodeService.getAlertCodes();
    }

    @PostMapping("add")
    public Object addAlertCode(@RequestBody Map<String, String> body) {
        return IAlertCodeService.addAlertCode(body.get("alertCode"), body.get("alertName"));
    }

    @DeleteMapping("remove/{id}")
    public Object removeAlertCode(@PathVariable int id) {
        return IAlertCodeService.removeAlertCode(id);
    }

    @PutMapping("edit")
    public Object editAlertCode(@RequestBody Map<String,String> body) {
        return IAlertCodeService.editAlertCode(Integer.parseInt(body.get("id")), body.get("alertCode"), body.get("alertName"));
    }

    @GetMapping("find/{id}")
    public AlertCode findById(@PathVariable int id) {
        return IAlertCodeService.findById(id);
    }
}
