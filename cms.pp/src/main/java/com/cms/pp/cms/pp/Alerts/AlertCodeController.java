package com.cms.pp.cms.pp.Alerts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/english/alerts/")
@RestController
public class AlertCodeController {

    @Autowired
    private AlertCodeService alertCodeService;

    @GetMapping("findALl")
    public List<AlertCode> getAlertCodes() {
        return alertCodeService.getAlertCodes();
    }
}
