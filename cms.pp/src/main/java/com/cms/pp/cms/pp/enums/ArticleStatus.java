package com.cms.pp.cms.pp.enums;

import lombok.Getter;

@Getter
public enum ArticleStatus {

	PUBLISHED("PUBLISHED"), UNPUBLSHED("UNPUBLISHED");

	public final String status;

	ArticleStatus(String status) {
		this.status = status;
	}

}
