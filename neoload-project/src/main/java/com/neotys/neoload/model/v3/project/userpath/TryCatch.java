package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.Valid;
import org.immutables.value.Value;

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({Element.DESCRIPTION, TryCatch.POLICY, TryCatch.TRY, TryCatch.CATCH})
@JsonDeserialize(as = ImmutableTryCatch.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface TryCatch extends Step{
	String DEFAULT_NAME = "tryCatch";
	String TRY = "try";
	String CATCH = "catch";
	String POLICY = "policy";
	enum Policy {
		CATCH_ALL,
		CATCH_ERRORS,
		CATCH_ASSERTIONS,
	}

	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}


	//@RequiredCheck(groups={NeoLoad.class})
	@JsonProperty(TRY)
	@Valid
	Container getTry();

	@Valid
	@JsonProperty(CATCH)
	Container getCatch();

	@Valid
	@JsonProperty(POLICY)
	Optional<Policy> getPolicy();

	@Override
	default Stream<Element> flattened() {
		return Stream.concat(Stream.of(this), Stream.concat(getTry().flattened(), getCatch().flattened()));
	}

}
