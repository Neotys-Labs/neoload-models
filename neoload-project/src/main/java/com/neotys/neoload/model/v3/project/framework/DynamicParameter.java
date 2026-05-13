package com.neotys.neoload.model.v3.project.framework;

import java.util.Optional;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

// Note: the extraction primitives (xpath, jsonpath, regexp, template) are intentionally
// duplicated from VariableExtractor for now. See LOAD-37460 (tech debt) for the planned
// refactor into a shared ExtractionPrimitives interface. Note also that match_number is
// intentionally absent: the designer-side ExpressionPathRegexpExtractorDefinition used by
// DynamicParameter does not support an Nth-match selector.
@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({
		Element.NAME,
		Element.DESCRIPTION,
		DynamicParameter.ENABLED,
		DynamicParameter.EXTRACTION_SOURCE,
		DynamicParameter.XPATH,
		DynamicParameter.JSON_PATH,
		DynamicParameter.REGEXP,
		DynamicParameter.TEMPLATE
})
@JsonDeserialize(as = ImmutableDynamicParameter.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface DynamicParameter extends Element {
	String ENABLED = "enabled";
	String EXTRACTION_SOURCE = "extraction_source";
	String XPATH = "xpath";
	String JSON_PATH = "jsonpath";
	String REGEXP = "regexp";
	String TEMPLATE = "template";

	@JsonProperty(ENABLED)
	@Value.Default
	default boolean isEnabled() {
		return true;
	}

	@JsonProperty(EXTRACTION_SOURCE)
	@RequiredCheck(groups = {NeoLoad.class})
	@Value.Default
	default ExtractionSource getExtractionSource() {
		return ExtractionSource.BODY;
	}

	@JsonProperty(XPATH)
	Optional<String> getXpath();

	@JsonProperty(JSON_PATH)
	Optional<String> getJsonPath();

	@JsonProperty(REGEXP)
	Optional<String> getRegexp();

	@JsonProperty(TEMPLATE)
	@Value.Default
	default String getTemplate() {
		return "$1$";
	}

	class Builder extends ImmutableDynamicParameter.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}