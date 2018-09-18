package com.neotys.neoload.model.repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.core.Element;

@Value.Immutable
@JsonDeserialize(as = ImmutableCustomAction.class)
public interface CustomAction extends Element {
	String getType();
	boolean isHit();
	List<CustomActionParameter> getParameters();
	Optional<Path> getLibraryPath();
}
