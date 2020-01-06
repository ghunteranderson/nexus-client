package com.ghunteranderson.nexus.maven;

import com.ghunteranderson.nexus.model.Asset;
import com.ghunteranderson.nexus.model.Component;

public class FormatUtils {
	public static String gavCoordinates(Component c) {
		return String.format("%s:%s:%s", c.getGroup(), c.getName(), c.getVersion());
	}
	
	public static String fileName(Asset asset) {
		String[] tokens = asset.getPath().split("\\/");
		return tokens[tokens.length-1];
	}
}
