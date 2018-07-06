package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface PostFormRequest extends PostRequest {
    List<Parameter> getPostParameters();
}
