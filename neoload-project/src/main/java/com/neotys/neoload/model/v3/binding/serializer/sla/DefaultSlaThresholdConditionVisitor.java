package com.neotys.neoload.model.v3.binding.serializer.sla;

import com.neotys.neoload.model.v3.binding.serializer.sla.SlaThresholdParser.UnitContext;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KeyPerformanceIndicator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Operator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;

final class DefaultSlaThresholdConditionVisitor extends SlaThresholdBaseVisitor<SlaThresholdCondition> {
	
	private final KeyPerformanceIndicator kpi;
	
	protected DefaultSlaThresholdConditionVisitor(final KeyPerformanceIndicator kpi) {
		super();
		
		this.kpi = kpi;
	}

	@Override 
	public SlaThresholdCondition visitCondition(SlaThresholdParser.ConditionContext ctx) {
		final Severity severity = Severity.of(ctx.severity().getText());
		final Operator operator = Operator.of(ctx.operator().getText());
		final String value = ctx.value().getText();
		final UnitContext unitCtx = ctx.unit();
		final String unit = unitCtx != null ? unitCtx.getText() : null;
		
		final SlaThresholdCondition condition = SlaThresholdCondition.builder()
				.severity(severity)
				.operator(operator)
				.value(kpi.toValue(value, unit))
				.build();
		return condition;
	}		
}
