package com.ghunteranderson.nexus.model;

import java.util.HashMap;
import java.util.Map;

public interface ComponentQuery {
	
	public static MavenComponentQuery mavenQuery() {
		return new MavenComponentQuery();
	}
	
	public Map<String, String> getQueryMap();
	
	@SuppressWarnings("unchecked")
	class ComponentQueryBase <E extends ComponentQueryBase<E>> implements ComponentQuery{
		
		private final Map<String, String> query = new HashMap<>();
		
		public Map<String, String> getQueryMap(){
			return query;
		}
		
		public E sortBy(ComponentSortCategory category) {
			query.put("sort", category.name().toLowerCase());
			return (E) this;
		}
		
		public E sortDirection(SortDirection sortDirection){
			query.put("direction", sortDirection.name().toLowerCase());
			return (E) this;
		}
		
		public E keyword(String keyword){
			query.put("q", keyword);
			return (E) this;
		}
		
		public E format(String format) {
			query.put("format", format);
			return (E) this;
		}
		
		public E group(String group) {
			query.put("group", group);
			return (E) this;
		}
		
		public E name(String name) {
			query.put("name", name);
			return (E) this;
		}
		
		public E version(String version) {
			query.put("version", version);
			return (E) this;
		}
		
		public E md5(String md5) {
			query.put("md5", md5);
			return (E) this;
		}
		
		public E sha1(String sha1) {
			query.put("sha1", sha1);
			return (E) this;
		}
		
		public E sha256(String sha256) {
			query.put("sha256", sha256);
			return (E) this;
		}
		
		public E sha512(String sha512) {
			query.put("sha512", sha512);
			return (E) this;
		}
		
		public E prerelease(String prerelease) {
			query.put("prerelease", prerelease);
			return (E) this;
		}
		
		public E repository(String repository) {
			query.put("repository", repository);
			return (E) this;
		}
		
	}
	
	public class MavenComponentQuery extends ComponentQueryBase<MavenComponentQuery> {
		
		@Override
		public MavenComponentQuery group(String groupId) {
			super.group(groupId);
			getQueryMap().put("maven.groupId", groupId);
			return this;
		}
		
		@Override
		public MavenComponentQuery name(String artifactId) {
			super.name(artifactId);
			getQueryMap().put("maven.artifactId", artifactId);
			return this;
		}
		
		public MavenComponentQuery baseVersion(String baseVersion) {
			getQueryMap().put("maven.baseVersion", baseVersion);
			return this;
		}
		
		public MavenComponentQuery extension(String extension) {
			getQueryMap().put("maven.extension", extension);
			return this;
		}
		
		public MavenComponentQuery classifier(String classifier) {
			getQueryMap().put("maven.classifier", classifier);
			return this;
		}
	}

}