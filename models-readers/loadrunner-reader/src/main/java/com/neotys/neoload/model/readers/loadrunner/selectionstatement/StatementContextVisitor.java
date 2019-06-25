package com.neotys.neoload.model.readers.loadrunner.selectionstatement;

import java.util.Collections;
import java.util.List;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14BaseVisitor;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.parsers.CPP14Parser.SelectionstatementContext;
import com.neotys.neoload.model.parsers.CPP14Parser.StatementContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.repository.ImmutableContainerForMulti;

public class StatementContextVisitor extends CPP14BaseVisitor<List<Element>> {

	private final LoadRunnerVUVisitor visitor;
	private final ImmutableContainerForMulti.Builder builder;

	public StatementContextVisitor(final LoadRunnerVUVisitor visitor, final ImmutableContainerForMulti.Builder builder) {
		super();
		this.visitor = visitor;
		this.builder = builder;
	}

	@Override
	public List<Element> visitStatement(StatementContext ctx) {
		super.visitStatement(ctx);
		return builder.build().getChilds();
	}

	@Override
	public List<Element> visitMethodcall(MethodcallContext ctx) {
		visitor.visitMethodcall(ctx);
		return Collections.emptyList();
	}

	@Override
	public List<Element> visitSelectionstatement(final SelectionstatementContext selectionstatementContext) {
		visitor.getCurrentContainers().add(builder);
		visitor.visitSelectionstatement(selectionstatementContext);
		visitor.getCurrentContainers().remove(visitor.getCurrentContainers().size() - 1);
		return Collections.emptyList();
	}
}
