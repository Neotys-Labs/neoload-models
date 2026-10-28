package com.neotys.neoload.model.v3.project;

import java.util.Optional;

/**
 * An entity optionally identified by its name; when present, the name must be unique within its list (see {@code UniqueNameCheck}).
 */
public interface UniqueOptionalNameEntity {
	Optional<String> getName();
}
