package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.security.WsKeystoreToken;
import com.neotys.neoload.model.v3.validation.constraints.WsKeystoreTokenCheck;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks the keystore of a signature or encryption token.
 * <p>
 * A keystore holding a variable (containing <code>${</code>, mixed values such as
 * {@code ./wss-keystores/client_${env}.p12} included) must start with {@code ./wss-keystores/} and cannot contain a
 * {@code ..} path segment, so it stays inside that folder; the rest of the value is never checked. A token
 * {@code password} is only allowed with such a variable keystore: the password of a fixed keystore is the one of its
 * {@code keystores} entry.
 */
public final class WsKeystoreTokenValidator extends AbstractConstraintValidator<WsKeystoreTokenCheck, WsKeystoreToken> {
	private static final String VARIABLE_START = "${";
	private static final String KEYSTORES_FOLDER = "./wss-keystores/";
	private static final Pattern PARENT_SEGMENT = Pattern.compile("(^|[/\\\\])\\.\\.([/\\\\]|$)");
	private static final String PASSWORD_MESSAGE =
			"{com.neotys.neoload.model.v3.validation.constraints.WsKeystoreTokenCheck.password.message}";
	private static final String PREFIX_MESSAGE =
			"{com.neotys.neoload.model.v3.validation.constraints.WsKeystoreTokenCheck.prefix.message}";

	@Override
	public boolean isValid(final WsKeystoreToken token, final ConstraintValidatorContext context) {
		if (token == null) {
			return true;
		}
		final String keystore = token.getKeystore();
		final boolean variable = keystore != null && keystore.contains(VARIABLE_START);
		if (variable && (!keystore.startsWith(KEYSTORES_FOLDER) || PARENT_SEGMENT.matcher(keystore).find())) {
			report(context, PREFIX_MESSAGE);
			return false;
		}
		if (!variable && token.getPassword().isPresent()) {
			report(context, PASSWORD_MESSAGE);
			return false;
		}
		return true;
	}

	private static void report(final ConstraintValidatorContext context, final String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}
