package com.ghunteranderson.nexus.client;

import java.util.Optional;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ghunteranderson.nexus.model.Asset;
import com.ghunteranderson.nexus.model.ComponentQuery;

public class AssetClient {
	
	private final WebTarget assetTarget;
	private final WebTarget searchTarget;
	private final WebTarget downloadTarget;
	
	public AssetClient(NexusInstance instance) {
		assetTarget = instance.getWebTarget("/service/rest/v1/assets");
		searchTarget = instance.getWebTarget("/service/rest/v1/search/assets");
		downloadTarget = instance.getWebTarget("/repository");
	}
	
	public Stream<Asset> findAll(String repository){
		
		Function<String, PaginatedResponse<Asset>> source = token -> {
			return assetTarget
					.queryParam("repository", repository)
					.queryParam("continuationToken", token)
					.request()
					.accept(MediaType.APPLICATION_JSON)
					.get(new GenericType<PaginatedResponse<Asset>>() {});
		};
		
		return new PaginationIterator<>(source).stream();
	}
	
	public Optional<Asset> findOne(String id){
		try {
			return Optional.of(assetTarget.path(id)
					.request()
					.accept(MediaType.APPLICATION_JSON)
					.get(Asset.class));
		} catch(NotFoundException ex) {
			return Optional.empty();
		}
	}
	
	public Stream<Asset> findAll(ComponentQuery query){
		// We will build the web target once and reuse it across each pagination request
		WebTarget target = searchTarget;
		for(Entry<String, String> param : query.getQueryMap().entrySet()) {
			target = target.queryParam(param.getKey(), param.getValue());
		}
		
		// Adding this because of "effectively final" requirement
		WebTarget finalTarget = target; 
		
		Function<String, PaginatedResponse<Asset>> source = token -> {
			return finalTarget.queryParam("continuationToken", token)
					.request()
					.accept(MediaType.APPLICATION_JSON)
					.get(new PaginatedAssetType());
		};
		
		return new PaginationIterator<>(source).stream();
	}
	
	public void delete(String id) {
		assetTarget.path(id).request().delete();
	}
	
	public InputStream download(String repository, String path) {
		return downloadTarget
				.path(repository)
				.path(path)
				.request()
				.accept(MediaType.APPLICATION_OCTET_STREAM)
				.get(InputStream.class);
	}
	
	public static class PaginatedAssetType extends GenericType<PaginatedResponse<Asset>>{}
	
}
