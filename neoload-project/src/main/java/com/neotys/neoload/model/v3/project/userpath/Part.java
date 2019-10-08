package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.project.Element;
import org.immutables.value.Value;

import java.util.Optional;


@Value.Immutable
public interface Part extends Element {

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

    class Builder extends ImmutablePart.Builder {}
    static Builder builder() {
        return new Builder();
    }
}
