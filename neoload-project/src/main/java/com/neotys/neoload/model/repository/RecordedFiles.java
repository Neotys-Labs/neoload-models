package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

@Value.Immutable
public interface RecordedFiles {
    String recordedRequestHeaderFile();

    String recordedRequestBodyFile();

    String recordedResponseHeaderFile();

    String recordedResponseBodyFile();
}