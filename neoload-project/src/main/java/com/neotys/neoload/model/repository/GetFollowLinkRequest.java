package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

@Value.Immutable
public interface GetFollowLinkRequest extends GetRequest {
    String getText(); // The exact text that appears in the hypertext link.
    Request getReferer();
}
