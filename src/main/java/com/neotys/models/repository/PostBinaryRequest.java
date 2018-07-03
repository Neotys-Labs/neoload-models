package com.neotys.models.repository;

import org.immutables.value.Value;

@Value.Immutable
public interface PostBinaryRequest extends PostRequest {
    byte[] getBinaryData();
}
