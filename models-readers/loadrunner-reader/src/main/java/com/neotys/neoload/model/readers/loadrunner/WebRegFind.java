package com.neotys.neoload.model.readers.loadrunner;

import java.util.Optional;

import com.neotys.neoload.model.repository.ImmutableRegexpValidator;
import com.neotys.neoload.model.repository.ImmutableTextValidator;
import com.neotys.neoload.model.repository.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;


public class WebRegFind {
	static Logger logger = LoggerFactory.getLogger(WebRegFind.class);

	/**
	 * We use this current id for the naming of the assertions (in Load Runner web_reg_find does not have a name)
	 */
	private static int currentID = 0;
	
	private static final String NOTFOUND = "NotFound";

	private WebRegFind() {
	}

	public static Validator toValidator(final String leftBrace, final String rightBrace, final MethodCall method) {
		Preconditions.checkNotNull(method);

		MethodUtils.getParameterValueWithName(leftBrace, rightBrace, method, "Search").ifPresent(value -> {
			if ("Headers".equals(value) || "Body".equals(value))
				logger.error("The value \"" + value + "\" for the \"Search\" option has not been taken under account for the LR function with name : "
						+ method.getName());
		});

		MethodUtils.getParameterValueWithName(leftBrace, rightBrace, method, "SaveCount").ifPresent(value -> logger.warn(
				"The option \"" + value + "\" for the the LR function with name \"" + method.getName() + "\" has not been taken under account"));

		Optional<String> textPfx = MethodUtils.getParameterStartingWith(method, "TextPfx");
		if (textPfx.isPresent()) {
			String prefix = MethodUtils.getValueAndVerifyRestrictionForBoundary(leftBrace, rightBrace, method, textPfx);
			String suffix = MethodUtils.getValueAndVerifyRestrictionForBoundary(leftBrace, rightBrace, method,
					MethodUtils.getParameterStartingWith(method, "TextSfx"));
			String regex = "\\Q" + prefix + "\\E.*\\Q" + suffix + "\\E";
			return ImmutableRegexpValidator.builder().name(method.getName() + "_" + Integer.toString(currentID++)).haveToContains(
					NOTFOUND.equals(MethodUtils.getParameterValueWithName(leftBrace, rightBrace, method, "Fail").orElse(
							NOTFOUND))).validationRegex(regex).build();
		}
		String exactMatch = MethodUtils.getValueAndVerifyRestrictionForBoundary(leftBrace, rightBrace, method,
				MethodUtils.getParameterStartingWith(method, "Text"));
		return ImmutableTextValidator.builder().name(method.getName() + "_" + Integer.toString(currentID++)).haveToContains(NOTFOUND.equals(
				MethodUtils.getParameterValueWithName(leftBrace, rightBrace, method, "Fail").orElse(NOTFOUND))).validationText(
						exactMatch).build();

	}
}
