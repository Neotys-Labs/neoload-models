package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.WebSocketMessagesMapping;
import com.neotys.neoload.model.v3.validation.constraints.UniqueWebSocketMessagesMappingPathCheck;

import javax.validation.ConstraintValidatorContext;

public final class UniqueWebSocketMessagesMappingPathValidator extends AbstractConstraintValidator<UniqueWebSocketMessagesMappingPathCheck, WebSocketMessagesMapping> {
	@Override
	public boolean isValid(final WebSocketMessagesMapping messagesMapping, final ConstraintValidatorContext context) {
		if (messagesMapping == null) {
			return true;
		}
		return !(messagesMapping.getJsonPath().isPresent() && messagesMapping.getXpath().isPresent());
	}
}
