package com.cms.pp.cms.pp.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CustomTopCommentersClass {
    private String userName;
    private long commentCount;

    public CustomTopCommentersClass(String userName, long commentCount) {
        this.userName = userName;
        this.commentCount = commentCount;
    }


}
