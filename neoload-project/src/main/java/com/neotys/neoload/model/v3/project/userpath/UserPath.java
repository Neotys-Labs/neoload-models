package com.neotys.neoload.model.v3.project.userpath;

import java.util.stream.Stream;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, UserPath.INIT, UserPath.ACTIONS, UserPath.END})
@JsonDeserialize(as = ImmutableUserPath.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface UserPath extends Element {
	public static final String INIT = "init";
	public static final String ACTIONS = "actions";
	public static final String END = "end";
	
	@JsonProperty(INIT)
	@Valid
	UnnamedContainer getInitContainer();
	
	@JsonProperty(ACTIONS)
	@RequiredCheck(groups={NeoLoad.class})
	@Valid	
	UnnamedContainer getActionsContainer();
	
	@JsonProperty(END)
	@Valid	
	UnnamedContainer getEndContainer();

	@Override
	default Stream<Element> flattened() {
		return Stream.of(getInitContainer(), getActionsContainer(), getEndContainer()).flatMap(Container::flattened);
	}
	
	class Builder extends ImmutableUserPath.Builder {}
	public static Builder builder() {
		return new Builder();
	}
}
