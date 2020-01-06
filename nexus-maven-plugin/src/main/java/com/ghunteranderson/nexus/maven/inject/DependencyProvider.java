package com.ghunteranderson.nexus.maven.inject;

public interface DependencyProvider {
	<T> T getInstance(Class<T> type, MojoContext context);
}
