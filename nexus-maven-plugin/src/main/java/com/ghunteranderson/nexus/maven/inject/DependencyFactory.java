package com.ghunteranderson.nexus.maven.inject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DependencyFactory implements InjectionBinder, DependencyProvider {
	
	private Map<Class<?>, ContextAwareLazyLoader<?>> registry = new HashMap<>();
	
	public <T> void bind(Class<T> type, BiFunction<MojoContext, DependencyFactory, ? extends T> source) {
		Function<MojoContext, ? extends T> supplier = (c) -> source.apply(c, this);
		registry.put(type, new ContextAwareLazyLoader<>(supplier));
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getInstance(Class<T> type, MojoContext context) {
		Object obj = registry.get(type).getValue(context);
		if(type.isAssignableFrom(obj.getClass()))
			return (T) obj;
		else {
			String error = "Unexpected error while creating dependency of type " + type.getName() + ". "
				+ "The supplier function provided an object of incompatible type " + obj.getClass().getName() + ".";
			throw new IllegalStateException(error);
		}
	}

}
