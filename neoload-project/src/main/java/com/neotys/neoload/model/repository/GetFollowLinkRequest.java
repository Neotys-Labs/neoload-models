package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Value.Immutable
@Deprecated
public interface GetFollowLinkRequest extends GetRequest {
    String getText(); // The exact text that appears in the hypertext link.
    Request getReferer();
}
