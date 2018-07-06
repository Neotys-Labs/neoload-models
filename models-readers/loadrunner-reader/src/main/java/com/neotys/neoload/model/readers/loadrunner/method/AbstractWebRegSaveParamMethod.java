package com.neotys.neoload.model.readers.loadrunner.method;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.repository.ImmutableVariableExtractor;
import com.neotys.neoload.model.repository.ImmutableVariableExtractor.Builder;
import com.neotys.neoload.model.repository.VariableExtractor.ExtractType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.readers.loadrunner.SearchAttribute;
import com.neotys.neoload.model.readers.loadrunner.WarningCallbBack;

public abstract class AbstractWebRegSaveParamMethod implements LoadRunnerMethod {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWebRegSaveParamMethod.class);

	private static final String ATTRIBUTE_CONVERT = "Convert";
	private static final String ATTRIBUTE_IGNOREREDIRECTIONS = "IgnoreRedirections";
	private static final String ATTRIBUTE_NOTFOUND = "NOTFOUND";
	private static final String ATTRIBUTE_ORD = "ORD";
	private static final String ATTRIBUTE_RELFRAMEID = "RelFrameID";
	private static final String ATTRIBUTE_SAVELEN = "SaveLen";
	private static final String ATTRIBUTE_SAVEOFFSET = "SaveOffset";
	private static final String ATTRIBUTE_SEARCH = "Search";
	private static final String ATTRIBUTE_DFES = "DFEs";
	private static final String ATTRIBUTE_HEADERNAMES = "HeaderNames";
	private static final String ATTRIBUTE_REQUESTURL = "RequestURL";
	private static final String ATTRIBUTE_CONTENTTYPE = "ContentType";
	private static final String ATTRIBUTE_LB = "LB";
	private static final String ATTRIBUTE_RB = "RB";
	private static final String ATTRIBUTE_REGEXP = "RegExp";
	private static final String ATTRIBUTE_GROUP = "Group";
	private static final String ATTRIBUTE_QUERYSTRING = "QueryString";
	private static final String ATTRIBUTE_RETURNXML = "ReturnXML";
	private static final String DEFAULT_REGEXP_VALUE = "(.*?)";
	private static final String ATTRIBUTE_VALUE_ERROR = "ERROR";
	private static final String ATTRIBUTE_VALUE_EMPTY = "empty";
	private static final String ATTRIBUTE_VALUE_ALL = "All";
	private static final String PARAM_NAME_PREFIX = "ParamName=";
	private static final String LOG_THE_ATTRIBUTE = "The attribute \"";
	private static final String LOG_IS_MANDATORY = "\" is mandatory, but not found in LR function was : ";
	
	
	private static final Set<String> UNSUPPORTED_ATTRIBUTE = new HashSet<>();
	static {
		UNSUPPORTED_ATTRIBUTE.addAll(Arrays.asList(ATTRIBUTE_HEADERNAMES, ATTRIBUTE_REQUESTURL, ATTRIBUTE_CONTENTTYPE, ATTRIBUTE_DFES,
				ATTRIBUTE_SAVEOFFSET, ATTRIBUTE_CONVERT, ATTRIBUTE_RELFRAMEID, ATTRIBUTE_SAVELEN, ATTRIBUTE_IGNOREREDIRECTIONS, ATTRIBUTE_RETURNXML));
	}

	public AbstractWebRegSaveParamMethod() {
		super();
	}

	void logWarn(final String warnMessage, final WarningCallbBack warningCallbBack) {
		warningCallbBack.addWarningMessage(warnMessage);
		LOGGER.warn(warnMessage);
	}

	@Override
	public final Element getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		final WarningCallbBack warningCallback = new WarningCallbBack();
		Preconditions.checkNotNull(method);
		try {
			final Builder extractBuilder = parseExtractor(visitor, method, warningCallback);
			visitor.getCurrentExtractors().add(extractBuilder.build());
			return null;
		} finally {
			if (warningCallback.isWarning()) {
				visitor.readSupportedFunctionWithWarn(method.getName(), ctx, warningCallback.getMessage());
			} else {
				visitor.readSupportedFunction(method.getName(), ctx);
			}
		}
	}

	final Builder parseExtractor(final LoadRunnerVUVisitor visitor, final MethodCall method, final WarningCallbBack warningCallback) {
		final String leftBrace = visitor.getLeftBrace();
		final String rightBrace = visitor.getRightBrace();
		String extractorName = MethodUtils.normalizeString(leftBrace, rightBrace, method.getParameters().get(0));
		if (extractorName.startsWith(PARAM_NAME_PREFIX)) {
			extractorName = extractorName.substring(PARAM_NAME_PREFIX.length());
		}
		final Builder extractBuilder = ImmutableVariableExtractor.builder().name(extractorName);
		final SearchAttribute searchAttribute = handleSearch(method, warningCallback, leftBrace, rightBrace);
		extractBuilder.extractType(searchAttribute.getExtractType());				
		handleLBRB(visitor, method, extractBuilder);
		handleRegExp(visitor, method, warningCallback, extractBuilder);
		handleXPath(visitor, method, warningCallback, extractBuilder, searchAttribute);
		handleJsonPath(visitor, method, warningCallback, extractBuilder, searchAttribute);
		handleNotFound(method, warningCallback, leftBrace, rightBrace, extractBuilder);
		logUnsupportedAttributes(method, warningCallback, leftBrace, rightBrace);
		handleOrd(method, warningCallback, leftBrace, rightBrace, extractBuilder);
		return extractBuilder;
	}

	private void handleLBRB(final LoadRunnerVUVisitor visitor, final MethodCall method, final Builder extractBuilder) {
		if (supportLBRB()) {
			final String startExpression = MethodUtils.getValueAndVerifyRestrictionForBoundary(visitor.getLeftBrace(), visitor.getRightBrace(),
					method,
					MethodUtils.getParameterStartingWith(method, ATTRIBUTE_LB));
			final String endExpression = MethodUtils.getValueAndVerifyRestrictionForBoundary(visitor.getLeftBrace(), visitor.getRightBrace(), method,
					MethodUtils.getParameterStartingWith(method, ATTRIBUTE_RB));
			extractBuilder.startExpression(startExpression).endExpression(endExpression);
		}
	}

	private void handleRegExp(final LoadRunnerVUVisitor visitor, final MethodCall method, final WarningCallbBack warningCallback,
			final Builder extractBuilder) {
		if (supportRegExp()) {
			final Optional<String> regExp = MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method,
					ATTRIBUTE_REGEXP);
			if (regExp.isPresent()) {
				extractBuilder.regExp(regExp.get());
			} else {
				logWarn(LOG_THE_ATTRIBUTE + ATTRIBUTE_REGEXP + LOG_IS_MANDATORY + method.getName(),
						warningCallback);
				extractBuilder.regExp(DEFAULT_REGEXP_VALUE);
			}
			final Optional<String> group = MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method,
					ATTRIBUTE_GROUP);
			if (group.isPresent()) {
				extractBuilder.group(group.get());
			}
		}
	}

	private void handleOrd(final MethodCall method, final WarningCallbBack warningCallback, final String leftBrace, final String rightBrace,
			final Builder extractBuilder) {
		MethodUtils.getParameterValueWithName(leftBrace, rightBrace, method, ATTRIBUTE_ORD).ifPresent(value -> {
			int nbOccur = 1;
			try {
				nbOccur = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				logWarn("The variable extractor contains an occurrence number that is not an integer. Fallback to extract first occurrence.",
						warningCallback);
			}
			extractBuilder.nbOccur(nbOccur);
		});
	}

	private void handleNotFound(final MethodCall method, final WarningCallbBack warningCallback, final String leftBrace, final String rightBrace,
			final Builder extractBuilder) {
		String strError = MethodUtils.getParameterValueWithName(leftBrace, rightBrace, method, ATTRIBUTE_NOTFOUND).orElse(ATTRIBUTE_VALUE_ERROR);
		if (ATTRIBUTE_VALUE_ERROR.equalsIgnoreCase(strError)) {
			extractBuilder.exitOnError(true);
		} else {
			if (!ATTRIBUTE_VALUE_EMPTY.equalsIgnoreCase(strError)) {
				logWarn("The value \"" + strError
						+ "\" has not been taken under account. The value will be set to \"" + ATTRIBUTE_VALUE_EMPTY + "\". The LR function was : "
						+ method.getName(),
						warningCallback);
			}
			extractBuilder.exitOnError(false);
		}
	}

	private void handleJsonPath(final LoadRunnerVUVisitor visitor, final MethodCall method, final WarningCallbBack warningCallback,
			final Builder extractBuilder, final SearchAttribute searchAttribute) {
		if (supportJsonPath()) {
			final Optional<String> jsonPath = MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method,
					ATTRIBUTE_QUERYSTRING);
			if (jsonPath.isPresent()) {
				extractBuilder.jsonPath(jsonPath.get());
				extractBuilder.extractType(ExtractType.JSON);
				if(searchAttribute != SearchAttribute.BODY){
					logWarn(LOG_THE_ATTRIBUTE + ATTRIBUTE_QUERYSTRING + "\" requires to have a Search attribute with " + SearchAttribute.BODY + " instead of " + searchAttribute.toString(),
							warningCallback);					
				}
			} else {
				logWarn(LOG_THE_ATTRIBUTE + ATTRIBUTE_QUERYSTRING + LOG_IS_MANDATORY + method.getName(),
						warningCallback);
				extractBuilder.jsonPath("");
			}
		}
	}

	private void handleXPath(final LoadRunnerVUVisitor visitor, final MethodCall method, final WarningCallbBack warningCallback,
			final Builder extractBuilder, final SearchAttribute searchAttribute) {
		if (supportXPath()) {
			final Optional<String> xpath = MethodUtils.getParameterValueWithName(visitor.getLeftBrace(), visitor.getRightBrace(), method,
					ATTRIBUTE_QUERYSTRING);
			if (xpath.isPresent()) {
				extractBuilder.xPath(xpath.get());
				extractBuilder.extractType(ExtractType.XPATH);
				if(searchAttribute != SearchAttribute.BODY){
					logWarn(LOG_THE_ATTRIBUTE + ATTRIBUTE_QUERYSTRING + "\" requires to have a Search attribute with " + SearchAttribute.BODY + " instead of " + searchAttribute.toString(),
							warningCallback);					
				}
			} else {
				logWarn(LOG_THE_ATTRIBUTE + ATTRIBUTE_QUERYSTRING + LOG_IS_MANDATORY + method.getName(),
						warningCallback);
				extractBuilder.xPath("");
			}
		}
	}

	private SearchAttribute handleSearch(final MethodCall method, final WarningCallbBack warningCallback, final String leftBrace,
			final String rightBrace) {
		final SearchAttribute searchAttribute = SearchAttribute.from(
				MethodUtils.getParameterValueWithName(leftBrace, rightBrace, method, ATTRIBUTE_SEARCH).orElse(ATTRIBUTE_VALUE_ALL));
		if (searchAttribute == SearchAttribute.COOKIES) {
			logWarn("The option \"Search=Cookies\" is not supported in NeoLoad. The extractor type has been set to \"Headers\".",
					warningCallback);
		} else if (searchAttribute == SearchAttribute.NORESOURCE) {
			logWarn("The option \"Search=Noresource\" is not supported in NeoLoad. The extractor type has been set to \"Body\".",
					warningCallback);
		}
		return searchAttribute;
	}

	abstract boolean supportLBRB();

	abstract boolean supportRegExp();

	abstract boolean supportXPath();
	
	abstract boolean supportJsonPath();

	private void logUnsupportedAttributes(final MethodCall method, final WarningCallbBack warningCallback, final String leftBrace,
			final String rightBrace) {

		for (final String unsupportedAttribute : UNSUPPORTED_ATTRIBUTE) {
			MethodUtils.getParameterValueWithName(leftBrace, rightBrace, method, unsupportedAttribute).ifPresent(value -> logWarn(
					"The option \"" + unsupportedAttribute + "\" has not been taken under account for the LR function with name : "
							+ method.getName(),
					warningCallback));
		}
	}
}
