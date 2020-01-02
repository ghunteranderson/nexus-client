package com.ghunteranderson.nexus.client;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.ghunteranderson.nexus.model.Task;

public class TaskClient {

	private final WebTarget taskTarget;
	
	public TaskClient(NexusInstance instance) {
		taskTarget = instance.getWebTarget("/service/rest/v1/tasks");
	}
	
	public Stream<Task> findAll(){
		return findAll(null);
	}
	
	public Stream<Task> findAll(String type){
		Function<String, PaginatedResponse<Task>> source = token -> {
			return taskTarget
				.queryParam("continuationToken", token)
				.queryParam("type", type)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<PaginatedResponse<Task>>() {});
		};
		
		return new PaginationIterator<>(source).stream();
	}
	
	public Optional<Task> findOne(String id){
		try {
			return Optional.of(taskTarget
					.path(id)
					.request()
					.accept(MediaType.APPLICATION_JSON)
					.get(Task.class));
		} catch(NotFoundException ex) {
			return Optional.empty();
		}
	}
	
	public boolean runTask(String id) {
		return taskTarget.path(id).path("run")
				.request()
				.post(null)
				.getStatus() == 204;
	}
	
	public boolean stopTask(String id) {
		return taskTarget.path(id).path("stop")
				.request()
				.post(null)
				.getStatus() == 204;
	}
}
