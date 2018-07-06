package com.neotys.neoload.model.readers.loadrunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.parsers.CPP14BaseVisitor;
import com.neotys.neoload.model.parsers.CPP14Parser;
import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.repository.ImmutableContainer;
import com.neotys.neoload.model.repository.Validator;
import com.neotys.neoload.model.repository.VariableExtractor;
import org.antlr.v4.runtime.Token;

import com.neotys.neoload.model.readers.loadrunner.method.LoadRunnerMethod;
import com.neotys.neoload.model.readers.loadrunner.method.LoadRunnerSupportedMethods;

public class LoadRunnerVUVisitor extends CPP14BaseVisitor<Element> {

	private final ImmutableContainer.Builder mainContainer;
	private final List<ImmutableContainer.Builder> currentContainers = new ArrayList<>();
	private List<VariableExtractor> currentExtractors;
	private List<Validator> currentValidators;
	private final String leftBrace;
	private final String rightBrace;
	private final LoadRunnerReader reader;
	private final EventListener eventListener;

	public LoadRunnerVUVisitor(final LoadRunnerReader reader, final String leftBrace, final String rightBrace, final String containerName) {
		this.mainContainer = ImmutableContainer.builder().name(containerName);
		this.currentContainers.clear();
		this.currentContainers.add(mainContainer);
		this.currentExtractors = new ArrayList<>();
		this.currentValidators = new ArrayList<>();
		this.leftBrace = leftBrace;
		this.rightBrace = rightBrace;
		this.reader = reader;
		this.eventListener = reader.getEventListener();
	}

	@Override
	public Element visitMethodcall(CPP14Parser.MethodcallContext ctx) {
		String methodName = ctx.Identifier().getText();
		ImmutableMethodCall.Builder methodBuilder = ImmutableMethodCall.builder().name(methodName);
		ParametersVisitor paramsVisitor = new ParametersVisitor();
		if (ctx.expressionlist() != null) {
			List<String> params = ctx.expressionlist().accept(paramsVisitor);
			methodBuilder.addAllParameters(params);
		}
		final MethodCall method = methodBuilder.build();
		final LoadRunnerMethod lrMethod = LoadRunnerSupportedMethods.get(method.getName());
		final Element elt;
		if(lrMethod == null){
			readUnsupportedFunction(method.getName(), ctx);
			return null;
		}		
		elt = setUniqueNameInContainer(lrMethod.getElement(this, method, ctx),
				currentContainers.get(currentContainers.size() - 1).build());
		Optional.ofNullable(elt).ifPresent(element -> currentContainers.get(currentContainers.size() - 1).addChilds(element));
		return elt;
	}

	@Override
	protected Element aggregateResult(final Element aggregate, final Element nextResult) {
		return currentContainers.get(0).build();
	}

	private static Element setUniqueNameInContainer(final Element element, final Container container) {
		if (element == null)
			return element;
		int i = 0;
		Element elementWithUniqueName = element;
		while (!isUniqueInContainer(elementWithUniqueName, container)) {
			elementWithUniqueName = element.withName(element.getName() + "_" + (++i));
		}
		return elementWithUniqueName;
	}

	private static boolean isUniqueInContainer(final Element element, final Container container) {
		return container.getChilds().stream().filter(element1 -> element1.getName().equals(element.getName())).count() == 0;
	}

	private static class ParametersVisitor extends CPP14BaseVisitor<List<String>> {

		@Override
		public List<String> visitExpressionlist(CPP14Parser.ExpressionlistContext ctx) {
			InitializeListVisitor listVisitor = new InitializeListVisitor();
			return ctx.initializerlist().accept(listVisitor);
		}
	}

	private static class InitializeListVisitor extends CPP14BaseVisitor<List<String>> {

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
	
	public static final int getLineNumber(final CPP14Parser.MethodcallContext ctx) {
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
}
