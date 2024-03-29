package com.cms.pp.cms.pp.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Calendar;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "comments")
public class Comment {

	@Id
	@SequenceGenerator(name = "MY_COMMENT_SEQ", sequenceName = "MY_COMMENT_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_COMMENT_SEQ")
	@Column(unique = true, length = 128, updatable = false, nullable = false)
	private long id;

	@OneToOne
	private User user;

	@Column(name = "comment_content")
	private String content;

	@Column(name = "comment_date")
	private java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());

	@JsonIgnore
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "article_content_id")
	private ArticleContent articleContent;

	@JsonIgnore
	@Version
	private Long version;

}
