package com.neotys.neoload.model.stats;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface FunctionStat {

	@Gson.Named("name")
	String getName();

	@Gson.Named("count")
	int getCount();
}
