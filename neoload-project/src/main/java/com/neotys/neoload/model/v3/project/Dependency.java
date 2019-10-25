package com.neotys.neoload.model.v3.project;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.io.InputStream;

@Value.Immutable
@Value.Style(redactedMask = "####")
public interface Dependency extends Element {

    @Value.Default
    default String getFileDirectory() {
        return ".";
    }

    String getFilename();

    DependencyType getType();

    // used to extract resources as stream from jars
    @Value.Redacted
    @Nullable
    InputStream getInputStream();

    class Builder extends ImmutableDependency.Builder {}

    static Builder builder() {
        return new Builder();
    }

}

