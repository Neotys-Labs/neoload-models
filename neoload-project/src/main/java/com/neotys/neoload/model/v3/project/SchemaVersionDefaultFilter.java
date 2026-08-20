package com.neotys.neoload.model.v3.project;

/**
 * Jackson valueFilter that omits {@code schemaVersion} from serialization
 * when the value equals the default ("3.0").
 *
 * Used to preserve round-trip compatibility with YAML files that do not
 * declare a {@code schemaVersion} (they are interpreted as 3.0 and remain
 * unchanged when written back).
 */
public final class SchemaVersionDefaultFilter {

	@Override
	public boolean equals(final Object other) {
		return Project.DEFAULT_SCHEMA_VERSION.equals(other);
	}

	@Override
	public int hashCode() {
		return 0;
	}
}
