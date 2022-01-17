package com.cms.pp.cms.pp.ConfigurationFlags;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
