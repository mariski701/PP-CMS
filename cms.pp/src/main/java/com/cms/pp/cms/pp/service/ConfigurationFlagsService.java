package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.model.entity.ConfigurationFlags;
import com.cms.pp.cms.pp.repository.ConfigurationFlagsRepository;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
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
        return configurationFlagsRepository.findFirstByOrderByDateDESC();
    }

    @Override
    public Object updateCommentConfiguration(boolean commentsAvailable) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.findFirstByOrderByDateDESC();
        configurationFlags.setComments(commentsAvailable);
        configurationFlagsRepository.save(configurationFlags);
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
    }

    @Override
    public Object updateRegisterConfiguration(boolean registerAvailable) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.findFirstByOrderByDateDESC();
        configurationFlags.setRegister(registerAvailable);
        configurationFlagsRepository.save(configurationFlags);
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
    }

    @Override
    public Object updateLoginConfiguration(boolean loginAvailable) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.findFirstByOrderByDateDESC();
        configurationFlags.setLogin(loginAvailable);
        configurationFlagsRepository.save(configurationFlags);
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
    }
}
