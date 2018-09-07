package com.neotys.neoload.model.readers.loadrunner.customaction;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;

public class MappingValueUtil {

	protected static final String VARIABLE_RETURN_VALUE = "¤¤¤VariableReturnValue¤¤¤";
	private static final String ARGUMENT_REGEX_1 = "¤¤¤arg";
	private static final String ARGUMENT_REGEX_2 = "\\d+";
	private static final String ARGUMENT_REGEX_3 = "¤¤¤";
	private static final String ARGUMENT_REGEX = ARGUMENT_REGEX_1 + ARGUMENT_REGEX_2 + ARGUMENT_REGEX_3;
	private MappingValueUtil() {}

	public static String parseMappingValue(final LoadRunnerVUVisitor visitor, final List<String> inputParameters, final String value, final String methodName, final AtomicInteger counter, final Set<Integer> readIndex){
		if(VARIABLE_RETURN_VALUE.equals(value)){
			return methodName + "_" + counter.incrementAndGet();
		}
		final Pattern pattern = Pattern.compile(ARGUMENT_REGEX);
		final Matcher matcher = pattern.matcher(value);
		final StringBuffer result = new StringBuffer();
		while (matcher.find()) {
			final String text = matcher.group(0);
			final int argIndex = getArgIndex(text);
			if(argIndex < inputParameters.size()){
				readIndex.add(argIndex);
				final String parameterValue = MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), inputParameters.get(argIndex));
				if(value.equals(text)){
					return parameterValue;
				}
				matcher.appendReplacement(result, parameterValue);
			} else {
				matcher.appendReplacement(result, text);
			}			
		}
		matcher.appendTail(result);
		return result.toString();
	}
	
	public static int getArgIndex(final String content){
		try{
			return Integer.parseInt(content.substring(ARGUMENT_REGEX_1.length(), content.length() - ARGUMENT_REGEX_3.length()));
		} catch(final Exception e){}
		return 0;
	}
}
