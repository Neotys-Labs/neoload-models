package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

@Value.Immutable
@Deprecated
public interface PostTextRequest extends PostRequest {
    String getData();
}
