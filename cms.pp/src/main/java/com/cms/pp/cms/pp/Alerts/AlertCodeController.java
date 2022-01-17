package com.cms.pp.cms.pp.Alerts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(
        origins = {"http://localhost:4200"},
        allowCredentials = "true",
        maxAge = 3600,
        allowedHeaders = "*",
        methods = {
                RequestMethod.GET,RequestMethod.POST,
                RequestMethod.DELETE, RequestMethod.PUT,
                RequestMethod.PATCH, RequestMethod.OPTIONS,
                RequestMethod.HEAD, RequestMethod.TRACE
        }
)
@RequestMapping("/api/english/alerts/")
public class AlertCodeController {

    @Autowired
    private AlertCodeService alertCodeService;

    @GetMapping("findALl")
    public List<AlertCode> getAlertCodes() {
        return alertCodeService.getAlertCodes();
    }
}
