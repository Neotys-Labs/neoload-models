package com.neotys.neoload.model.readers.loadrunner;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.repository.ImmutablePage;
import com.neotys.neoload.model.repository.ImmutableParameter;
import com.neotys.neoload.model.repository.ImmutableParameter.Builder;
import com.neotys.neoload.model.repository.Parameter;

public class MethodUtils {

	enum ITEM_BOUNDARY {
		EXTRARES,
		ITEMDATA,
		LAST
	}
	
	private static final String NL_VARIABLE_START = "${";
	private static final String NL_VARIABLE_END = "}";

	private static Map<String, String> variablesMapping = null;
	
	static Logger logger = LoggerFactory.getLogger(MethodUtils.class);

	private MethodUtils() {}

	public static Pattern getVariablePatternWithBrace(String leftBrace, String rightBrace) {
		return Pattern.compile("\\Q"+leftBrace+"\\E((?!\\Q"+rightBrace+"\\E).)+\\Q"+rightBrace+"\\E");
	}

	public static Optional<String> getParameterStartingWith(MethodCall method, String name) {
		return method.getParameters().stream().filter(s -> s.toLowerCase().startsWith(name.toLowerCase()) || s.toLowerCase().startsWith("\"" + name.toLowerCase())).findFirst();
	}

	protected static Optional<String> getParameterWithName(MethodCall method, String name) {
		return getParameterStartingWith(method, name+"=");
	}

	public static Optional<String> getParameterValueWithName(final String leftBrace, final String rightBrace, MethodCall method, String name) {
		Optional<String> parameter = getParameterWithName(method, name);
		return parameter.map(param -> MethodUtils.normalizeString(leftBrace, rightBrace, param)).map(s -> s.substring((name+"=").length()));
	}

	
	public static void setVariableMapping(Map<String, String> localVariablesMapping) {
		variablesMapping = localVariablesMapping;
	}
	
	/**
	 * function to get the corresponding name from LR parameter to neoload variable
	 * @param lrName
	 * @return the name that needs to be used in neoload parameters
	 */
	public static String getCorrespondingVariableNameForNL(String lrName) {
		if(variablesMapping == null) {
			return lrName;
		}
		String correspondingName = variablesMapping.get(lrName);
		if (correspondingName != null)
			return correspondingName;
		return lrName;
	}
	
	/**
	 * extract the EXTRARES or ITEMDATA part from a web url method
	 * @param parameters the parameters to extract the items from.
	 * @return a List of elements between the typeListName and the end boundary
	 */
	protected static Optional<List<String>> extractItemListAsStringList(final LoadRunnerVUVisitor visitor, List<String> parameters, final ITEM_BOUNDARY typeListName, final Optional<ImmutablePage.Builder> pageBuilder) {
		final boolean containsTypeListName = parameters.contains(typeListName.toString());
		pageBuilder.ifPresent(p -> p.isDynamic(!containsTypeListName));
		if(!containsTypeListName) {			
			return Optional.empty();
		}		
		// split the list to get only the part after the "typeListName"
		final List<String> result = parameters.subList(parameters.indexOf(typeListName.toString())+1, parameters.size())
				.stream().map(param -> MethodUtils.normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(), param)).collect(Collectors.toList());
		// compute last index (end boundaries can be EXTRARES, ITEMDATA, LAST)
		final MutableInt boundaryIndex = new MutableInt(result.size());
		ImmutableList.copyOf(ITEM_BOUNDARY.values()).forEach(itemBoundary -> boundaryIndex.setValue(result.indexOf(itemBoundary.toString())>=0 ? Math.min(boundaryIndex.intValue(), result.indexOf(itemBoundary.toString())) : boundaryIndex));

