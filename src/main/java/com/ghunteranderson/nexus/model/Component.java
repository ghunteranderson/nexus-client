package com.ghunteranderson.nexus.model;

import java.util.List;

import lombok.Data;

@Data
public class Component {

	private String id;
	private String repository;
	private String format;
	private String group;
	private String name;
	private String version;
	private List<Asset> assets;
}
