package com.neotys.neoload.model.repository;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableServer.class)
@Deprecated
public interface Server {
    String getName();
    String getHost();
    String getPort();
    Optional<String> getScheme();
}
