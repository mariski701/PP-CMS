package com.cms.pp.cms.pp.ConfigurationFlags;

import com.cms.pp.cms.pp.CustomCorsConfigAnnotation;
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
    public Object changeCommentConfiguration(@PathVariable boolean commentsAvailable) {
        return configurationFlagsService.changeCommentConfiguration(commentsAvailable);
    }

    @PutMapping("register/{registerAvailable}")
    public Object changeRegisterConfiguration(@PathVariable boolean registerAvailable) {
        return configurationFlagsService.changeRegisterConfiguration(registerAvailable);
    }

    @PutMapping("login/{loginAvailable}")
    public Object changeLoginConfiguration(@PathVariable boolean loginAvailable) {
        return configurationFlagsService.changeLoginConfiguration(loginAvailable);
    }
}
