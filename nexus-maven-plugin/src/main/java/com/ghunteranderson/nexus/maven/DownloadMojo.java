package com.ghunteranderson.nexus.maven;

import java.util.Optional;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.ghunteranderson.jsemver.VersionRange;
import com.ghunteranderson.nexus.maven.inject.Dependency;
import com.ghunteranderson.nexus.model.Component;
import com.ghunteranderson.nexus.service.DownloadService;
import com.ghunteranderson.nexus.service.SearchService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Mojo(name="download",
		requiresProject = false)
public class DownloadMojo extends AbstractNexusMojo {
	
	@Parameter(defaultValue="${groupId}")
	private String groupId;
	
	@Parameter(defaultValue="${artifactId}")
	private String artifactId;

	@Parameter(defaultValue="${version}")
	private String version;
	
	@Dependency private DownloadService downloadService;
	@Dependency private SearchService searchService;
	
	@Override
	protected void run() {
		Optional<Component> component = searchService.findLatest(groupId, artifactId, VersionRange.from(version));
		if(component.isPresent())
			getLog().info("Found component: " + component);
		else
			getLog().error(String.format("Could not find component %s:%s matching version \"%s\"", groupId, artifactId, version));
	}	


}
