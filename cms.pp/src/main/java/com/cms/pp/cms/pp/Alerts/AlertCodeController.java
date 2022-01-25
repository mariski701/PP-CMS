package com.cms.pp.cms.pp.Alerts;

import com.cms.pp.cms.pp.CustomCorsConfigAnnotation;
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
    public Object addCode(Map<String, String> body) {
        return alertCodeService.addCode(body.get("alertCode"), body.get("alertName"));
    }

    @DeleteMapping("remove/{id}")
    public Object removeCode(@PathVariable int id) {
        return alertCodeService.removeCode(id);
    }

    @PutMapping("edit")
    public Object editCode(@RequestBody Map<String,String> body) {
        return alertCodeService.editCode(Integer.parseInt(body.get("id")), body.get("alertCode"), body.get("alertName"));
    }
}
