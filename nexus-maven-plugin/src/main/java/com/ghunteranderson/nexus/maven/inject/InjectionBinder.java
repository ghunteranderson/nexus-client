package com.ghunteranderson.nexus.maven.inject;

import java.util.function.BiFunction;

public interface InjectionBinder {

	<T> void bind(Class<T> type, BiFunction<MojoContext, DependencyFactory, ? extends T> source);
	
}
