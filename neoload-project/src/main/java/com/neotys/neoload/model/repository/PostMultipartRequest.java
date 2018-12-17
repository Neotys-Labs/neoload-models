package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

import java.util.List;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Value.Immutable
@Deprecated
public interface PostMultipartRequest extends PostRequest {

    List<Part> getParts();
}
