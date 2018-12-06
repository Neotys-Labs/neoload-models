package com.neotys.neoload.model.v3.project.userpath;

import java.util.Optional;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.binding.serializer.HeaderDeserializer;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonDeserialize(using = HeaderDeserializer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Header {
	@RequiredCheck(groups={NeoLoad.class})
	String getName();
	
	Optional<String> getValue();
	
	class Builder extends ImmutableHeader.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
