package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

@Value.Immutable
@Deprecated
public interface PostSubmitFormRequest extends PostRequest {
	Request getReferer();	
}
