package com.cms.pp.cms.pp.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RoleDTO {

	private String name;

	private List<Map<String, String>> privileges;

}
