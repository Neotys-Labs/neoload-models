package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.Valid;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_DEFAULT)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, TryCatch.CAUGHT_EXCEPTIONS, TryCatch.TRY, TryCatch.CATCH})
@JsonDeserialize(as = ImmutableTryCatch.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
// S2097 suppressed: the nested Jackson value-filter classes override equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface TryCatch extends Step {

	String DEFAULT_NAME = "try_catch";
	String TRY = "try";
	String CATCH = "catch";
	String CAUGHT_EXCEPTIONS = "caught_exceptions";
	CaughtException DEFAULT_CAUGHT_EXCEPTION = CaughtException.ERRORS;

	enum CaughtException {
		@JsonProperty("errors")
		ERRORS,
		@JsonProperty("assertions")
		ASSERTIONS,
		@JsonProperty("all")
		ALL
	}

	@Value.Default
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultNameFilter.class)
	default String getName() {
		return DEFAULT_NAME;
	}

	class DefaultNameFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_NAME.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_NAME.hashCode();
		}
	}

	@JsonProperty(CAUGHT_EXCEPTIONS)
	@Value.Default
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultCaughtExceptionFilter.class)
	default CaughtException getCaughtExceptions()  { return DEFAULT_CAUGHT_EXCEPTION; }

	class DefaultCaughtExceptionFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_CAUGHT_EXCEPTION.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_CAUGHT_EXCEPTION.hashCode();
		}
	}

	@JsonProperty(TRY)
	@Valid
	Optional<Container> getTry();

	@JsonProperty(CATCH)
	@Valid
	Optional<Container> getCatch();

	@Override
	default Stream<Element> flattened() {
		final Stream<Element> tryStream = getTry().map(Container::flattened).orElse(Stream.empty());
		final Stream<Element> catchStream = getCatch().map(Container::flattened).orElse(Stream.empty());
		return Stream.concat(Stream.of(this), Stream.concat(tryStream, catchStream));
	}

	class Builder extends ImmutableTryCatch.Builder {}
	static Builder builder() {
		return new Builder();
	}
}