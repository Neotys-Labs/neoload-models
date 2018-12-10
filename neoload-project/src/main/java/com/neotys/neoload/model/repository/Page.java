package com.neotys.neoload.model.repository;

import com.neotys.neoload.model.core.Element;
import org.immutables.value.Value;

import java.util.List;
import java.util.stream.Stream;

/**
 * A Page is a container containing only Page or Request.
 */

@Value.Immutable
@Deprecated
public interface Page extends PageElement {
    List<Element> getChilds();
    int getThinkTime();
    boolean isDynamic();

    @Override
    default Stream<Element> flattened() {
        return Stream.concat(Stream.of(this), getChilds().stream().flatMap(Element::flattened));
    }
}
