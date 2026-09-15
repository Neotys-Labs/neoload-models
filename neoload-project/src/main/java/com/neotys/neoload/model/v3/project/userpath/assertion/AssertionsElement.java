package com.neotys.neoload.model.v3.project.userpath.assertion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.AssertionsDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.AssertionsSerializer;
import com.neotys.neoload.model.v3.validation.constraints.AssertionsFieldCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.List;
import org.immutables.value.Value;

@AssertionsFieldCheck(groups={NeoLoad.class})
public interface AssertionsElement {
	String ASSERTIONS = "assertions";
	String CONTENT_ASSERTIONS = "content_assertions";

	@Deprecated
	@JsonProperty(value = ASSERTIONS, access = JsonProperty.Access.WRITE_ONLY)
	@JsonDeserialize(using = AssertionsDeserializer.class)
	@Value.Auxiliary
	List<Assertion> getAssertions();

	@JsonProperty(CONTENT_ASSERTIONS)
	@JsonDeserialize(using = AssertionsDeserializer.class)
	@JsonSerialize(using = AssertionsSerializer.class)
	@Value.Default
	default List<Assertion> getContentAssertions() {
		return getAssertions();
	}
}
