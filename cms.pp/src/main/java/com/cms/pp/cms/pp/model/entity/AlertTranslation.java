package com.cms.pp.cms.pp.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "Alert_translation")
public class AlertTranslation {

	@Id
	@SequenceGenerator(name = "MY_ALERTTRANSLATION_SEQ", sequenceName = "MY_ALERTTRANSLATION_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_ALERTTRANSLATION_SEQ")
	@Column(unique = true, length = 128, updatable = false, nullable = false)
	private int id;

	@Column(name = "error_translation")
	private String errorTranslation;

	@ManyToOne
	@JoinColumn(name = "language_id")
	private Language language;

	@ManyToOne
	@JoinColumn(name = "alertCode_id")
	private AlertCode alertCode;

	@JsonIgnore
	@Version
	private Long version;

}
