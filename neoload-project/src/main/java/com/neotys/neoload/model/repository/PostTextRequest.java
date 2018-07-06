package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

@Value.Immutable
public interface PostTextRequest extends PostRequest {
    String getData();
}
