package com.neotys.neoload.model.readers.loadrunner.selectionstatement;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14BaseVisitor;
import com.neotys.neoload.model.parsers.CPP14Parser.ConditionContext;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;

public class ConditionContextVisitor extends CPP14BaseVisitor<Element> {
	
	private final LoadRunnerVUVisitor visitor;
	
	public ConditionContextVisitor(final LoadRunnerVUVisitor visitor) {
		this.visitor = visitor;
	}

	@Override
	public Element visitCondition(final ConditionContext ctx) {
		return ctx.expression().accept(this);
	}
	
	@Override
	public Element visitMethodcall(MethodcallContext ctx) {
		return visitor.visitMethodcall(ctx).get(0);
	}	
}