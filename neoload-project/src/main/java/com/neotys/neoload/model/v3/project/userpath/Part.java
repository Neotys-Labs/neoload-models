package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.project.Element;
import org.immutables.value.Value;

import java.util.Optional;


@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, Part.CONTENT_TYPE, Part.CHARSET, Part.TRANSFER_ENCODING, Part.VALUE, Part.FILENAME, Part.SOURCE_FILENAME})
@JsonSerialize(as = ImmutablePart.class)
@JsonDeserialize(as = ImmutablePart.class)
@Value.Immutable
public interface Part extends Element {

	String CONTENT_TYPE = "content_type";
	String CHARSET = "charset";
	String TRANSFER_ENCODING = "transfer_encoding";
	String VALUE = "value";
	String FILENAME = "filename";
	String SOURCE_FILENAME = "source_filename";

	@JsonProperty(CONTENT_TYPE)
	Optional<String> getContentType();

	@JsonProperty(CHARSET)
	Optional<String> getCharSet();

	@JsonProperty(TRANSFER_ENCODING)
	Optional<String> getTransferEncoding();

	@JsonProperty(VALUE)
	Optional<String> getValue();

	/**
	 * File name sent to the server in the Part
	 */
	@JsonProperty(FILENAME)
	Optional<String> getFilename();

	/**
	 * File used as Part content.
	 */
	@JsonProperty(SOURCE_FILENAME)
	Optional<String> getSourceFilename();

	class Builder extends ImmutablePart.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
