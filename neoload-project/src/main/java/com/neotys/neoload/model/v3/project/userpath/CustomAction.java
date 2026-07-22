package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.neotys.neoload.model.v3.project.Element;
import org.immutables.value.Value;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;


@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, CustomAction.TYPE, CustomAction.PARAMETERS, CustomAction.AS_REQUEST, CustomAction.LIBRARY_PATH})
@JsonSerialize(as = ImmutableCustomAction.class)
@JsonDeserialize(as = ImmutableCustomAction.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface CustomAction extends Step {
	String TYPE = "type";
	String PARAMETERS = "parameters";
	String AS_REQUEST = "asRequest";
	String LIBRARY_PATH = "libraryPath";

	@JsonProperty(TYPE)
	String getType();

	@JsonProperty(PARAMETERS)
	List<CustomActionParameter> getParameters();

	@JsonProperty(AS_REQUEST)
	@JsonInclude(Include.NON_DEFAULT)
	@Value.Default
	default boolean asRequest(){
		return false;
	}

	@JsonProperty(LIBRARY_PATH)
	Optional<Path> getLibraryPath();

	class Builder extends ImmutableCustomAction.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
