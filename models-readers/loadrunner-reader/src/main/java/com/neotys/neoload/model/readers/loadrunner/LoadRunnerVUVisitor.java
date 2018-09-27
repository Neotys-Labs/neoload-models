package com.neotys.neoload.model.readers.loadrunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.Token;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.function.Function;
import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.parsers.CPP14BaseVisitor;
import com.neotys.neoload.model.parsers.CPP14Parser;
import com.neotys.neoload.model.parsers.CPP14Parser.ConditionContext;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.parsers.CPP14Parser.SelectionstatementContext;
import com.neotys.neoload.model.parsers.CPP14Parser.StatementContext;
import com.neotys.neoload.model.readers.loadrunner.customaction.ImmutableMappingMethod;
import com.neotys.neoload.model.readers.loadrunner.method.LoadRunnerMethod;
import com.neotys.neoload.model.repository.Condition;
import com.neotys.neoload.model.repository.Conditions;
import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.repository.CustomAction;
import com.neotys.neoload.model.repository.CustomActionParameter;
import com.neotys.neoload.model.repository.EvalString;
import com.neotys.neoload.model.repository.Header;
import com.neotys.neoload.model.repository.ImmutableCondition;
import com.neotys.neoload.model.repository.ImmutableConditions;
import com.neotys.neoload.model.repository.ImmutableContainer;
import com.neotys.neoload.model.repository.ImmutableIfThenElse;
import com.neotys.neoload.model.repository.Page;
import com.neotys.neoload.model.repository.Request;
import com.neotys.neoload.model.repository.Validator;
import com.neotys.neoload.model.repository.VariableExtractor;

public class LoadRunnerVUVisitor extends CPP14BaseVisitor<List<Element>> {

	private final ImmutableContainer.Builder mainContainer;
	private final List<ImmutableContainer.Builder> currentContainers = new ArrayList<>();
	private List<VariableExtractor> currentExtractors;
	private List<Validator> currentValidators;
	private List<Header> currentHeaders;
	private List<Header> globalHeaders;
	private final String leftBrace;
	private final String rightBrace;
	private final LoadRunnerReader reader;
	private final EventListener eventListener;
	private Optional<Request> currentRequest = Optional.empty();
	
	public LoadRunnerVUVisitor(final LoadRunnerReader reader, final String leftBrace, final String rightBrace, final String containerName) {
		this.mainContainer = ImmutableContainer.builder().name(containerName);
		this.currentContainers.clear();
		this.currentContainers.add(mainContainer);
		this.currentExtractors = new ArrayList<>();
		this.currentValidators = new ArrayList<>();
		this.currentHeaders = new ArrayList<>();
		this.globalHeaders = new ArrayList<>();
		this.leftBrace = leftBrace;
		this.rightBrace = rightBrace;
		this.reader = reader;
		this.eventListener = reader.getEventListener();	
	}

	@Override
	public List<Element> visitMethodcall(CPP14Parser.MethodcallContext ctx) {
		String methodName = ctx.Identifier().getText();
		ImmutableMethodCall.Builder methodBuilder = ImmutableMethodCall.builder().name(methodName);
		ParametersVisitor paramsVisitor = new ParametersVisitor();
		if (ctx.expressionlist() != null) {
			List<String> params = ctx.expressionlist().accept(paramsVisitor);
			methodBuilder.addAllParameters(params);
		}
		final MethodCall method = methodBuilder.build();
		final LoadRunnerMethod lrMethod = reader.getLrSupportedMethod(method.getName());
		if(lrMethod == null){
			readUnsupportedFunction(method.getName(), ctx);
			return Collections.emptyList();
		}
		final List<Element> elements = lrMethod.getElement(this, method, ctx);
		for(final Element element: elements){
			if(!(element instanceof EvalString)){
				addInCurrentContainer(element);
			}
		}		
		return elements;
	}

