package com.neotys.neoload.model.readers.loadrunner.selectionstatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.tree.ParseTree;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14BaseVisitor;
import com.neotys.neoload.model.parsers.CPP14Parser.SelectionstatementContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.Condition;
import com.neotys.neoload.model.repository.Conditions;
import com.neotys.neoload.model.repository.ContainerForMulti;
import com.neotys.neoload.model.repository.CustomAction;
import com.neotys.neoload.model.repository.CustomActionParameter;
import com.neotys.neoload.model.repository.IfThenElse;
import com.neotys.neoload.model.repository.ImmutableCondition;
import com.neotys.neoload.model.repository.ImmutableConditions;
import com.neotys.neoload.model.repository.ImmutableContainerForMulti;
import com.neotys.neoload.model.repository.ImmutableIfThenElse;

public class SelectionStatementVisitor extends CPP14BaseVisitor<Element> {

	private static final String LR_METHOD_IF = "if";
	private static final String NL_IF_ACTION_NAME = "condition";
	private static final String NAME_THEN = "Then";
	private static final String NAME_ELSE = "Else";
	private static final String TAG_THEN = "then-container";
	private static final String TAG_ELSE= "else-container";
	private static final String CUSTOM_ACTION_VARIABLE_PARAMETER = "variable";

	private final LoadRunnerVUVisitor visitor;

	public SelectionStatementVisitor(final LoadRunnerVUVisitor visitor) {
		this.visitor = visitor;
	}

	@Override
	public Element visitSelectionstatement(SelectionstatementContext selectionstatementContext) {
		if(selectionstatementContext.getChildCount() == 0){
			return super.visitSelectionstatement(selectionstatementContext);
		}
		final String methodName = selectionstatementContext.getChild(0).getText();
		if(LR_METHOD_IF.equals(methodName)){
			return handleIf(selectionstatementContext);
		}
		return super.visitSelectionstatement(selectionstatementContext);
	}

	private Element handleIf(SelectionstatementContext selectionstatementContext) {
		final IfThenElse ifThenElse = ImmutableIfThenElse.builder()
			.name(NL_IF_ACTION_NAME)
			.conditions(readConditions(selectionstatementContext))
			.then(readThen(selectionstatementContext))
			.getElse(readElse(selectionstatementContext))
			.build();
		visitor.addInContainers(ifThenElse);
		return ifThenElse;
	}

	private Conditions readConditions(final SelectionstatementContext selectionstatementContext) {
		final ParseTree conditionTree = selectionstatementContext.getChild(2);
		final Element element = conditionTree.accept(new ConditionContextVisitor(visitor));
		final Optional<String> conditionString = extractCondition(conditionTree);
		if(element == null){
			visitor.readSupportedFunctionWithWarn(LR_METHOD_IF, selectionstatementContext.getParent(), "Condition not supported " + conditionString.orElse(""));
		} else {
			visitor.readSupportedFunction(LR_METHOD_IF, selectionstatementContext.getParent());
		}
		final String operand1;
		if(element instanceof CustomAction){
			operand1 = getVariableSyntax((CustomAction)element);
		} else {
			operand1 = extractBooleanCondition(conditionTree);
		}
		final Condition condition = ImmutableCondition.builder()
				.operand1(operand1)
				.operator(Condition.Operator.EQUALS)
				.operand2(conditionString.orElse("").startsWith("!") ? Boolean.FALSE.toString() : Boolean.TRUE.toString())
				.build();
		final ImmutableConditions.Builder conditionsBuilder = ImmutableConditions.builder();
		conditionsBuilder.addConditions(condition);
		conditionsBuilder.description(conditionString);
		conditionsBuilder.matchType(Conditions.MatchType.ANY);
		return conditionsBuilder.build();
	}

	private static String extractBooleanCondition(final ParseTree conditionTree) {
		if(conditionTree.getChildCount() == 0){
			return "true";
		}
		final ParseTree tree = conditionTree.getChild(0);
		if(tree == null){
			return "true";
		}
		if("false".equals(tree.getText())){
			return "false";
		}
		return "true";
	}

	private static Optional<String> extractCondition(final ParseTree conditionTree) {
		if(conditionTree.getChildCount() == 0){
			return Optional.empty();
		}
		final ParseTree tree = conditionTree.getChild(0);
		if(tree == null){
			return Optional.empty();
		}
		return Optional.ofNullable(tree.getText());
	}

	private ContainerForMulti readThen(SelectionstatementContext selectionstatementContext) {
		final ImmutableContainerForMulti.Builder builder = ImmutableContainerForMulti.builder().name(NAME_THEN).tag(TAG_THEN);
		visitor.getCurrentContainers().add(builder);
		selectionstatementContext.getChild(4).accept(new StatementContextVisitor(visitor, builder));
		// End of a statement
		// We need to close all transactions currently opened
		// because NL does not support starting transaction in statement, and closing it outside of statement.
		while(visitor.getCurrentContainers().get(visitor.getCurrentContainers().size() - 1) != builder){
			visitor.closeContainer();
		}
		visitor.getCurrentContainers().remove(visitor.getCurrentContainers().size() - 1);
		return builder.build();
	}

	private ContainerForMulti readElse(SelectionstatementContext selectionstatementContext) {
		final List<Element> elseElements = new ArrayList<>();
		final ImmutableContainerForMulti.Builder builder = ImmutableContainerForMulti.builder().name(NAME_ELSE).tag(TAG_ELSE);
		visitor.getCurrentContainers().add(builder);
		if(selectionstatementContext.getChildCount() > 6){
			elseElements.addAll(selectionstatementContext.getChild(6).accept(new StatementContextVisitor(visitor, builder)));
		}
		// End of a statement
		// We need to close all transactions currently opened
		// because NL does not support starting transaction in statement, and closing it outside of statement.
		while(visitor.getCurrentContainers().get(visitor.getCurrentContainers().size() - 1) != builder){
			visitor.closeContainer();
		}
		visitor.getCurrentContainers().remove(visitor.getCurrentContainers().size() - 1);
		return builder.build();
	}

	private static String getVariableSyntax(final CustomAction customAction) {
		for(final CustomActionParameter parameter : customAction.getParameters()){
			if(CUSTOM_ACTION_VARIABLE_PARAMETER.equals(parameter.getName())){
				return MethodUtils.getVariableSyntax(parameter.getValue());
			}
		}
		return "";
	}
}
