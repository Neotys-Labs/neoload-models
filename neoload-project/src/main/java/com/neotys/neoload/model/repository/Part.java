package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
@Deprecated
public interface Part {

    String getName();
    Optional<String> getContentType();
    Optional<String> getCharSet();
    Optional<String> getTransferEncoding();

    Optional<String> getValue();
    /**
     * File name sent to the server in the Part
     */
    Optional<String> getFilename();

    /**
     * File used as Part content.
     */
    Optional<String> getSourceFilename();
}
