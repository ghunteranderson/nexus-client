package com.ghunteranderson.nexus.model;

import java.util.Map;

import lombok.Data;

@Data
public class Repository {
	private String name;
	private String format;
	private String type;
	private String url;
	private Map<String, Map> attributes;
}
