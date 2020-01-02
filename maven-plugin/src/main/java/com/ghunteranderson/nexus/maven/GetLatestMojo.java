package com.ghunteranderson.nexus.maven;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.crypto.DefaultSettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.apache.maven.settings.crypto.SettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecryptionResult;

import com.ghunteranderson.jsemver.Version;
import com.ghunteranderson.jsemver.VersionComparator;
import com.ghunteranderson.jsemver.VersionRange;
import com.ghunteranderson.nexus.client.AssetClient;
import com.ghunteranderson.nexus.client.ComponentClient;
import com.ghunteranderson.nexus.client.NexusInstance;
import com.ghunteranderson.nexus.model.Asset;
import com.ghunteranderson.nexus.model.Component;
import com.ghunteranderson.nexus.model.ComponentQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Mojo(name="getLatest",
		requiresProject = false)
public class GetLatestMojo extends AbstractMojo {
	
	@Parameter( defaultValue = "${settings}", readonly = true )
    private Settings settings;
	
	@org.apache.maven.plugins.annotations.Component
    private SettingsDecrypter decrypter;
	
	@Parameter(defaultValue="${serverId}")
	private String serverId;
	@Parameter(defaultValue="${url}")
	private String url;
	@Parameter(defaultValue="${groupId}")
	private String groupId;
	@Parameter(defaultValue="${artifactId}")
	private String artifactId;
	@Parameter(defaultValue="${version}")
	private String version;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if(url == null || url.trim().isEmpty())
			throw new MojoExecutionException("Property \"url\" is missing.");
		if(groupId == null || groupId.trim().isEmpty())
			throw new MojoExecutionException("Property \"groupId\" is missing.");
		if(artifactId == null || artifactId.trim().isEmpty())
			throw new MojoExecutionException("Property \"artifactId\" is missing.");
		if(version == null || version.trim().isEmpty())
			throw new MojoExecutionException("Property \"version\" is missing.");
		
		
		NexusInstance instance = buildNexusInstance();
		Optional<Component> optionalComponent = findLatestComponent(instance);
		
		if(!optionalComponent.isPresent())
			throw new MojoFailureException(String.format("Could not find any artifacts at %s with coordinates %s:%s and version selector \"%s\"", url, groupId, artifactId, version));
		
		Component component = optionalComponent.get();
		getLog().info(String.format("Found component %s:%s:%s", component.getGroup(), component.getName(), component.getVersion()));
		
		for(Asset asset : optionalComponent.get().getAssets())
			download(instance, asset);
	}
	
	private NexusInstance buildNexusInstance() {
		NexusInstance.Builder builder = NexusInstance.builder(url);
		
		if(serverId != null && !serverId.trim().isEmpty()) {
			Server server = settings.getServer(serverId);
			if(server==null) {
				getLog().warn("No server found named " + serverId + ". Attempting to continue without authentication.");
				settings.getServers().stream().forEach(s -> {
					getLog().debug("Found server: " + s.getId());
				});
				if(settings.getServers().isEmpty())
					getLog().warn("No servers found");
			}
			else {
				SettingsDecryptionRequest request = new DefaultSettingsDecryptionRequest(server);
				SettingsDecryptionResult result = decrypter.decrypt(request);				
				builder.authenticate(result.getServer().getUsername(), result.getServer().getPassword());
			}
		}
		
		return builder.build();
	}
	
	private Optional<Component> findLatestComponent(NexusInstance instance){
		VersionRange versionRange = VersionRange.from(version);
		VersionComparator comparator = new VersionComparator();
		
		ComponentClient client = new ComponentClient(instance);
		ComponentQuery query = ComponentQuery.mavenQuery()
				.group(groupId)
				.name(artifactId);
		
		return client.findAll(query)
				.map(c -> {
					getLog().debug("Found component: " + c);
					return c;
				})
				// Only accept versions in range
				.filter(c -> versionRange.contains(Version.from(c.getVersion())))
				// Sort them in reverse order (newest to oldest)
				.sorted((c1, c2) -> -1*comparator.compare(Version.from(c1.getVersion()), Version.from(c2.getVersion())))
				// Get the newest component
				.findFirst();
	}
	
	private void download(NexusInstance instance, Asset asset) throws MojoExecutionException{
		String outputName = getOutputFileName(asset);
		AssetClient client = new AssetClient(instance);
		
		try {
			getLog().info("Downloading artifact: " + asset.getPath());
			InputStream in = client.download(asset);
			File file = new File(outputName);
			Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);	
		} catch(Exception ex) {
			throw new MojoExecutionException("An error ocurred while downloading asset: " + asset.getDownloadUrl());
		}

	}
	
	private String getOutputFileName(Asset asset) {
		String[] tokens = asset.getPath().split("\\/");
		return tokens[tokens.length-1];
	}
	

}
