package com.neotys.neoload.model.core;

import java.util.Optional;
import java.util.stream.Stream;

public interface Element {
    String getName();
    Optional<String> getDescription();
    Element withName(String of);

    default Stream<Element> flattened() {
        return Stream.of(this);
    }
}
