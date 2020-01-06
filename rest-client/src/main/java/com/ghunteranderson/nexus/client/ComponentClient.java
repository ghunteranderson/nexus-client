package com.ghunteranderson.nexus.client;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.ghunteranderson.nexus.model.Component;
import com.ghunteranderson.nexus.model.ComponentQuery;

public class ComponentClient {

	private final WebTarget componentTarget;
	private final WebTarget searchTarget;
	
	public ComponentClient(NexusInstance instance) {
		componentTarget = instance.getWebTarget("/service/rest/v1/components");
		searchTarget = instance.getWebTarget("/service/rest/v1/search");
	}
	
	public Stream<Component> findAll(String repository){
		Function<String, PaginatedResponse<Component>> source = token -> {
			return componentTarget
					.queryParam("repository", repository)
					.queryParam("continuationToken", token)
					.request()
					.accept(MediaType.APPLICATION_JSON)
					.get(new PaginatedComponentType());
		};
		
		return new PaginationIterator<>(source).stream();
	}
	
	public Optional<Component> findOne(String id){
		try {
			return Optional.of(componentTarget.path(id).request().get(Component.class));
		} catch(NotFoundException ex) {
			return Optional.empty();
		}
	}
	
	public Stream<Component> findAll(ComponentQuery query){
		// We will build the web target once and reuse it across each pagination request
		WebTarget target = searchTarget;
		for(Entry<String, String> param : query.getQueryMap().entrySet()) {
			target = target.queryParam(param.getKey(), param.getValue());
		}
		
		// Adding this because of "effectively final" requirement
		WebTarget finalTarget = target; 
		
		Function<String, PaginatedResponse<Component>> source = token -> {
			return finalTarget.queryParam("continuationToken", token)
					.request()
					.accept(MediaType.APPLICATION_JSON)
					.get(new PaginatedComponentType());
		};
		
		return new PaginationIterator<>(source).stream();
	}
	
	public void delete(String id) {
		componentTarget.path(id).request().delete();
	}
	
	private static class PaginatedComponentType extends GenericType<PaginatedResponse<Component>>{}
}
