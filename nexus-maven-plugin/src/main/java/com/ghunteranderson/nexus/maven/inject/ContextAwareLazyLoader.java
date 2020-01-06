package com.ghunteranderson.nexus.maven.inject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ContextAwareLazyLoader<T> {

	private Map<String, T> values = new HashMap<>();
	private Function<MojoContext, T> source;
	
	public ContextAwareLazyLoader(Function<MojoContext, T> source) {
		this.source = source;
	}
	
	public T getValue(MojoContext context) {
		String contextId = context.toString();
		if(!values.containsKey(contextId))
			values.put(contextId, source.apply(context));
		return values.get(contextId);
	}
	
}
