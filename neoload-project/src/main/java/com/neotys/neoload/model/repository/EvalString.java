package com.neotys.neoload.model.repository;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@JsonDeserialize(as = ImmutableEvalString.class)
public interface EvalString extends ReturnTypeElement {	
	List<Either<String,VariableName>> getContent();
}
