package com.neotys.neoload.model.scenario;

import javax.validation.Valid;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public interface DurationPolicy {
	public static final String DURATION = "duration";

	@Valid
	Duration getDuration();
}
