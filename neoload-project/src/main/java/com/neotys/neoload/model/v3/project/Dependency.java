package com.neotys.neoload.model.v3.project;

import org.immutables.value.Value;

import java.io.InputStream;
import java.util.Optional;

@Value.Immutable
@Value.Style(redactedMask = "####")
public interface Dependency extends Element {

    String getFilename();

    DependencyType getType();

    // used to extract resources as stream from jars
    @Value.Redacted
    InputStream getInputStream();
}

