package com.neotys.neoload.model.v3.project.framework;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ExtractionSource {
	@JsonProperty(ExtractionSource.BODY_VALUE)
	BODY,
	@JsonProperty(ExtractionSource.HEADERS_VALUE)
	HEADERS,
	@JsonProperty(ExtractionSource.ALL_VALUE)
	ALL;

	public static final String BODY_VALUE = "body";
	public static final String HEADERS_VALUE = "headers";
	public static final String ALL_VALUE = "all";
}