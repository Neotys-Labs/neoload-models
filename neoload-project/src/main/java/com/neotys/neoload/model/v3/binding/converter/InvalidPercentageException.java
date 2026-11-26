package com.neotys.neoload.model.v3.binding.converter;

/**
 * Raised when a text is not a valid integer percentage.
 */
public final class InvalidPercentageException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	InvalidPercentageException(final String input) {
		super("Incorrect percentage: '" + input + "' is not a valid percentage.");
	}
}
