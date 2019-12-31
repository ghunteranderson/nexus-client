package com.ghunteranderson.nexus.client;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.ghunteranderson.nexus.model.Component;

public class ComponentClient {

	private final WebTarget client;
	
	public ComponentClient(NexusInstance instance) {
		client = instance.getWebTarget("/service/rest/v1/components");
	}
	
	public Stream<Component> findAll(String repository){
		Function<String, PaginatedResponse<Component>> source = token -> {
			return client
					.queryParam("repository", repository)
					.queryParam("continuationToken", token)
					.request()
					.accept(MediaType.APPLICATION_JSON)
					.get(new GenericType<PaginatedResponse<Component>>() {});
		};
		
		return new PaginationIterator<>(source).stream();
	}
	
	public Optional<Component> findOne(String id){
		try {
			return Optional.of(client.path(id).request().get(Component.class));
		} catch(NotFoundException ex) {
			return Optional.empty();
		}
	}
	
	
	
	public void delete(String id) {
		client.path(id).request().delete();
	}
}
