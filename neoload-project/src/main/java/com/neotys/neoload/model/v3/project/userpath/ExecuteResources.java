package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ExecuteResources {
	@JsonProperty("static")
	STATIC,
	@JsonProperty("dynamic")
	DYNAMIC,
	@JsonProperty("dynamic_forced_encoding")
	DYNAMIC_FORCED_ENCODING
}
