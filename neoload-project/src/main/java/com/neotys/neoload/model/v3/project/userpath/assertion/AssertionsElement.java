package com.neotys.neoload.model.v3.project.userpath.assertion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.validation.constraints.AssertionsFieldCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.List;
import org.immutables.value.Value;

@AssertionsFieldCheck(groups={NeoLoad.class})
public interface AssertionsElement {
	String ASSERTIONS = "assertions";
	String CONTENT_ASSERTIONS = "content_assertions";

	// Bound only to the legacy "assertions" JSON key. Not part of the intended public API: it
	// exists so AssertionsFieldValidator can detect (by reference) whether "content_assertions"
	// was explicitly provided or is falling back to it, which a single @JsonAlias-merged property
	// cannot distinguish (Jackson silently keeps whichever key comes last with no error).
	@JsonProperty(value = ASSERTIONS, access = JsonProperty.Access.WRITE_ONLY)
	@Value.Auxiliary
	List<ContentAssertion> getLegacyAssertions();

	@JsonProperty(CONTENT_ASSERTIONS)
	@Value.Default
	default List<ContentAssertion> getContentAssertions() {
		return getLegacyAssertions();
	}
}
