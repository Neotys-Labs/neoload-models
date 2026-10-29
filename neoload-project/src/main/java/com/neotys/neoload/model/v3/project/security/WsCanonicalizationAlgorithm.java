package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WsCanonicalizationAlgorithm {
	CANONICAL_XML_OMIT_COMMENTS("canonical_xml_omit_comments"),
	CANONICAL_XML_WITH_COMMENTS("canonical_xml_with_comments"),
	EXCLUSIVE_CANONICAL_XML_OMIT_COMMENTS("exclusive_canonical_xml_omit_comments"),
	EXCLUSIVE_CANONICAL_XML_WITH_COMMENTS("exclusive_canonical_xml_with_comments");

	private final String value;

	WsCanonicalizationAlgorithm(final String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	/**
	 * Jackson value filter omitting the property from serialization when it equals the default ({@code exclusive_canonical_xml_omit_comments}).
	 */
	public static class DefaultFilter {
		@Override
		public boolean equals(final Object value) { return EXCLUSIVE_CANONICAL_XML_OMIT_COMMENTS.equals(value); }
		@Override
		public int hashCode() { return EXCLUSIVE_CANONICAL_XML_OMIT_COMMENTS.hashCode(); }
	}
}
