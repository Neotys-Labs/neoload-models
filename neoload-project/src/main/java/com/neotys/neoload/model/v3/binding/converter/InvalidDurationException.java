package com.neotys.neoload.model.v3.binding.converter;

/**
 * Raised when a text is not a valid duration, such as misordered or repeated units.
 */
public final class InvalidDurationException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	InvalidDurationException(final String input) {
		super("Incorrect duration: '" + input + "' is not a valid duration.");
	}
}
