package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.immutables.value.Value;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Value.Immutable
@JsonDeserialize(as = ImmutableCustomAction.class)
public interface CustomAction extends Step {
	String getType();
	boolean asRequest();
	List<CustomActionParameter> getParameters();
	Optional<Path> getLibraryPath();

	class Builder extends ImmutableCustomAction.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
