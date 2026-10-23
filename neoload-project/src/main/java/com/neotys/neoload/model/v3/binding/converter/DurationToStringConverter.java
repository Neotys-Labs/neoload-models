package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.duration.SecondsDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;

public final class DurationToStringConverter extends StdConverter<LoadDuration, String> {
	@Override
	public String convert(final LoadDuration duration) {
		if (duration == null) return null;

		if (duration.getType() == LoadDuration.Type.TIME) {
			return ((SecondsDuration) duration.getValue()).toText();
		}
		return IterationDurationHelper.convertToString((Integer) duration.getValue());
	}
}
