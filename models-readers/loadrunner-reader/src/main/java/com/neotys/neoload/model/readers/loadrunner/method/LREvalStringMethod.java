package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.Either;
import com.neotys.neoload.model.repository.ImmutableEvalString;
import com.neotys.neoload.model.repository.ImmutableVariableName;
import com.neotys.neoload.model.repository.VariableName;

public class LREvalStringMethod implements LoadRunnerMethod {
	
	private static int counter = 1;

	public LREvalStringMethod() {
		super();
	}

	@Override
	public Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		Preconditions.checkNotNull(method.getParameters(), method.getName() + " method must have a parameter");
		if(method.getParameters().isEmpty()){
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, method.getName() + " method must have a parameter");
			return null;
		} 		
		visitor.readSupportedFunction(method.getName(), ctx);		
		final List<Either<String, VariableName>> content = parseContent(method.getParameters().get(0), visitor.getLeftBrace(), visitor.getRightBrace());		
		final String variableName = "eval_string_" + counter++;
		return ImmutableEvalString.builder().name(variableName).variableName(variableName).content(content).build();
	}

	private static List<Either<String, VariableName>> parseContent(String content, final String leftBrace, final String rightBrace) {
		final List<Either<String, VariableName>> result = new ArrayList<>();
		if(Strings.isNullOrEmpty(content)){
			return result;
		}
		if(content.startsWith("\"") && content.endsWith("\"")){
			content = content.substring(1, content.length()-1);
		}	
		if(content.startsWith("${") && content.endsWith("}")){
			// NL Variable
			content = content.substring(2, content.length()-1);
			result.add(Either.right(ImmutableVariableName.builder().name(content).build()));
			return result;
		}	
		final String regExp = "(?=(?!^)\\" + leftBrace + ")|(?<=\\" + leftBrace + ")|(?=(?!^)\\" + rightBrace + ")|(?<=\\" + rightBrace + ")";
		final Iterator<String> it = Arrays.asList(content.split(regExp)).iterator();
		boolean isVariable = false;
		while(it.hasNext()){
			final String element = it.next();
			if(leftBrace.equals(element)){
				isVariable = true;
			} else if(rightBrace.equals(element)){
				isVariable = false;
			} else {
				if(isVariable){
					result.add(Either.right(ImmutableVariableName.builder().name(element).build()));
				} else {
					result.add(Either.left(element));
				}
			}
		}
		return result;
	}
}
