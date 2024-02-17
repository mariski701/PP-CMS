package com.cms.pp.cms.pp.controller;

import com.cms.pp.cms.pp.model.entity.ConfigurationFlags;
import com.cms.pp.cms.pp.configuration.CustomCorsConfigAnnotation;
import com.cms.pp.cms.pp.service.ConfigurationFlagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CustomCorsConfigAnnotation
@RequestMapping("/api/cms/admin/config")
public class ConfigurationFlagsController {
    @Autowired
    private ConfigurationFlagsService configurationFlagsService;

    @GetMapping("")
    public ConfigurationFlags getConfig() {
        return configurationFlagsService.getConfig();
    }

    @PutMapping("comments/{commentsAvailable}")
    public Object updateCommentConfiguration(@PathVariable boolean commentsAvailable) {
        return configurationFlagsService.updateCommentConfiguration(commentsAvailable);
    }

    @PutMapping("register/{registerAvailable}")
    public Object updateRegisterConfiguration(@PathVariable boolean registerAvailable) {
        return configurationFlagsService.updateRegisterConfiguration(registerAvailable);
    }

    @PutMapping("login/{loginAvailable}")
    public Object updateLoginConfiguration(@PathVariable boolean loginAvailable) {
        return configurationFlagsService.updateLoginConfiguration(loginAvailable);
    }
}
