package com.cms.pp.cms.pp.ConfigurationFlags;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/cms/admin/config")
@RestController
public class ConfigurationFlagsController {
    @Autowired
    private ConfigurationFlagsService configurationFlagsService;

    @GetMapping("")
    public ConfigurationFlags getConfig() {
        return configurationFlagsService.getConfig();
    }

    @PutMapping("comments/{commentsAvailable}")
    public int changeCommentConfiguration(@PathVariable boolean commentsAvailable) {
        return configurationFlagsService.changeCommentConfiguration(commentsAvailable);
    }

    @PutMapping("register/{registerAvailable}")
    public int changeRegisterConfiguration(@PathVariable boolean registerAvailable) {
        return configurationFlagsService.changeRegisterConfiguration(registerAvailable);
    }

    @PutMapping("login/{loginAvailable}")
    public int changeLoginConfiguration(@PathVariable boolean loginAvailable) {
        return configurationFlagsService.changeLoginConfiguration(loginAvailable);
    }
}
