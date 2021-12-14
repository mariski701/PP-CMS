package com.cms.pp.cms.pp.Article;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import lombok.Data;
import org.springframework.boot.jackson.JsonComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class ArticleContentDTO {
    private Integer id;
    private String title;
    private String language;
    private Collection<Map<String, String>> tags;
    private String content;
}
