package com.ghunteranderson.nexus.client;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.ghunteranderson.nexus.model.Asset;

public class AssetClient {
	
	private final WebTarget client;
	
	public AssetClient(NexusInstance instance) {
		client = instance.getWebTarget("/service/rest/v1/assets");
	}
	
	public Stream<Asset> findAll(String repository){
		
		Function<String, PaginatedResponse<Asset>> source = token -> {
			return client
					.queryParam("repository", repository)
					.queryParam("continuationToken", token)
					.request()
					.accept(MediaType.APPLICATION_JSON)
					.get(new GenericType<PaginatedResponse<Asset>>() {});
		};
		
		return new PaginationIterator<>(source).stream();
	}
	
	public Optional<Asset> find(String id){
		try {
			return Optional.of(client.path(id)
					.request()
					.accept(MediaType.APPLICATION_JSON)
					.get(Asset.class));
		} catch(NotFoundException ex) {
			return Optional.empty();
		}
	}
	
	public void delete(String id) {
		client.path(id).request().delete();
	}
	
}
