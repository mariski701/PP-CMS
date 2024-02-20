package com.cms.pp.cms.pp.utils;

import com.cms.pp.cms.pp.model.ErrorProvidedDataHandler;
import lombok.Data;
import lombok.experimental.UtilityClass;

@UtilityClass
@Data
public class ErrorProvidedDataHandlerUtils {

	public static ErrorProvidedDataHandler getErrorProvidedDataHandler(String code) {
		return new ErrorProvidedDataHandler().setError(code);
	}

}
