package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.binding.serializer.ConditionDeserializer;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import javax.validation.Valid;
import java.util.*;

@JsonInclude(value=Include.NON_EMPTY)
@JsonDeserialize(using = ConditionDeserializer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Condition {

	enum Operator {
		EQUALS(Arrays.asList("equals", "==")),
		NOT_EQUALS(Arrays.asList("not_equals", "!=")),
		CONTAINS(Arrays.asList("contains")),
		NOT_CONTAINS(Arrays.asList("not_contains")),
		STARTS_WITH(Arrays.asList("starts_with")),
		NOT_STARTS_WITH(Arrays.asList("not_starts_with")),
		ENDS_WITH(Arrays.asList("ends_with")),
		NOT_ENDS_WITH(Arrays.asList("not_ends_with")),
		MATCH_REGEXP(Arrays.asList("match_regexp")),
		NOT_MATCH_REGEXP(Arrays.asList("not_match_regexp")),
		GREATER(Arrays.asList("greater", ">")),
		GREATER_EQUAL(Arrays.asList("greater_equal", ">=")),
		LESS(Arrays.asList("less", "<")),
		LESS_EQUAL(Arrays.asList("less_equal", "<=")),
		EXISTS(Arrays.asList("exists")),
		NOT_EXISTS(Arrays.asList("not_exists"));

		private final List<String> names;

		private Operator(final List<String> names) {
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

	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	String getOperand1();

	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	Operator getOperator();

	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	Optional<String> getOperand2();

	class Builder extends ImmutableCondition.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
