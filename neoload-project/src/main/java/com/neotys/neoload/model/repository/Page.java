package com.neotys.neoload.model.repository;

import org.immutables.value.Value;

import java.util.List;

/**
 * A Page is a container containing only Page or Request.
 */

@Value.Immutable
public interface Page extends PageElement {
    List<PageElement> getChilds();
    int getThinkTime();
}
