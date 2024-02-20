package com.cms.pp.cms.pp.configuration;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@CrossOrigin(origins = { "http://localhost:4200" }, allowCredentials = "true", maxAge = 3600, allowedHeaders = "*",
		methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH,
				RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.TRACE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomCorsConfigAnnotation {

	public String key() default "";

}
