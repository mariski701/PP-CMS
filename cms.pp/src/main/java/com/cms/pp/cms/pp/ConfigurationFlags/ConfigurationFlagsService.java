package com.cms.pp.cms.pp.ConfigurationFlags;

import com.cms.pp.cms.pp.ErrorProvidedDataHandler;
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

    public Object changeCommentConfiguration(boolean commentsAvailable) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        configurationFlags.setComments(commentsAvailable);
        errorProvidedDataHandler.setError("2001");
        configurationFlagsRepository.save(configurationFlags);
        return errorProvidedDataHandler; //success
    }

    public Object changeRegisterConfiguration(boolean registerAvailable) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        configurationFlags.setRegister(registerAvailable);
        errorProvidedDataHandler.setError("2001");
        configurationFlagsRepository.save(configurationFlags);
        return errorProvidedDataHandler; //success
    }

    public Object changeLoginConfiguration(boolean loginAvailable) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        configurationFlags.setLogin(loginAvailable);
        errorProvidedDataHandler.setError("2001");
        configurationFlagsRepository.save(configurationFlags);
        return errorProvidedDataHandler; //success
    }
}
