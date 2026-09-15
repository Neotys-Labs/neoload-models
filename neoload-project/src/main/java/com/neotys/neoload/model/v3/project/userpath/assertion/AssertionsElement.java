package com.neotys.neoload.model.v3.project.userpath.assertion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.AssertionsDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.AssertionsSerializer;
import com.neotys.neoload.model.v3.validation.constraints.UniqueContentAssertionNameCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.List;
import javax.validation.Valid;
import org.immutables.value.Value;

public interface AssertionsElement {
	String ASSERTIONS = "assertions";
	String CONTENT_ASSERTIONS = "content_assertions";

	@Deprecated
	@JsonProperty(ASSERTIONS)
	@JsonDeserialize(using = AssertionsDeserializer.class)
	@JsonSerialize(using = AssertionsSerializer.class)
	@UniqueContentAssertionNameCheck(groups={NeoLoad.class})
	@Valid
	List<Assertion> getAssertions();

	@JsonProperty(value = CONTENT_ASSERTIONS, access = JsonProperty.Access.WRITE_ONLY)
	@JsonDeserialize(using = AssertionsDeserializer.class)
	@Value.Default
	default List<Assertion> getContentAssertions() {
		return getAssertions();
	}
}
