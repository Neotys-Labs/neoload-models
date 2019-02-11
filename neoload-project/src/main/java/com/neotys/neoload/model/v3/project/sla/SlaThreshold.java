package com.neotys.neoload.model.v3.project.sla;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.neotys.neoload.model.v3.binding.serializer.SlaThresholdDeserializer;
import com.neotys.neoload.model.v3.validation.constraints.RangeCheck;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.SlaThresholdCheck;
import com.neotys.neoload.model.v3.validation.constraints.SlaThresholdCheck.UsageType;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value = Include.NON_EMPTY)
@JsonDeserialize(using = SlaThresholdDeserializer.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@SlaThresholdCheck.List({
		@SlaThresholdCheck(usage = UsageType.CHECK_RELATIONSHIP_KPI_AND_SCOPE, message = "{com.neotys.neoload.model.v3.validation.constraints.SlaThresholdCheck.RelationshipKpiAndScope.message}", groups = {NeoLoad.class}),
		@SlaThresholdCheck(usage = UsageType.CHECK_UNIQUE_CONDITION_SEVERITY, message = "{com.neotys.neoload.model.v3.validation.constraints.SlaThresholdCheck.UniqueConditionSeverity.message}", groups = {NeoLoad.class})
})
public interface SlaThreshold  {
	enum KeyPerformanceIndicator {
		AVG_ELT_PER_SEC ("/s"),                 // "avg-elt-per-sec"  -> "Average Elements per Second"
		AVG_PAGE_RESP_TIME ("ms", "s"),         // "avg-page-resp-time"  -> "Average Page Response time"
		AVG_REQUEST_PER_SEC ("/s"),             // "avg-request-per-sec" ->"Average Requests per Second" for requests. 
		AVG_REQUEST_RESP_TIME ("ms", "s"),      // "avg-request-resp-time" -> "Average Request Response time"
		AVG_RESP_TIME ("ms", "s"),              // "avg-resp-time" -> "Average Response Time (sec.)"
		AVG_THROUGHPUT_PER_SEC ("Mbps"),        // "avg-throughput-per-sec" -> "Average Throughput per Second (Mbits/s)"
		AVG_TRANSACTION_RESP_TIME ("ms", "s"),  // "avg-transaction-resp-time" -> "Average Transaction Response time"
		
		COUNT,                                  // "count" -> "Total count'
		
		ERROR_RATE ("%"),                       // "error-rate" -> "Error Rate (%)"
		ERRORS_PER_SEC ("/s"),                  // "errors-per-sec" -> "Errors per second (err./sec)"
		ERRORS_COUNT,                           // "errors-count" -> "Total Errors"
		
		PERC_TRANSACTION_RESP_TIME ("ms", "s"), // "perc-transaction-resp-time" -> "Percentile Transaction response time" for Transactions.
		
		THROUGHPUT ("MB");                      // "throughput" -> "Total throughput (MBytes)"
		
		private final Set<String> acceptedUnits = new HashSet<>();
				
		private KeyPerformanceIndicator(final String... units) {
			acceptedUnits.addAll(Arrays.asList(units));
		}
		
		public String friendlyName() {
			return name().toLowerCase().replace('_', '-');
		}		
		
		public double toValue(final String value, final String unit) {
			if (Strings.isNullOrEmpty(unit)) {
				return Double.valueOf(value);
			}
			if (acceptedUnits.contains(unit)) {
				double convertedValue = Double.valueOf(value);
				if (unit.equals("ms")) {
					convertedValue = convertedValue / 1000;
				}
				return convertedValue;
			}
			throw new IllegalArgumentException(friendlyName() + " does not accept this unit: " + unit + ". Possible units: " + acceptedUnits + ".");
		}
		
		public static KeyPerformanceIndicator of(final String name) {
			if (Strings.isNullOrEmpty(name)) {
				throw new IllegalArgumentException("The parameter 'name' must not be null or empty.");
			}
			
			try {
				return KeyPerformanceIndicator.valueOf(name.toUpperCase().replace('-', '_'));
			}
			catch (final IllegalArgumentException iae) {
				throw new IllegalArgumentException("The parameter 'name' must be: " + KeyPerformanceIndicator.values() +".");	
			}    		
		}
	}
	
	enum Scope {
		ON_TEST {
			@Override
			public Set<KeyPerformanceIndicator> getKeyPerformanceIndicators() {
				return Sets.newHashSet(
					KeyPerformanceIndicator.AVG_REQUEST_RESP_TIME,
					KeyPerformanceIndicator.AVG_PAGE_RESP_TIME,
					KeyPerformanceIndicator.AVG_TRANSACTION_RESP_TIME,
					KeyPerformanceIndicator.PERC_TRANSACTION_RESP_TIME,
					KeyPerformanceIndicator.AVG_REQUEST_PER_SEC,
					KeyPerformanceIndicator.AVG_THROUGHPUT_PER_SEC,
					KeyPerformanceIndicator.THROUGHPUT,
					KeyPerformanceIndicator.COUNT,
					KeyPerformanceIndicator.ERRORS_COUNT,
					KeyPerformanceIndicator.ERROR_RATE
				);
			}
		},
		ON_INTERVAL {
			@Override
			public Set<KeyPerformanceIndicator> getKeyPerformanceIndicators() {
				return Sets.newHashSet(
					KeyPerformanceIndicator.AVG_RESP_TIME,
					KeyPerformanceIndicator.AVG_ELT_PER_SEC,
					KeyPerformanceIndicator.AVG_THROUGHPUT_PER_SEC,
					KeyPerformanceIndicator.ERRORS_PER_SEC,
					KeyPerformanceIndicator.ERROR_RATE
				);
			}
		};
		
		public abstract Set<KeyPerformanceIndicator> getKeyPerformanceIndicators();
		
		public static Scope of(final String name) {
			if (Strings.isNullOrEmpty(name)) {
				throw new IllegalArgumentException("The parameter 'name' must not be null or empty.");
			}
			
			try {
				return Scope.valueOf(name.toUpperCase().replace(' ', '_'));
			}
			catch (final IllegalArgumentException iae) {
				throw new IllegalArgumentException("The parameter 'name' must be: 'on test' or 'on interval'.");	
			}    		
		}
		
		public String friendlyName() {
			return name().toLowerCase().replace('_', ' ');
		}
	}
	
	Scope DEFAULT_SCOPE = Scope.ON_TEST;
	Integer DEFAULT_PERCENT = 90;
		
	@RequiredCheck(groups = {NeoLoad.class})
	KeyPerformanceIndicator getKeyPerformanceIndicator();

	@RangeCheck(min = 0, max = 100, groups = {NeoLoad.class})
	Optional<Integer> getPercent();

	@RequiredCheck(groups = {NeoLoad.class})
	@Valid
	List<SlaThresholdCondition> getConditions();
	
	@RequiredCheck(groups = {NeoLoad.class})
	@Value.Default
	default Scope getScope() {
		return DEFAULT_SCOPE;
	}
	
	class Builder extends ImmutableSlaThreshold.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
