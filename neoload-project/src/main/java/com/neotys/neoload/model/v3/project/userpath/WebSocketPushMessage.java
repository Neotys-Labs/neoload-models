package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.MatchDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.MatchSerializer;
import com.neotys.neoload.model.v3.binding.serializer.StepsDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.StepsSerializer;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.assertion.AssertionsElement;
import com.neotys.neoload.model.v3.validation.constraints.WebSocketPushMessageCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.Valid;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

/**
 * A handler of the frames a {@link WebSocketChannel} receives. For each frame that does not
 * answer a waiting synchronous {@link WebSocketRequest}, every push message whose
 * {@link #getConditions() conditions} are true runs; only when none of them matched, every push
 * message without conditions runs: those are the fallbacks.
 *
 * <p>A running push message applies its {@link #getExtractors() extractors} and
 * {@link #getAssertions() assertions} to the frame, then runs its {@link #getSteps() steps},
 * which may be empty.</p>
 *
 * <p>Its {@link #getName() name} is a segment of the path of the channels declared in its
 * steps, after the name of its own channel.</p>
 */
@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, WebSocketPushMessage.CONDITIONS, Match.MATCH, WebSocketPushMessage.CHARSET, WebSocketPushMessage.EXTRACTORS, AssertionsElement.ASSERTIONS, WebSocketPushMessage.STEPS})
@JsonSerialize(as = ImmutableWebSocketPushMessage.class)
@JsonDeserialize(as = ImmutableWebSocketPushMessage.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@WebSocketPushMessageCheck(groups={NeoLoad.class})
// S2097 suppressed: the nested Jackson value-filter class overrides equals(Object) to compare the
// property value (not another filter instance), which is how the CUSTOM value filter selects the default
// value to omit; a real class check would always be false and defeat the omission.
@SuppressWarnings("java:S2097")
public interface WebSocketPushMessage extends Element, AssertionsElement {
	String DEFAULT_NAME = "push_message";
	String CONDITIONS = "conditions";
	String CHARSET = "charset";
	String EXTRACTORS = "extractors";
	String STEPS = "steps";

	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultNameFilter.class)
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	/**
	 * Present for a conditional push message, and then holding at least one condition; absent
	 * for a fallback. An empty list is kept apart from an absent one so that it can be rejected.
	 */
	@JsonProperty(CONDITIONS)
	@Valid
	Optional<List<Condition>> getConditions();

	/**
	 * How the conditions are combined, {@link Match#ANY} when absent. Only meaningful, and only
	 * allowed, together with {@link #getConditions() conditions}.
	 */
	@JsonProperty(Match.MATCH)
	@JsonSerialize(contentUsing = MatchSerializer.class)
	@JsonDeserialize(contentUsing = MatchDeserializer.class)
	Optional<Match> getMatch();

	@JsonProperty(CHARSET)
	Optional<String> getCharset();

	@JsonProperty(EXTRACTORS)
	@Valid
	List<VariableExtractor> getExtractors();

	@JsonProperty(STEPS)
	@JsonSerialize(using = StepsSerializer.class)
	@JsonDeserialize(using = StepsDeserializer.class)
	@Valid
	List<Step> getSteps();

	/** Whether this push message only runs when no conditional push message of its channel matched. */
	@JsonIgnore
	default boolean isFallback() {
		return !getConditions().isPresent();
	}

	@Override
	default Stream<Element> flattened() {
		return Stream.concat(Stream.of(this), getSteps().stream().flatMap(Step::flattened));
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

	class Builder extends ImmutableWebSocketPushMessage.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
