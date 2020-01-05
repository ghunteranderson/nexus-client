package com.ghunteranderson.nexus.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.ghunteranderson.nexus.client.AssetClient;
import com.ghunteranderson.nexus.client.NexusInstance;
import com.ghunteranderson.nexus.model.Asset;
import com.ghunteranderson.nexus.model.Component;

public class DownloadService {
	
	
	private final AssetClient client;
	
	public DownloadService(NexusInstance instance) {
		client = new AssetClient(instance);
	}
	
	public void downloadAsset(Asset asset, String directory) throws IOException {
		InputStream in = client.download(asset.getRepository(), asset.getPath());
		Path path = Paths.get(directory, getFileName(asset));
		
		File dir = path.toFile();
		if(!dir.exists())
			dir.mkdirs();
		
		Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
	}
	
	public void downloadComponent(Component c, String directory) throws IOException {
		for(Asset a : c.getAssets())
			downloadAsset(a, directory);
	}
	
	private String getFileName(Asset asset) {
		String[] tokens = asset.getPath().split("\\/");
		return tokens[tokens.length-1];
	}	
	
}
