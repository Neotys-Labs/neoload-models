package com.neotys.neoload.model.v3.validation.constraintvalidators;

import javax.validation.ConstraintValidatorContext;

import com.neotys.neoload.model.v3.compatibility.SchemaSupport;
import com.neotys.neoload.model.v3.validation.constraints.ValidSchemaVersion;

public final class ValidSchemaVersionValidator
		extends AbstractConstraintValidator<ValidSchemaVersion, String> {

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		// null is considered valid here; use @NotNull separately if required
		if (value == null) {
			return true;
		}
		return SchemaSupport.getDefault().isSupported(value);
	}
}
