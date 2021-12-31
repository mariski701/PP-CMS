package com.cms.pp.cms.pp.user;

import lombok.Data;

import java.util.Collection;
import java.util.Map;

@Data
public class CMSUserDTO {
    private String userName;
    private String userPassword;
    private String role;
    private String userMail;

}
