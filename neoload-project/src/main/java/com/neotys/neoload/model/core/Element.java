package com.neotys.neoload.model.core;

import java.util.Optional;
import java.util.stream.Stream;

import com.neotys.neoload.model.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.validation.groups.NeoLoad;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public interface Element {
	String NAME = "name";
	String DESCRIPTION = "description";

	@RequiredCheck(groups={NeoLoad.class})
	String getName();
    Optional<String> getDescription();
    
    Element withName(String of);

    default Stream<Element> flattened() {
        return Stream.of(this);
    }
}
