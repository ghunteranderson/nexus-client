package com.ghunteranderson.nexus.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
public class NexusInstance {

	private final WebTarget client;
	
	WebTarget getWebTarget(String path) {
		return client.path(path);
	}
	
	
	public static class Builder {
		private ClientConfig clientConfig;
		private String url;
		private String username;
		private String password;
		
		public Builder(String url) {
			this.url = url;
		}
		
		public Builder client(ClientConfig clientConfig) {
			this.clientConfig = clientConfig;
			return this;
		}
		
		public Builder authenticate(String username, String password) {
			this.username = username;
			this.password = password;
			return this;
		}
		
		public NexusInstance build() {
			if(clientConfig == null)
				clientConfig = new ClientConfig();
			
			Client client = ClientBuilder.newClient(clientConfig);
			if(username != null)
				client.register(new ClientAuthorzationFilter(username, password));
			
			return new NexusInstance(client.target(url));
		}
	}
}