	@Override
	public List<Element> visitSelectionstatement(final SelectionstatementContext selectionstatementContext) {
		
		final String methodName = selectionstatementContext.getChild(0).getText();
		if(!"if".equals(methodName)){
			return super.visitSelectionstatement(selectionstatementContext);
		}
		final ImmutableIfThenElse.Builder ifThenElseBuilder = ImmutableIfThenElse.builder().name(methodName);
		
		final Element condition = selectionstatementContext.getChild(2).accept(new ConditionContextVisitor());
		final ImmutableConditions.Builder conditionsBuilder = ImmutableConditions.builder();
		if(condition instanceof CustomAction){
			conditionsBuilder.addConditions(ImmutableCondition.builder()
					.operand1(getVariableName((CustomAction)condition))
					.operator(Condition.Operator.EQUALS)
					.operand2("true")
					.build())
				.matchType(Conditions.MatchType.ANY)
				.build();			
		}	
		ifThenElseBuilder.conditions(conditionsBuilder.build());
		final List<Element> thenElements = selectionstatementContext.getChild(4).accept(new StatementContextVisitor("Then"));
		final Container thenContainer = ImmutableContainer.builder().name("Then").addAllChilds(thenElements).build();
		ifThenElseBuilder.then(thenContainer);
		final List<Element> elseElements = new ArrayList<>();
		if(selectionstatementContext.getChildCount() > 6){
			elseElements.addAll(selectionstatementContext.getChild(6).accept(new StatementContextVisitor("Else")));			
		}		
		final Container elseContainer = ImmutableContainer.builder().name("Else").addAllChilds(elseElements).build();
		ifThenElseBuilder.getElse(elseContainer);		
		addInCurrentContainer(ifThenElseBuilder.build());
		return Collections.emptyList();		
	}
	
	private class ConditionContextVisitor extends CPP14BaseVisitor<Element> {

		@Override
		public Element visitCondition(final ConditionContext ctx) {
			final Element element = ctx.expression().accept(this);
			return element;
		}
		
		@Override
		public Element visitMethodcall(MethodcallContext ctx) {
			final Element element = LoadRunnerVUVisitor.this.visitMethodcall(ctx).get(0);
			return element;
		}
	}
	
	private class StatementContextVisitor extends CPP14BaseVisitor<List<Element>> {
		
		private final ImmutableContainer.Builder currentContainer;
		
		public StatementContextVisitor(final String name){
			super();
			this.currentContainer = ImmutableContainer.builder().name(name);
		}
		
		@Override
		public List<Element> visitStatement(StatementContext ctx) {
			super.visitStatement(ctx);
			return currentContainer.build().getChilds();
		}
				
		@Override
		public List<Element> visitMethodcall(MethodcallContext ctx) {			 
			currentContainers.add(currentContainer);
			LoadRunnerVUVisitor.this.visitMethodcall(ctx);
			currentContainers.remove(currentContainers.size()-1);
			return Collections.emptyList();
		}
	}
		
	private static String getVariableName(final CustomAction customAction) {
		for(final CustomActionParameter parameter : customAction.getParameters()){
			if("variable".equals(parameter.getName())){
				return parameter.getValue();
			}
		}
		return "";
	}

	@Override
	protected List<Element> aggregateResult(final List<Element> aggregate, final List<Element> nextResult) {
		return ImmutableList.of(currentContainers.get(0).build());
	}
	
	public void addInCurrentContainer(Element element){
		if(element!=null){
			element = setUniqueNameInContainer(element,	currentContainers.get(currentContainers.size() - 1).build().getChilds());
			currentContainers.get(currentContainers.size() - 1).addChilds(element);
		}
	}

	static Element setUniqueNameInContainer(final Element element, final List<Element> childs) {
		if (element == null)
			return element;
		int i = 0;
		Element elementWithUniqueName = element;
		while (!isUniqueInContainer(elementWithUniqueName, childs)) {
			elementWithUniqueName = element.withName(element.getName() + "_" + (++i));
		}
		return elementWithUniqueName;
	}

	private static boolean isUniqueInContainer(final Element element, final List<Element> childs) {
		return childs.stream().filter(element1 -> element1.getName().equals(element.getName())).count() == 0;
	}

