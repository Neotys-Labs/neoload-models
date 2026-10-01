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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.UserPathDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.UserPathSerializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.assertion.AssertionsElement;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonSerialize(using = UserPathSerializer.class)
@JsonDeserialize(using = UserPathDeserializer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface UserPath extends Element, AssertionsElement {
	String USER_SESSION = "user_session";
	String RESET_ON = "reset_on";
	String RESET_OFF = "reset_off";
	String RESET_AUTO = "reset_auto";

	String ON_ERROR = "on_error";
	String ON_ASSERTION_FAILURE = "on_assertion_failure";
	String DO_NOTHING = "do_nothing";
	String GO_TO_NEXT_ITERATION = "go_to_next_iteration";
	String STOP_AND_START_NEW_VU = "stop_and_start_new_vu";

	String THINK_TIME = "think_time";

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
	
	/**
	 * What the Virtual User does when an error occurs or an assertion fails, as in the designer's
	 * User Path "Runtime parameters" panel.
	 */
	enum FailurePolicy {
		@JsonProperty(UserPath.DO_NOTHING)
		DO_NOTHING,
		@JsonProperty(UserPath.GO_TO_NEXT_ITERATION)
		GO_TO_NEXT_ITERATION,
		@JsonProperty(UserPath.STOP_AND_START_NEW_VU)
		STOP_AND_START_NEW_VU;
	}

	@JsonInclude(value=Include.NON_DEFAULT)
	@Value.Default
	default UserSession getUserSession() {
		return DEFAULT_USER_SESSION;
	}
	
	/**
	 * The policy applied when an error occurs. Empty when not declared: the NeoLoad default then applies.
	 */
	Optional<FailurePolicy> getOnError();

	/**
	 * The policy applied when an assertion fails. Empty when not declared: the NeoLoad default then applies.
	 */
	Optional<FailurePolicy> getOnAssertionFailure();

	@Valid
	Optional<UserPathThinkTime> getThinkTime();

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
