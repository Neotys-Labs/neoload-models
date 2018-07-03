package com.neotys.models.repository;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
@JsonDeserialize(as = ImmutableServer.class)
public interface Server {
    String getName();
    String getHost();
    String getPort();
    Optional<String> getScheme();
}
