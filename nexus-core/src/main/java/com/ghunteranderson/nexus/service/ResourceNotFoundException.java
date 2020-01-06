package com.ghunteranderson.nexus.service;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -6073893227668466337L;

	public ResourceNotFoundException(String message) {
		super(message);
	}
	
	public ResourceNotFoundException(Throwable t) {
		super(t);
	}
	
	public ResourceNotFoundException(String message, Throwable t) {
		super(message, t);
	}
}
