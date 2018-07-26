package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface RecordedFiles {
    Optional<String> recordedRequestHeaderFile();

    Optional<String> recordedRequestBodyFile();

    Optional<String> recordedResponseHeaderFile();

    Optional<String> recordedResponseBodyFile();
}
