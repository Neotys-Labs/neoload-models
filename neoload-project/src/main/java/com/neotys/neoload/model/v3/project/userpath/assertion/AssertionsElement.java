package com.neotys.neoload.model.v3.project.userpath.assertion;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.validation.constraints.UniqueContentAssertionNameCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.List;
import javax.validation.Valid;

public interface AssertionsElement {
	String ASSERTIONS = "assertions";
	String CONTENT_ASSERTIONS = "content_assertions";

	@JsonProperty(CONTENT_ASSERTIONS)
	@JsonAlias(ASSERTIONS)
	@UniqueContentAssertionNameCheck(groups={NeoLoad.class})
	@Valid
	List<ContentAssertion> getContentAssertions();
}
