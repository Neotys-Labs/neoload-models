package com.neotys.neoload.model.repository;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.core.ShareableElement;

import java.util.List;
import java.util.stream.Stream;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public interface IContainer extends ShareableElement {
    List<Element> getChilds();

    @Override
    default Stream<Element> flattened() {
        return Stream.concat(Stream.of(this), getChilds().stream().flatMap(Element::flattened));
    }
}
