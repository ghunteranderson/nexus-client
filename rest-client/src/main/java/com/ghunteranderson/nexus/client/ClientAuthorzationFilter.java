package com.ghunteranderson.nexus.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

class ClientAuthorzationFilter implements ClientRequestFilter{

	private final String header;
	
	public ClientAuthorzationFilter(String username, String password) {
		byte[] rawHeader = (username + ":" + password).getBytes(StandardCharsets.UTF_8);
		header = "Basic " + Base64.getEncoder().encodeToString(rawHeader);
	}
	
	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		requestContext.getHeaders().add("Authorization", header);
	}
	
}
