package com.cms.pp.cms.pp.utils;

import lombok.Data;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@UtilityClass
public class PrincipalUtils {
    public static String getPrincipalUserName(Object principal) {
        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        }
        return principal.toString();
    }
}
