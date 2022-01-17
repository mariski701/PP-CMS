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
@RequestMapping("/api/alerts/")
public class AlertTranslationController {
    @Autowired
    private AlertTranslationService alertTranslationService;

    @GetMapping("{language}")
    public List<AlertTranslationDTO> findByLanguage(@PathVariable String language) {
        return alertTranslationService.findByLanguage(language);
    }

    @PostMapping("add")
    public Object addAlertTranslation(@RequestBody AlertTranslationDTO alertTranslationDTO) {
        return alertTranslationService.addAlertTranslation(alertTranslationDTO);
    }
}
