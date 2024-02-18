package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.model.entity.ConfigurationFlags;
import com.cms.pp.cms.pp.repository.ConfigurationFlagsRepository;
import com.cms.pp.cms.pp.model.ErrorProvidedDataHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@RequiredArgsConstructor
@Service("ConfigurationFlagsService")
public class ConfigurationFlagsService implements IConfigurationFlagService {
    private final ConfigurationFlagsRepository configurationFlagsRepository;

    @Override
    public ConfigurationFlags getConfig() {
        int id = 1;
        return configurationFlagsRepository.findById(1).orElse(null);
    }

    @Override
    public Object updateCommentConfiguration(boolean commentsAvailable) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        configurationFlags.setComments(commentsAvailable);
        configurationFlagsRepository.save(configurationFlags);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        return errorProvidedDataHandler;
    }

    @Override
    public Object updateRegisterConfiguration(boolean registerAvailable) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        configurationFlags.setRegister(registerAvailable);
        configurationFlagsRepository.save(configurationFlags);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        return errorProvidedDataHandler;
    }

    @Override
    public Object updateLoginConfiguration(boolean loginAvailable) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        configurationFlags.setLogin(loginAvailable);
        configurationFlagsRepository.save(configurationFlags);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        return errorProvidedDataHandler;
    }
}
