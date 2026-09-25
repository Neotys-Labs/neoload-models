package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import javax.validation.Valid;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_DEFAULT)
@JsonPropertyOrder({SoapRequest.NAME, Element.DESCRIPTION, UrlServerElement.URL, UrlServerElement.SERVER, SoapRequest.CONTENT})
@JsonSerialize(as = ImmutableSoapRequest.class)
@JsonDeserialize(as = ImmutableSoapRequest.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@SuppressWarnings("java:S2097")
public interface SoapRequest extends Step, UrlServerElement {
	String NAME = "name";
	String CONTENT = "content";

	String DEFAULT_NAME = "soap_request";

	@JsonProperty(NAME)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultNameFilter.class)
	@RequiredCheck(groups = {NeoLoad.class})
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@JsonProperty(CONTENT)
	@RequiredCheck(groups = {NeoLoad.class})
	@Valid
	SoapRequestContent getContent();

	class DefaultNameFilter {
		@Override
		public boolean equals(final Object value) {
			return DEFAULT_NAME.equals(value);
		}

		@Override
		public int hashCode() {
			return DEFAULT_NAME.hashCode();
		}
	}

	class Builder extends ImmutableSoapRequest.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
