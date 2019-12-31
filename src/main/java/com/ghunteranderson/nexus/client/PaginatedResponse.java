package com.ghunteranderson.nexus.client;

import java.util.List;

import lombok.Data;

@Data
class PaginatedResponse <K> {
	private List<K> items;
	private String continuationToken;
}
