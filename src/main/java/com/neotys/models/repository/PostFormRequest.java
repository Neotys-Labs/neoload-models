package com.neotys.models.repository;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface PostFormRequest extends PostRequest {
    List<Parameter> getPostParameters();
}
