package com.neotys.neoload.model.v3.project.scenario;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.LoadDurationDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.LoadDurationSerializer;
import com.neotys.neoload.model.v3.validation.constraints.CompositeCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

/**
 * The length of a load: either a {@link TimeLoadDuration} or an {@link IterationLoadDuration}.
 * A plain number in YAML is a number of seconds.
 */
@CompositeCheck(groups={NeoLoad.class}, message="{com.neotys.neoload.model.v3.validation.constraints.CompositeCheck.duration.message}")
@JsonSerialize(using = LoadDurationSerializer.class)
@JsonDeserialize(using = LoadDurationDeserializer.class)
public interface LoadDuration {
	enum Type {
		ITERATION,
		TIME
	}

	Type getType();

	/**
	 * @return the whole seconds of a time duration, the count of an iteration duration
	 */
	long getAmount();
}
