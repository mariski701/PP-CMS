package com.cms.pp.cms.pp.ConfigurationFlags;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationFlagsService {
    @Autowired
    private ConfigurationFlagsRepository configurationFlagsRepository;

    public ConfigurationFlags getConfig() {
        int id = 1;
        return configurationFlagsRepository.findById(1).orElse(null);
    }

    public int changeCommentConfiguration(boolean commentsAvailable) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        configurationFlags.setComments(commentsAvailable);
        configurationFlagsRepository.save(configurationFlags);
        return 2001; //success
    }

    public int changeRegisterConfiguration(boolean registerAvailable) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        configurationFlags.setRegister(registerAvailable);
        configurationFlagsRepository.save(configurationFlags);
        return 2001; //success
    }

    public int changeLoginConfiguration(boolean loginAvailable) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        configurationFlags.setLogin(loginAvailable);
        configurationFlagsRepository.save(configurationFlags);
        return 2001; //success
    }
}
