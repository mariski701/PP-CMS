package com.cms.pp.cms.pp.Alerts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/alerts/")
@RestController
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
