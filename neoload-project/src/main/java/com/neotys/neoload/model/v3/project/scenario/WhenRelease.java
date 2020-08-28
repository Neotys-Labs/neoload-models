package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.StringToWhenReleaseConverter;
import com.neotys.neoload.model.v3.binding.converter.WhenReleaseToStringConverter;
import com.neotys.neoload.model.v3.project.Composite;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@CompositeCheck(groups={NeoLoad.class}, message="{com.neotys.neoload.model.v3.validation.constraints.CompositeCheck.whenRelease.message}")
@JsonSerialize(converter= WhenReleaseToStringConverter.class)
@JsonDeserialize(converter= StringToWhenReleaseConverter.class)
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface WhenRelease extends Composite<Object, WhenRelease.Type> {
	enum Type {
		PERCENTAGE,
		VU_NUMBER,
		MANUAL
	}

	class Builder extends ImmutableWhenRelease.Builder {}
	static WhenRelease.Builder builder() {
		return new WhenRelease.Builder();
	}
}
