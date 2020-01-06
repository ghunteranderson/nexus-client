package com.ghunteranderson.nexus.client;

import java.util.Map;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.ghunteranderson.nexus.model.StatusDetails;

public class StatusClient {

	private final WebTarget readableTarget;
	private final WebTarget writableTarget;
	private final WebTarget detailsTarget;
	
	public StatusClient(NexusInstance instance) {
		readableTarget = instance.getWebTarget("/service/rest/v1/status");
		writableTarget = instance.getWebTarget("/service/rest/v1/status/writable");
		detailsTarget = instance.getWebTarget("/service/rest/v1/status/check");
	}
	
	/**
	 * @return true if server can respond to read requests
	 */
	public boolean isReadable() {
		try {
			return readableTarget.request().get().getStatus() == 200;
		} catch(Exception ex) {
			return false;
		}
	}
	
	/**
	 * @return A map of system status checks and their results
	 */
	public Map<String, StatusDetails> details() {
		return detailsTarget
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get(new StatusDetailsMapType());
	}
	
	/**
	 * /@return True if server can respond to write requests
	 */
	public boolean isWritable() {
		try {
			return writableTarget.request().get().getStatus() == 200;
		} catch(Exception e) {
			return false;
		}
	}
	
	private static class StatusDetailsMapType extends GenericType<Map<String, StatusDetails>>{}
	
}
