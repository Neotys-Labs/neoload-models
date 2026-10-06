package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.security.WsKeystore;
import com.neotys.neoload.model.v3.validation.constraints.UniqueWsKeystorePathCheck;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintValidatorContext;

public final class UniqueWsKeystorePathValidator extends AbstractConstraintValidator<UniqueWsKeystorePathCheck, List<WsKeystore>> {
	@Override
	public boolean isValid(final List<WsKeystore> keystores, final ConstraintValidatorContext context) {
		if (keystores == null) {
			return true;
		}
		final Set<String> paths = new HashSet<>();
		return keystores.stream()
				.map(WsKeystore::getPath)
				.filter(path -> !Strings.isNullOrEmpty(path))
				.allMatch(paths::add);
	}
}
