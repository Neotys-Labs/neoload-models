package com.neotys.neoload.model.v3.project.population;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.DoubleToPercentageConverter;
import com.neotys.neoload.model.v3.binding.converter.PercentageToDoubleConverter;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.DigitsCheck;
import com.neotys.neoload.model.v3.validation.constraints.RangeCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import java.util.Optional;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, UserPathPolicy.DISTRIBUTION})
@JsonDeserialize(as = ImmutableUserPathPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface UserPathPolicy extends Element {
	String DISTRIBUTION = "distribution";
	
	@JsonIgnore
	Optional<String> getDescription();
	
	@JsonSerialize(converter=DoubleToPercentageConverter.class)
	@JsonDeserialize(converter=PercentageToDoubleConverter.class)
	@JsonProperty(DISTRIBUTION)
	@DigitsCheck(integer=3, fraction=1, groups={NeoLoad.class})
	@RangeCheck(min=0, max=100, groups={NeoLoad.class})
	Double getDistribution();
	
	class Builder extends ImmutableUserPathPolicy.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
