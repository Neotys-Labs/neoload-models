package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

import java.util.Optional;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
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
