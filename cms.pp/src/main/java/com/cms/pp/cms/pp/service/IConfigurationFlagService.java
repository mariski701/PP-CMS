package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.model.entity.ConfigurationFlags;

public interface IConfigurationFlagService {

	ConfigurationFlags getConfig();

	Object updateCommentConfiguration(boolean commentsAvailable);

	Object updateRegisterConfiguration(boolean registerAvailable);

	Object updateLoginConfiguration(boolean loginAvailable);

}
