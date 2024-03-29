package com.cms.pp.cms.pp.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "ConfigurationFlags")
public class ConfigurationFlags {

	@Id
	@SequenceGenerator(name = "MY_CONFIG_SEQ", sequenceName = "MY_CONFIG_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MY_CONFIG_SEQ")
	@Column(unique = true, length = 128, updatable = false, nullable = false)
	private int id;

	@Column(name = "Comments", nullable = false)
	private boolean comments = true;

	@Column(name = "Register", nullable = false)
	private boolean register = true;

	@Column(name = "Login", nullable = false)
	private boolean login = true;

}
