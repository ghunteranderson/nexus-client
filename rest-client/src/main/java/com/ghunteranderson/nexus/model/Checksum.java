package com.ghunteranderson.nexus.model;

import lombok.Data;

@Data
public class Checksum {
	private String sha1;
	private String md5;
	private String sha512;
	private String sha256;
}
