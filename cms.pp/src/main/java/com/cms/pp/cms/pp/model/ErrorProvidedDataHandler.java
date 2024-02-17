package com.cms.pp.cms.pp.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorProvidedDataHandler {
    private String error;
}
