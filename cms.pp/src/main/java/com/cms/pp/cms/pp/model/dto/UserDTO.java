package com.cms.pp.cms.pp.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Data
public class UserDTO {
    private String username;
    private String usermail;
    private int id;
    public UserDTO(int id, String username, String usermail) {
        this.id = id;
        this.username = username;
        this.usermail = usermail;
    }
}
