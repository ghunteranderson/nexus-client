package com.ghunteranderson.nexus.maven;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.crypto.SettingsDecrypter;

import com.ghunteranderson.nexus.maven.inject.DependencyFactory;
import com.ghunteranderson.nexus.maven.inject.Injector;
import com.ghunteranderson.nexus.maven.inject.MojoContext;

public abstract class AbstractNexusMojo extends AbstractMojo{

	@Parameter(defaultValue="${session}")
	private MavenSession session;
	
	
	@Component
	private SettingsDecrypter settingsDecryptor;

	@Override
	public final void execute() throws MojoExecutionException, MojoFailureException {
		MojoContext context = new MojoContext();
		context.setSession(session);
		context.setLogger(getLog());
		context.setSettingsDecryptor(settingsDecryptor);
		
		DependencyFactory factory = new DependencyFactory();
		new ServiceConfiguration().applyTo(factory);
		Injector injector = new Injector(factory);
		injector.inject(context, this);
		
		run();
	}
	
	protected abstract void run();
	
}
