package com.neotys.neoload.model.v3.project.userpath.assertion;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.validation.constraints.UniqueContentAssertionNameCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

public interface ContentAssertionElement {
	String ASSERT_CONTENT = "assert_content";
	
	@JsonProperty(ASSERT_CONTENT)
	@UniqueContentAssertionNameCheck(groups={NeoLoad.class})
	@Valid
	List<ContentAssertion> getContentAssertions();	
}
