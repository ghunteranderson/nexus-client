package com.ghunteranderson.nexus.client;

import java.util.List;
import java.util.stream.Stream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.ghunteranderson.nexus.model.Repository;

public class RepositoryClient {

	private final WebTarget repositoryTarget;
	
	public RepositoryClient(NexusInstance instance) {
		repositoryTarget = instance.getWebTarget("/service/rest/v1/repositories");
	}
	
	public Stream<Repository> findAll(){
		return repositoryTarget.request()
			.accept(MediaType.APPLICATION_JSON)
			.get(new GenericType<List<Repository>>(){})
			.stream();
	}
	
	
}
