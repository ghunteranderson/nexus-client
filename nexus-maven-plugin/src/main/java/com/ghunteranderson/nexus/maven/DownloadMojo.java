package com.ghunteranderson.nexus.maven;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
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
	
	@Parameter(defaultValue="${dir}")
	private String dir;
	
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
		
		String directory = StringUtils.isBlank(dir) ? "." : dir;
		try {
			for(Asset asset : component.getAssets()) {
				getLog().info("Downloading " + Paths.get(directory, FormatUtils.fileName(asset)).toString());
				downloadService.downloadComponent(component, directory);
			}
		} catch(IOException ex) {
			throw new MojoExecutionException("IOException while downloading artifact: " 
					+ ex.getClass().getSimpleName() + ": " + ex.getMessage(), ex);
		}
	}	


}
