package com.neotys.neoload.model.readers.loadrunner;

import com.neotys.neoload.model.repository.VariableExtractor.ExtractType;

public enum SearchAttribute {

	HEADERS(ExtractType.HEADERS), // search only the header
	BODY(ExtractType.BODY), // search only body data, not headers
	NORESOURCE(ExtractType.BODY), // search only the HTML body, excluding all headers and resources
	COOKIES(ExtractType.HEADERS), // Search only in cookies
	ALL(ExtractType.BOTH); // search body , headers, and resources 

	private final ExtractType extractType;
	
	private SearchAttribute(final ExtractType extractType){
		this.extractType = extractType;
	}

	public ExtractType getExtractType() {
		return extractType;
	}
	
	public static SearchAttribute from(final String value){		 
		for(final SearchAttribute searchAttribute: SearchAttribute.values()){
			if(searchAttribute.toString().equalsIgnoreCase(value)){
				return searchAttribute;
			}
		}
		return SearchAttribute.ALL;
	}

}
