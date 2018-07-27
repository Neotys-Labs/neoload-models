package com.neotys.neoload.model.repository;

import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface PostSubmitFormRequest extends PostRequest {
	Request getReferer();	
	Optional<String> getExtractorPath();
}
