package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Playback {
	@JsonProperty("parallel")
	PARALLEL,
	@JsonProperty("sequential")
	SEQUENTIAL
}
