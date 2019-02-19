package com.neotys.neoload.model.v3.project.userpath;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.binding.serializer.UserPathDeserializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.SlaElement;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonDeserialize(using = UserPathDeserializer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface UserPath extends Element, SlaElement {
	String USER_SESSION = "user_session";
	String RESET_ON = "reset_on";
	String RESET_OFF = "reset_off";
	String RESET_AUTO = "reset_auto";

	String INIT = "init";
	String ACTIONS = "actions";
	String END = "end";

	UserSession DEFAULT_USER_SESSION = UserSession.RESET_AUTO;
	
	enum UserSession {
		@JsonProperty(UserPath.RESET_ON)
		RESET_ON,
		@JsonProperty(UserPath.RESET_OFF)
		RESET_OFF,
		@JsonProperty(UserPath.RESET_AUTO)
		RESET_AUTO;
	}
	
	@Value.Default
	default UserSession getUserSession() {
		return DEFAULT_USER_SESSION;
	}
	
	@Valid
	Optional<Container> getInit();
	
	@RequiredCheck(groups={NeoLoad.class})
	@Valid	
	Container getActions();
	
	@Valid	
	Optional<Container> getEnd();

	@Override
	default Stream<Element> flattened() {
		return Stream.of(getInit().orElse(null), getActions(), getEnd().orElse(null))
				.filter(Objects::nonNull)
				.flatMap(Container::flattened);
	}

	class Builder extends ImmutableUserPath.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
