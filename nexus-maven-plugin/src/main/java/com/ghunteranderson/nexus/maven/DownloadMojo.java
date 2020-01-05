package com.ghunteranderson.nexus.maven;

import java.io.IOException;
import java.util.Optional;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.ghunteranderson.jsemver.VersionRange;
import com.ghunteranderson.nexus.maven.inject.Dependency;
import com.ghunteranderson.nexus.model.Asset;
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
	protected void run() throws MojoExecutionException {
		Optional<Component> optional = searchService.findLatest(groupId, artifactId, VersionRange.from(version));
		if(!optional.isPresent()) {
			String error = String.format("Could not find component %s:%s matching version \"%s\"", groupId, artifactId, version);
			getLog().error(error);
			getLog().error("Artifact may not exist or authentication may be required.");
			throw new MojoExecutionException(error);
		}
		
		Component component = optional.get();
		getLog().info("Found component: " + FormatUtils.gavCoordinates(component));
		try {
			for(Asset asset : component.getAssets()) {
				getLog().info("Downloading " + FormatUtils.fileName(asset));
				downloadService.downloadComponent(component);
			}
		} catch(IOException ex) {
			throw new MojoExecutionException("IOException while downloading artifact.", ex);
		}
	}	


}
