package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.model.entity.ConfigurationFlags;
import com.cms.pp.cms.pp.repository.ConfigurationFlagsRepository;
import com.cms.pp.cms.pp.model.ErrorProvidedDataHandler;
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

    public Object updateCommentConfiguration(boolean commentsAvailable) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        configurationFlags.setComments(commentsAvailable);
        configurationFlagsRepository.save(configurationFlags);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        return errorProvidedDataHandler;
    }

    public Object updateRegisterConfiguration(boolean registerAvailable) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        configurationFlags.setRegister(registerAvailable);
        configurationFlagsRepository.save(configurationFlags);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        return errorProvidedDataHandler;
    }

    public Object updateLoginConfiguration(boolean loginAvailable) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        configurationFlags.setLogin(loginAvailable);
        configurationFlagsRepository.save(configurationFlags);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        return errorProvidedDataHandler;
    }
}
