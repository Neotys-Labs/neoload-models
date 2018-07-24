package com.neotys.neoload.model.repository;

import com.neotys.neoload.model.core.Element;

import java.util.List;
import java.util.stream.Stream;

public interface IContainer extends Element {
    List<Element> getChilds();

    @Override
    default Stream<Element> flattened() {
        return Stream.concat(Stream.of(this), getChilds().stream().flatMap(Element::flattened));
    }
}
