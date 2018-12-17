package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

import java.util.Optional;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Value.Immutable
@Deprecated
public interface RecordedFiles {
    Optional<String> recordedRequestHeaderFile();

    Optional<String> recordedRequestBodyFile();

    Optional<String> recordedResponseHeaderFile();

    Optional<String> recordedResponseBodyFile();
}
