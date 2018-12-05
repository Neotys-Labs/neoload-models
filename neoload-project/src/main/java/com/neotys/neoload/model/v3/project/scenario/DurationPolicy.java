package com.neotys.neoload.model.v3.project.scenario;

import javax.validation.Valid;

public interface DurationPolicy {
	String DURATION = "duration";

	@Valid
	Duration getDuration();
}
