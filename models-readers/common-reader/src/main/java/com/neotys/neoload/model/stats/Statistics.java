package com.neotys.neoload.model.stats;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface Statistics {

	@Gson.Named("project-type")
	String getProjectType();

	@Gson.Named("script-count")
	int getScriptCount();

	@Gson.Named("status-code")
	String getStatusCode();

	@Gson.Named("duration-millis")
	long getDurationInMillis();

	@Gson.Named("conversion-rate-percent")
	int getConversionRatePercent();

	@Gson.Named("supported-functions-nowarn-count")
	int getSupportedFunctionsNoWarnCount();

	@Gson.Named("supported-functions-warn-count")
	int getSupportedFunctionsWarnCount();

	@Gson.Named("unsupported-functions-count")
	int getUnsupportedFunctionsCount();

	@Gson.Named("unsupported-functions")
	List<FunctionStat> getUnsupportedFunctions();
}
