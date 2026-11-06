package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.security.WsKeystore;
import com.neotys.neoload.model.v3.validation.constraints.UniqueWsKeystorePathCheck;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public final class UniqueWsKeystorePathValidator extends AbstractUniqueKeyValidator<WsKeystore>
		implements ConstraintValidator<UniqueWsKeystorePathCheck, List<? extends WsKeystore>> {
	@Override
	public boolean isValid(final List<? extends WsKeystore> items, final ConstraintValidatorContext context) {
		return hasUniqueKeys(items, context);
	}

	@Override
	String key(final WsKeystore item) {
		return item.getPath();
	}
}
