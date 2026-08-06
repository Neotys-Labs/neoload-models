package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.CaughtExceptionsCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.Valid;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@CaughtExceptionsCheck(groups = {NeoLoad.class})
@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, TryCatch.CAUGHT_EXCEPTIONS, TryCatch.TRY, TryCatch.CATCH})
@JsonDeserialize(as = ImmutableTryCatch.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface TryCatch extends Step {

	String DEFAULT_NAME = "try_catch";
	String TRY = "try";
	String CATCH = "catch";
	String CAUGHT_EXCEPTIONS = "caught_exceptions";

	enum CaughtException {
		errors,
		assertions
	}

	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(CAUGHT_EXCEPTIONS)
	Optional<List<CaughtException>> getCaughtExceptions();

	@JsonProperty(TRY)
	@RequiredCheck(groups = {NeoLoad.class})
	@Valid
	Container getTry();

	@JsonProperty(CATCH)
	@Valid
	Optional<Container> getCatch();

	@Override
	default Stream<Element> flattened() {
		final Stream<Element> tryStream = getTry().flattened();
		final Stream<Element> catchStream = getCatch().map(Container::flattened).orElse(Stream.empty());
		return Stream.concat(Stream.of(this), Stream.concat(tryStream, catchStream));
	}

	class Builder extends ImmutableTryCatch.Builder {}
	static Builder builder() {
		return new Builder();
	}
}