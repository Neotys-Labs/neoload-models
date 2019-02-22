package com.neotys.neoload.model.v3.binding.serializer.sla;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.neotys.neoload.model.v3.binding.serializer.sla.SlaThresholdParser.KpiContext;
import com.neotys.neoload.model.v3.binding.serializer.sla.SlaThresholdParser.PercentileContext;
import com.neotys.neoload.model.v3.binding.serializer.sla.SlaThresholdParser.ScopeContext;
import com.neotys.neoload.model.v3.binding.serializer.sla.SlaThresholdParser.ThresholdContext;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KPI;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.Scope;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;

final class DefaultSlaThresholdVisitor extends SlaThresholdBaseVisitor<SlaThreshold> {

	protected DefaultSlaThresholdVisitor() {
		super();
	}

	@Override 
	public SlaThreshold visitThreshold(final ThresholdContext ctx) { 
		final KpiContext pkiContext = ctx.kpi();
		final KPI kpi = KPI.of(pkiContext.getText());
				
		Integer percent = null;
		if (kpi == KPI.PERC_TRANSACTION_RESP_TIME) {
			final PercentileContext percentileContext = ctx.percentile();
			percent = (percentileContext != null) ? Integer.valueOf(percentileContext.INTEGER().getText()) : SlaThreshold.DEFAULT_PERCENT;
		}
		
		final DefaultSlaThresholdConditionVisitor conditionVisitor = new DefaultSlaThresholdConditionVisitor(kpi);
		final List<SlaThresholdCondition> conditions = ctx.condition()
                .stream()
                .map(condition -> condition.accept(conditionVisitor))
                .collect(Collectors.toList());
		
		final ScopeContext scopeContext = ctx.scope();
		final Scope scope = (scopeContext != null) ? Scope.of(scopeContext.getText()) : SlaThreshold.DEFAULT_SCOPE;
		
		return SlaThreshold.builder()
				.kpi(kpi)
				.percent(Optional.ofNullable(percent))
				.conditions(conditions)
				.scope(scope)
				.build();		
	}
}
