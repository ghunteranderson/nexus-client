package com.ghunteranderson.nexus.client;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PaginationIterator <E> implements Iterator<E>{

	private Iterator<E> buffer;
	private String token;
	private final Function<String, PaginatedResponse<E>> source;
	
	public PaginationIterator(Function<String, PaginatedResponse<E>> source) {
		this.source = source;
	}
	
	
	@Override
	public boolean hasNext() {
		if(buffer == null || !buffer.hasNext())
			loadNext();
		return buffer.hasNext();
	}
	@Override
	public E next() {
		if(hasNext())
			return buffer.next();
		else
			throw new NoSuchElementException();
	}
	
	private void loadNext() {
		if(buffer != null && token == null)
			return;
		
		PaginatedResponse<E> next = source.apply(token);
		token = next.getContinuationToken();
		buffer = next.getItems().iterator();
	}
	
	public Stream<E> stream(){
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this, 0), false);
	}
	
}
