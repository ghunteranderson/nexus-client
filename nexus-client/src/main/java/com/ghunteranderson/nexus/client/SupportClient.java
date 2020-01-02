package com.ghunteranderson.nexus.client;

import java.io.InputStream;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ghunteranderson.nexus.model.SupportZipRequest;

public class SupportClient {
	
	private final WebTarget supportDownloadTarget;
	
	public SupportClient(NexusInstance instance) {
		supportDownloadTarget = instance.getWebTarget("/service/rest/v1/support/supportzip");
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
}
