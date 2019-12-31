package com.ghunteranderson.nexus.model;

import java.util.HashMap;
import java.util.Map;

public interface ComponentQuery {
	
	public static MavenComponentQuery mavenQuery() {
		return new MavenComponentQuery();
	}
	
	
	@SuppressWarnings("unchecked")
	class ComponentQueryBase <E extends ComponentQueryBase<E>>{
		
		
		protected final Map<String, String> query = new HashMap<>();
		
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
		
		
	}
	public class MavenComponentQuery extends ComponentQueryBase<MavenComponentQuery> {
		
		
		public MavenComponentQuery groupId(String groupId) {
			query.put("maven.groupId", groupId);
			return this;
		}
		
		public MavenComponentQuery artifactId(String artifactId) {
			query.put("maven.artifactId", artifactId);
			return this;
		}
		
		public MavenComponentQuery mavenBaseVersion(String baseVersion) {
			query.put("maven.baseVersion", baseVersion);
			return this;
		}
	}

}