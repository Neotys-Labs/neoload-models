package com.neotys.neoload.model.scenario;

import javax.validation.Valid;

@Deprecated
public interface DurationPolicy {
	public static final String DURATION = "duration";

	@Valid
	Duration getDuration();
}
