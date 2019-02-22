package com.neotys.neoload.model.v3.validation.constraintvalidators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KPI;
import com.neotys.neoload.model.v3.validation.constraints.SlaThresholdsCheck;



public final class SlaThresholdsValidator extends AbstractConstraintValidator<SlaThresholdsCheck, List<SlaThreshold>> {
	protected SlaThresholdsCheck.UsageType usage;
	protected Class<? extends Element> from;
	
	@Override
	public void initialize(final SlaThresholdsCheck constraintAnnotation) {
		this.usage = constraintAnnotation.usage();
		this.from = constraintAnnotation.from();
	}

	@Override
	public boolean isValid(final List<SlaThreshold> thresholds, final ConstraintValidatorContext context) {
		// null or empty value are valid
		if ((thresholds == null) ) {
			return true;
		}

		if (usage == SlaThresholdsCheck.UsageType.CHECK_UNIQUE_KPI_AND_SCOPE) {
			return checkUniqueKpiAndScope(thresholds);
		}
		if (usage == SlaThresholdsCheck.UsageType.CHECK_LIST_OF_KPIS_FROM_ELEMENT) {
			return checkListOfKpisFromElement(thresholds, from);
		}
		return false;
	}
	
	private static boolean checkUniqueKpiAndScope(final List<SlaThreshold> thresholds) {
		final Set<String> uniqueKpisAndScopes = new HashSet<>();
		int nullCount = 0;
		int total = thresholds.size();
		for (final SlaThreshold threshold: thresholds) {
			final KPI kpi = threshold.getKpi();
			if (kpi != null) {
				uniqueKpisAndScopes.add(kpi.name() + ' ' + threshold.getScope());
			}
			else {
				nullCount = nullCount + 1;
			}
		}
		return ((uniqueKpisAndScopes.size() + nullCount) == total);
	}

	private static boolean checkListOfKpisFromElement(final List<SlaThreshold> thresholds, final Class<? extends Element> from) {
		return true;
	}
}
