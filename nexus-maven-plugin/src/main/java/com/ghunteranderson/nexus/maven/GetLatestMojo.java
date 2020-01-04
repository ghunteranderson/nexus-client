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
public abstract class GetLatestMojo extends AbstractMojo {

}
