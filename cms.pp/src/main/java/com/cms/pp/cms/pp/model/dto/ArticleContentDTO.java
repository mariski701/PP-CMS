package com.cms.pp.cms.pp.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ArticleContentDTO {

	private Integer id;

	private String title;

	private String language;

	private Collection<Map<String, String>> tags;

	private String content;

	private String image;

}