		return Optional.of(result.subList(0, boundaryIndex.getValue()));
	}
	
	public static String unquote(String param) {
		if (param.startsWith("\"") && param.endsWith("\"")) {
			return param.substring(1, param.length() - 1);
		}
		return param;
	}
	
	public static String getVariableSyntax(final String variableName) {
		if(variableName.startsWith(NL_VARIABLE_START) && variableName.endsWith(NL_VARIABLE_END)){
			return variableName;
		}
		return NL_VARIABLE_START + variableName + NL_VARIABLE_END;
	}
	
	public static String getVariableName(final String variableSyntax) {
		if(variableSyntax!=null && variableSyntax.startsWith(NL_VARIABLE_START) && variableSyntax.endsWith(NL_VARIABLE_END)){
			return variableSyntax.substring(NL_VARIABLE_START.length(), variableSyntax.length() - NL_VARIABLE_END.length());
		}
		return variableSyntax;
	}

	protected static String normalizeVariables(final String leftBrace, final String rightBrace, final String param) {
		if(param.startsWith(NL_VARIABLE_START) && param.endsWith(NL_VARIABLE_END)){
			return param;
		}
		final Matcher m = getVariablePatternWithBrace(leftBrace, rightBrace).matcher(param);
		final StringBuilder sb = new StringBuilder();
		int lastIndex = 0;
		while (m.find()) {
			String paramName = param.substring(m.start() + leftBrace.length(), m.end() - rightBrace.length());
			sb.append(param.substring(lastIndex, m.start())).append(NL_VARIABLE_START).append(getCorrespondingVariableNameForNL(paramName)).append(NL_VARIABLE_END);
			lastIndex = m.end();
		}
		if (lastIndex == 0) {
			return param;
		}
		return sb.append(param.substring(lastIndex)).toString();		
	}

	protected static String unescape(String param) {
		LookupTranslator transaltor = new LookupTranslator(
		                      new String[][] {
		                            {"\\\\", "\\"},
		                            {"\\\"", "\""},
		                            {"\\'", "'"},
		                            {"\\", ""}
		                      });
		return transaltor.translate(param);

	}

	public static String normalizeString(final String leftBrace, final String rightBrace, final String param) {
		if(param==null){
			return "";
		}		
		return normalizeVariables(leftBrace, rightBrace, unescape(unquote(param)));
	}


	public static String normalizeName(final String name) {
		if(name == null){
			return name;
		}
		return unquote(name.trim()).replaceAll("[^a-zA-Z_0-9 \\-_\\.]","_");
	}

	/**
	 * returns the url parameters in a List of Model's Parameters
	 * @param query the part of the URL that contains all the parameters
	 * @return map
	 */
	@VisibleForTesting
	protected static List<Parameter> queryToParameterList(String query) {
		List<Parameter> result = new ArrayList<>();
		if (query == null || "".equals(query))
			return result;
		try {
			query = URLDecoder.decode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.warn("Request Parameters are not encode in UTF-8 : " + e);
		}
		for (String param : query.split("&")) {
			final Builder parameterBuilder = ImmutableParameter.builder();
			if(param.contains("=")){
				final String[] pair = param.split("=");
				if (pair.length > 1) {
					parameterBuilder.name(pair[0]).value(pair[1]);
				} else {
					parameterBuilder.name(pair[0]).value("");
				}
			} else {
				parameterBuilder.name(param);
			}
			result.add(parameterBuilder.build());
		}
		return result;
	}
	
	protected static List<Item> parseItemList(final List<String> attributes) {
		final List<Item> items = new ArrayList<>();
		final Item item = new Item();
		attributes.stream().forEach(attribute -> {
			if("ENDITEM".equals(attribute)){
				addItem(items,item);
			} else {
				item.getAttributes().add(unescape(unquote(attribute)));
			}			
		});
		return items;
	}

	private static void addItem(List<Item> items, Item item) {
        items.add(Item.of(item.getAttributes()));
        item.getAttributes().clear();
    }

    private static boolean isRestrictedBoundary(String boundary) {
		return ImmutableList.of("BIN", "DIG", "ALNUMIC", "ALNUMLC", "ALNUMUC").contains(boundary);
	}
	
    public static String getValueAndVerifyRestrictionForBoundary(final String leftBrace, final String rightBrace, MethodCall method, Optional<String> boundaryString) {
    	String unquotedString = MethodUtils.normalizeString(leftBrace, rightBrace, boundaryString.orElseThrow(IllegalArgumentException::new));
    	String lbOptions = unquotedString.split("=")[0];
        Arrays.asList(lbOptions.split("/")).stream().filter(MethodUtils::isRestrictedBoundary).forEach(option ->
        	logger.error("the option \"" + option + "\" can not be taken into account for the LR function : " + method.getName())
    	);
        return unquotedString.substring(lbOptions.length()+1);
    }
}
