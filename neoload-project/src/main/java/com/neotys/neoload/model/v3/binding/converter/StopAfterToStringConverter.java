package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;

public final class StopAfterToStringConverter extends StdConverter<StopAfter, String> {
	private static final String CURRENT_ITERATION = "current_iteration";

	@Override
	public String convert(final StopAfter stopAfter) {
		if (stopAfter == null) return null;

		if (stopAfter.getType() == StopAfter.Type.TIME) {
			return ((SecondsDuration) stopAfter.getValue()).toText();
		}
		return CURRENT_ITERATION;
	}
}
