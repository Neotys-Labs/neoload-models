package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.variable.SqlVariable;
import com.neotys.neoload.model.v3.validation.constraints.SqlVariableCheck;

import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * Checks the driver of a sql variable against its url: the driver is mandatory when the database
 * cannot be recognized from the url, and a driver that is entered must be the one of that database.
 * A driver class NeoLoad does not know is accepted, as long as it is not the driver of another database.
 */
public final class SqlVariableValidator extends AbstractConstraintValidator<SqlVariableCheck, SqlVariable> {

	private static final String DRIVER_MISMATCH_MESSAGE =
			"{com.neotys.neoload.model.v3.validation.constraints.SqlVariableCheck.driver.message}";

	@Override
	public boolean isValid(final SqlVariable sqlVariable, final ConstraintValidatorContext context) {
		final String url = sqlVariable.getUrl();
		if (url == null || url.trim().isEmpty()) {
			return true;
		}

		final Optional<JdbcDatabase> urlDatabase = JdbcDatabase.ofUrl(url);
		final Optional<String> driver = sqlVariable.getDriver();
		if (!driver.isPresent()) {
			return urlDatabase.isPresent();
		}

		final Optional<JdbcDatabase> driverDatabase = JdbcDatabase.ofDriver(driver.get());
		if (!urlDatabase.isPresent() || !driverDatabase.isPresent() || urlDatabase.get() == driverDatabase.get()) {
			return true;
		}

		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(DRIVER_MISMATCH_MESSAGE).addConstraintViolation();
		return false;
	}
}
