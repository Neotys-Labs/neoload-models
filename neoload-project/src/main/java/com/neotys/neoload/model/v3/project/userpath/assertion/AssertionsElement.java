package com.neotys.neoload.model.v3.project.userpath.assertion;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.AssertionsDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.AssertionsSerializer;
import com.neotys.neoload.model.v3.validation.constraints.UniqueContentAssertionNameCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

public interface AssertionsElement {
	String ASSERTIONS = "assertions";
	
	@JsonProperty(ASSERTIONS)
	@JsonDeserialize(using = AssertionsDeserializer.class)
	@JsonSerialize(using = AssertionsSerializer.class)
	@UniqueContentAssertionNameCheck(groups={NeoLoad.class})
	@Valid
	List<Assertion> getAssertions();	
}
