package com.cms.pp.cms.pp.Alerts;

import com.cms.pp.cms.pp.CustomCorsConfigAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CustomCorsConfigAnnotation
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
