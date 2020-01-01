package com.ghunteranderson.nexus.model;

import lombok.Data;

@Data
public class Task {
	private String id;
	private String name;
	private String type;
	private String message;
	private String currentState;
	private String lastRunResult;
	private String nextRun;
	private String lastRun;
}
