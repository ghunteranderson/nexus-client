package com.ghunteranderson.nexus.model;

import java.util.Date;

import lombok.Data;

@Data
public class StatusDetails {
	private boolean healthy;
	private String message;
	private String error;
	private String details;
	private Date time;
	private int duration;
	private String timestamp;
}
