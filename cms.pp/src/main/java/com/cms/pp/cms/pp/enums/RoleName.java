package com.cms.pp.cms.pp.enums;

import lombok.Getter;

@Getter
public enum RoleName {

	ROLE_GUEST("ROLE_GUEST"), ROLE_USERWITHOUTCOMMENTS("ROLE_USERWITHOUTCOMMENTS"), ROLE_USER("ROLE_USER"),
	ROLE_EDITOR("ROLE_EDITOR"), ROLE_MODERATOR("ROLE_MODERATOR"), ROLE_ADMIN("ROLE_ADMIN");

	public final String roleName;

	private RoleName(String roleName) {
		this.roleName = roleName;
	}

}
