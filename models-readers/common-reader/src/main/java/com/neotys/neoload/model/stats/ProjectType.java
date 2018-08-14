package com.neotys.neoload.model.stats;

public enum ProjectType {
	LOAD_RUNNER("LoadRunner");

	private final String name;

	ProjectType(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
