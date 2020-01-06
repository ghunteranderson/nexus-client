package com.ghunteranderson.nexus.maven.inject;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.settings.crypto.SettingsDecrypter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class MojoContext {
	
	private MavenSession session;
	private Log logger;
	private SettingsDecrypter settingsDecryptor;
}
