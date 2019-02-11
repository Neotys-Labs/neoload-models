package com.neotys.neoload.model.v3.util;

import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Operator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;


public class SlaThresholdConditionUtils {
	private SlaThresholdConditionUtils() {
	}

	public static String toSeverity(final Severity severity) {
		switch (severity) {
			case FAIL:
				return "HIGH";
			case WARN:
				return "LOW";
			default:
				throw new IllegalArgumentException("The severity '" + severity + "' is unknown.");
		}
	}
	
	public static String toOperator(final Operator operator) {
		switch (operator) {
			case LESS_THAN:
				return "LESS_THAN";
			case GREATER_THAN:
				return "GREATER_THAN";
			case EQUALS:
				return "EQUALS";
			default:
				throw new IllegalArgumentException("The operator '" + operator + "' is unknown.");
		}
	}
}
