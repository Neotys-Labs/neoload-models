package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.security.WsPart;
import com.neotys.neoload.model.v3.validation.constraints.WsPartCheck;
import javax.validation.ConstraintValidatorContext;

public final class WsPartValidator extends AbstractConstraintValidator<WsPartCheck, WsPart> {
	@Override
	public boolean isValid(final WsPart part, final ConstraintValidatorContext context) {
		if (part == null) {
			return true;
		}
		if (part.getId().isPresent()) {
			return !part.getName().isPresent() && !part.getNamespace().isPresent();
		}
		return part.getName().isPresent();
	}
}
