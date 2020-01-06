package com.ghunteranderson.nexus.model;

import lombok.Data;

@Data
public class Asset {

	private String downloadUrl;
	private String path;
	private String id;
	private String repository;
	private String format;
	private Checksum checksum;
	
}
