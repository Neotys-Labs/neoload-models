package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.binding.serializer.ConditionDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.ConditionSerializer;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import javax.validation.Valid;
import java.util.*;

@JsonInclude(value=Include.NON_EMPTY)
@JsonSerialize(using = ConditionSerializer.class)
@JsonDeserialize(using = ConditionDeserializer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Condition {

	enum Operator {
		EQUALS(List.of("equals", "==")),
		NOT_EQUALS(List.of("not_equals", "!=")),
		CONTAINS(List.of("contains")),
		NOT_CONTAINS(List.of("not_contains")),
		STARTS_WITH(List.of("starts_with")),
		NOT_STARTS_WITH(List.of("not_starts_with")),
		ENDS_WITH(List.of("ends_with")),
		NOT_ENDS_WITH(List.of("not_ends_with")),
		MATCH_REGEXP(List.of("match_regexp")),
		NOT_MATCH_REGEXP(List.of("not_match_regexp")),
		GREATER(List.of("greater", ">")),
		GREATER_EQUAL(List.of("greater_equal", ">=")),
		LESS(List.of("less", "<")),
		LESS_EQUAL(List.of("less_equal", "<=")),
		EXISTS(List.of("exists")),
		NOT_EXISTS(List.of("not_exists"));

		private final List<String> names;

		Operator(final List<String> names) {
			this.names = names;
		}

		public static Operator of(final String name) {
			if (Strings.isNullOrEmpty(name)) {
				throw new IllegalArgumentException("The operator must not be null or empty.");
			}
			for(Operator operator : values()){
				if(operator.getNames().contains(name)){
					return operator;
				}
			}
			final Set<String> possibleValues = new HashSet<>();
			for(Operator operator : values()){
				possibleValues.addAll(operator.getNames());
			}

			throw new IllegalArgumentException("The operator must be: " + possibleValues + ".");
		}

		public List<String> getNames() {
			return names;
		}
	}

	@Valid
	String getOperand1();

	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	Operator getOperator();

	@Valid
	Optional<String> getOperand2();

	class Builder extends ImmutableCondition.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
