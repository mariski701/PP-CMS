package com.cms.pp.cms.pp.user;

import lombok.Getter;
import lombok.Setter;


public class UserDTO {
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String usermail;
    @Getter
    @Setter
    private int id;
    public UserDTO(int id, String username, String usermail) {
        this.id = id;
        this.username = username;
        this.usermail = usermail;
    }
}
