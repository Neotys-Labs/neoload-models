package com.neotys.neoload.model.v3.util;

import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KeyPerformanceIndicator;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.Scope;


public class SlaThresholdUtils {
	private SlaThresholdUtils() {
	}

	public static String toIdentifier(final KeyPerformanceIndicator pki) {
		switch (pki) {
			case AVG_REQUEST_RESP_TIME:
				return "AVERAGE_REQUEST_RESPONSE_TIME";
			case AVG_PAGE_RESP_TIME:
				return "AVERAGE_PAGE_RESPONSE_TIME";
			case AVG_TRANSACTION_RESP_TIME:
				return "AVERAGE_CONTAINER_RESPONSE_TIME";
			case PERC_TRANSACTION_RESP_TIME:
				return "PERCENTILE_CONTAINER_RESPONSE_TIME";
			case AVG_ELT_PER_SEC:
			case AVG_REQUEST_PER_SEC:
				return "AVERAGE_HITS_PER_SECOND";
			case AVG_THROUGHPUT_PER_SEC:
				return "AVERAGE_THROUGHPUT_PER_SECOND";
			case ERRORS_COUNT:
				return "TOTAL_ERRORS";
			case COUNT:
				return "TOTAL_HITS";
			case THROUGHPUT:
				return "TOTAL_THROUGHPUT";
			case AVG_RESP_TIME:
				return "AVERAGE_RESPONSE_TIME";
			case ERRORS_PER_SEC:
				return "ERRORS_PER_SECOND";
			case ERROR_RATE:
				return "ERROR_PERCENTILE";
			default:
				throw new IllegalArgumentException("The key performance indicator '" + pki + "' is unknown.");
		}
	}
	
	public static String toFamily(final Scope scope) {
		switch (scope) {
			case ON_TEST:
				return "PER_RUN";
			case ON_INTERVAL:
				return "PER_TIME_INTERVAL";
			default:
				throw new IllegalArgumentException("The scope '" + scope + "' is unknown.");
		}
	}
}
