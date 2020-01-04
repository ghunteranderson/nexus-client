package com.ghunteranderson.nexus.maven;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.crypto.DefaultSettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecryptionResult;

import com.ghunteranderson.nexus.client.NexusInstance;
import com.ghunteranderson.nexus.maven.inject.DependencyFactory;
import com.ghunteranderson.nexus.maven.inject.InjectionBinder;
import com.ghunteranderson.nexus.maven.inject.MojoContext;
import com.ghunteranderson.nexus.service.DownloadService;
import com.ghunteranderson.nexus.service.SearchService;

public class ServiceConfiguration {

	public void applyTo(InjectionBinder factory) {
		factory.bind(NexusInstance.class, this::nexusInstance);
		factory.bind(DownloadService.class, this::downloadService);
		factory.bind(SearchService.class, this::searchService);	
	}
	
	
	private NexusInstance nexusInstance(MojoContext context, DependencyFactory factory) {
		Properties props = context.getSession().getSystemProperties();
		String url = props.getProperty("url");
		String serverId = props.getProperty("serverId");
		
		NexusInstance.Builder builder = NexusInstance.builder(url);
		
		if(StringUtils.isNotBlank(serverId)) {
			Server server = context.getSession().getSettings().getServer(serverId);
			if(server == null)
				context.getLogger().warn("Could not find server with ID " + serverId + ". Attempting to continue.");
			else {
				SettingsDecryptionRequest request = new DefaultSettingsDecryptionRequest(server);
				SettingsDecryptionResult result = context.getSettingsDecryptor().decrypt(request);
				builder.authenticate(result.getServer().getUsername(), result.getServer().getPassword());
			}
		}
		
		return builder.build();
	}
	
	private DownloadService downloadService(MojoContext context, DependencyFactory factory) {
		NexusInstance nexus = factory.getInstance(NexusInstance.class, context);
		return new DownloadService(nexus);
	}
	
	private SearchService searchService(MojoContext context, DependencyFactory factory) {
		NexusInstance nexus = factory.getInstance(NexusInstance.class, context);
		return new SearchService(nexus);
	}

}
