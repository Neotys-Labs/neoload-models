package com.neotys.neoload.model.v3.validation.constraintvalidators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KeyPerformanceIndicator;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.Scope;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;
import com.neotys.neoload.model.v3.validation.constraints.SlaThresholdCheck;



public final class SlaThresholdValidator extends AbstractConstraintValidator<SlaThresholdCheck, SlaThreshold> {
	protected SlaThresholdCheck.UsageType usage;
	
	@Override
	public void initialize(final SlaThresholdCheck constraintAnnotation) {
		this.usage = constraintAnnotation.usage();
	}

	@Override
	public boolean isValid(final SlaThreshold threshold, final ConstraintValidatorContext context) {
		// null value is valid
		if (threshold == null) {
			return true;
		}

		switch (usage) {
			case CHECK_RELATIONSHIP_KPI_AND_SCOPE: {
				return checkRelationShipKpiAndScope(threshold);
			}
			case CHECK_UNIQUE_CONDITION_SEVERITY: {
				return checkUniqueConditionSeverity(threshold);
			}
		}
		return false;
	}
	
	private static boolean checkRelationShipKpiAndScope(final SlaThreshold threshold) {
		final Scope scope = threshold.getScope();
		final KeyPerformanceIndicator kpi = threshold.getKeyPerformanceIndicator();
		if (kpi == null) { // null value is valid
			return true;
		}
		return scope.getKeyPerformanceIndicators().contains(kpi);
	}
	
	private static boolean checkUniqueConditionSeverity(final SlaThreshold threshold) {
		final Set<Severity> uniqueSeverities = new HashSet<>();
		int nullCount = 0;
		int total = 0;
		final List<SlaThresholdCondition> conditions = threshold.getConditions();
		if ((conditions != null) && (!conditions.isEmpty())) {
			total = conditions.size();
			for (final SlaThresholdCondition condition: conditions) {
				final Severity severity = condition.getSeverity();
				if (severity != null) {
					uniqueSeverities.add(severity);
				}
				else {
					nullCount = nullCount + 1;
				}
			}
		}
		return ((uniqueSeverities.size() + nullCount) == total);
	}
}
