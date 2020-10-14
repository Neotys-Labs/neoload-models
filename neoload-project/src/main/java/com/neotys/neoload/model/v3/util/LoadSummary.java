package com.neotys.neoload.model.v3.util;

import java.util.Optional;

import org.immutables.value.Value;

import com.neotys.neoload.model.v3.project.scenario.LoadDuration;

@Value.Immutable
public interface LoadSummary {
	Optional<Integer> getMaxUsers();	
	Optional<LoadDuration> getDuration();
	
	class Builder extends ImmutableLoadSummary.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