	private class ParametersVisitor extends CPP14BaseVisitor<List<String>> {

		@Override
		public List<String> visitExpressionlist(CPP14Parser.ExpressionlistContext ctx) {
			InitializeListVisitor listVisitor = new InitializeListVisitor();
			return ctx.initializerlist().accept(listVisitor);
		}
	}
	
	private class InitializeListVisitor extends CPP14BaseVisitor<List<String>> {

		private static final String PARAMETER_SEPARATOR = ",";

		@Override
		public List<String> visitInitializerlist(CPP14Parser.InitializerlistContext ctx) {
			return ctx.children.stream().flatMap(parseTree -> {
				InitializeListVisitor listVisitor = new InitializeListVisitor();
				// if a child is an InitializerList then append all the childs leaf as a new parameter
				List<String> params = parseTree.accept(listVisitor);
				if (params == null) {
					params = new ArrayList<>();
					while (parseTree.getChildCount() > 0) {
						parseTree = parseTree.getChild(0);
					}
					if (!PARAMETER_SEPARATOR.equals(parseTree.getText())) {
						params.add(parseTree.getText());
					}
				}
				return params.stream();
			}).collect(Collectors.toList());
		}
		
		@Override
		public List<String> visitMethodcall(MethodcallContext ctx) {
			final List<Element> elements = LoadRunnerVUVisitor.this.visitMethodcall(ctx);
			if(!elements.isEmpty() && (elements.get(0) instanceof Function)){
				return ImmutableList.of(((Function)elements.get(0)).getReturnValue());				
			}
			return ImmutableList.of();
		}		
	}
		
	public LoadRunnerReader getReader() {
		return reader;
	}
	
	public String getLeftBrace() {
		return leftBrace;
	}
	
	public String getRightBrace() {
		return rightBrace;
	}
	
	public List<VariableExtractor> getCurrentExtractors() {
		return currentExtractors;
	}
	
	public List<Validator> getCurrentValidators() {
		return currentValidators;
	}
	
	public List<Header> getCurrentHeaders() {
		return currentHeaders;
	}
	
	public List<Header> getGlobalHeaders() {
		return globalHeaders;
	}
	
	public static int getLineNumber(final CPP14Parser.MethodcallContext ctx) {
		final Token token = ctx.getStart();
		if (token == null) {
			return 0;
		}
		return token.getLine();
	}
	
	public List<ImmutableContainer.Builder> getCurrentContainers() {
		return currentContainers;
	}

	public void readSupportedFunction(final String functionName, final CPP14Parser.MethodcallContext ctx) {
		eventListener.readSupportedFunction(getCurrentScriptName(), functionName, LoadRunnerVUVisitor.getLineNumber(ctx));		
	}
	
	public void readSupportedFunctionWithWarn(final String functionName, final CPP14Parser.MethodcallContext ctx, final String warning) {
		eventListener.readSupportedFunctionWithWarn(getCurrentScriptName(), functionName, LoadRunnerVUVisitor.getLineNumber(ctx), warning);		
	}
	
	public void readUnsupportedFunction(final String functionName, final CPP14Parser.MethodcallContext ctx) {
		eventListener.readUnsupportedFunction(getCurrentScriptName(), functionName, LoadRunnerVUVisitor.getLineNumber(ctx));		
	}
	
	private String getCurrentScriptName(){
		return reader.getCurrentScriptName();
	}
	
	public Optional<Request> getCurrentRequest() {
		return currentRequest;
	}
	
	public void setCurrentRequestFromPage(final Page currentPage) {
		if(currentPage!=null && !currentPage.getChilds().isEmpty()){
			for(final Element pageElement : currentPage.getChilds()){
				if(pageElement instanceof Request){
					this.currentRequest = Optional.of((Request)pageElement);
					return;
				}
			}			 			
		}
	}

	public ImmutableMappingMethod getCustomActionMappingMethod(final String methodName) {
		return reader.getCustomActionMappingMethod(methodName);
	}
}
