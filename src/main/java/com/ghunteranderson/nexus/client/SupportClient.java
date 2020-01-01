package com.ghunteranderson.nexus.client;

import java.io.InputStream;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ghunteranderson.nexus.model.StatusDetails;
import com.ghunteranderson.nexus.model.SupportZipRequest;

public class SupportClient {

	private final WebTarget readableTarget;
	private final WebTarget writableTarget;
	private final WebTarget detailsTarget;
	private final WebTarget supportDownloadTarget;
	
	public SupportClient(NexusInstance instance) {
		readableTarget = instance.getWebTarget("/service/rest/v1/status");
		writableTarget = instance.getWebTarget("/service/rest/v1/status/writable");
		detailsTarget = instance.getWebTarget("/service/rest/v1/status/check");
		supportDownloadTarget = instance.getWebTarget("/service/rest/v1/support/supportzip");
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
	
	public InputStream downloadSupportZip(SupportZipRequest request) {
		Response response = supportDownloadTarget
				.request()
				.accept(MediaType.APPLICATION_OCTET_STREAM)
				.post(Entity.json(request));
		
		if(response.getStatus() == 200)
			return response.readEntity(InputStream.class);
		
		else
			return null; //TODO: decide how to handle errors here
	}
	
	private static class StatusDetailsMapType extends GenericType<Map<String, StatusDetails>>{}
	
}
