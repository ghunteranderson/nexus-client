package com.ghunteranderson.nexus.service;

import java.util.Optional;
import java.util.logging.Logger;

import com.ghunteranderson.jsemver.Version;
import com.ghunteranderson.jsemver.VersionComparator;
import com.ghunteranderson.jsemver.VersionRange;
import com.ghunteranderson.jsemver.VersionSyntaxException;
import com.ghunteranderson.nexus.client.ComponentClient;
import com.ghunteranderson.nexus.model.Component;
import com.ghunteranderson.nexus.model.ComponentQuery;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchService {
	
	private static final Logger logger = Logger.getLogger(SearchService.class.getName());

	private final ComponentClient client;
	
	public Optional<Component> findLatest(String group, String name, VersionRange versionRange){
		VersionComparator comparator = new VersionComparator();
		ComponentQuery query = ComponentQuery.mavenQuery()
				.group(group)
				.name(name);
		
		return client.findAll(query)
			// Parse all component versions
			.map(c -> {
				try {
					Version parsedVersion = Version.from(c.getVersion(), false);
					return new ParsedVersionWrapper<>(parsedVersion, c);
				} catch(VersionSyntaxException ex) {
					logger.warning("Ignoring component with unparsable version " + c.getVersion());
					return (ParsedVersionWrapper<Component>) null;
				}
			})
			// Remove any unparsable versions and versions not in range
			.filter(w -> {
				if(w == null)
					return false;
				else
					return versionRange.contains(w.getVersion());
			})
			// Sort in reverse order
			.sorted( (w1, w2) -> -1*comparator.compare(w1.getVersion(), w2.getVersion()))
			// Return latest versions
			.findFirst()
			.map(w -> w.getSource());
	}
	
	@Data
	@RequiredArgsConstructor
	private static class ParsedVersionWrapper<T> {
		private final Version version;
		private final T source;
	}
	
	
}
