package com.ghunteranderson.nexus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent=true)
public class SupportZipRequest {
	
	@JsonProperty("systemInformation")
	private boolean includeSystemInformation;
	
	@JsonProperty("threadDump")
	private boolean includeThreadDump;
	
	@JsonProperty("metrics")
	private boolean includeMetrics;
	
	@JsonProperty("configuration")
	private boolean includeConfiguration;
	
	@JsonProperty("security")
	private boolean includeSecurity;
	
	@JsonProperty("log")
	private boolean includeLogs;
	
	@JsonProperty("taskLog")
	private boolean includeTaskLogs;
	
	@JsonProperty("auditLog")
	private boolean includeAuditLogs;
	
	@JsonProperty("jmx")
	private boolean includeJmx;
	
	@JsonProperty
	private boolean limitFileSizes;
	
	@JsonProperty
	private boolean limitZipSize;

	public SupportZipRequest includeAll() {
		includeSystemInformation = true;
		includeThreadDump = true;
		includeMetrics = true;
		includeConfiguration = true;
		includeSecurity = true;
		includeLogs = true;
		includeTaskLogs = true;
		includeAuditLogs = true;
		includeJmx = true;
		return this;
	}
}
