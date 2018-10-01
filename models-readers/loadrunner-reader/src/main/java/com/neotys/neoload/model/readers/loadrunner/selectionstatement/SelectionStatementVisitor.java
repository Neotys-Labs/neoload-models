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
import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.repository.CustomAction;
import com.neotys.neoload.model.repository.CustomActionParameter;
import com.neotys.neoload.model.repository.IfThenElse;
import com.neotys.neoload.model.repository.ImmutableCondition;
import com.neotys.neoload.model.repository.ImmutableConditions;
import com.neotys.neoload.model.repository.ImmutableContainer;
import com.neotys.neoload.model.repository.ImmutableIfThenElse;

public class SelectionStatementVisitor extends CPP14BaseVisitor<Element> {

	private static final String LR_METHOD_IF = "if";
	private static final String NL_IF_ACTION_NAME = "condition";
	private static final String NL_THEN_CONTAINER_NAME = "Then";
	private static final String NL_ELSE_CONTAINER_NAME = "Else";
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
		final Optional<String> description = extractDescription(conditionTree);
		if(element == null){
			visitor.readSupportedFunctionWithWarn(LR_METHOD_IF, selectionstatementContext.getParent(), "Condition not supported " + description.orElse(""));
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
				.operand2("true")
				.build();		
		final ImmutableConditions.Builder conditionsBuilder = ImmutableConditions.builder();
		conditionsBuilder.addConditions(condition);
		conditionsBuilder.description(description);
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

	private static Optional<String> extractDescription(final ParseTree conditionTree) {
		if(conditionTree.getChildCount() == 0){
			return Optional.empty();
		}
		final ParseTree tree = conditionTree.getChild(0);
		if(tree == null){
			return Optional.empty();
		}
		return Optional.ofNullable(tree.getText());
	}

	private Container readThen(SelectionstatementContext selectionstatementContext) {
		final List<Element> thenElements = selectionstatementContext.getChild(4).accept(new StatementContextVisitor(visitor, NL_THEN_CONTAINER_NAME));
		return ImmutableContainer.builder().name(NL_THEN_CONTAINER_NAME).addAllChilds(thenElements).build();
	}
	
	private Container readElse(SelectionstatementContext selectionstatementContext) {
		final List<Element> elseElements = new ArrayList<>();
		if(selectionstatementContext.getChildCount() > 6){
			elseElements.addAll(selectionstatementContext.getChild(6).accept(new StatementContextVisitor(visitor, NL_ELSE_CONTAINER_NAME)));			
		}		
		return ImmutableContainer.builder().name(NL_ELSE_CONTAINER_NAME).addAllChilds(elseElements).build();
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
