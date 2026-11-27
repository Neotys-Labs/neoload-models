package com.neotys.neoload.model.v3.binding.converter;

/**
 * Raised when a text cannot be read as the value of its field, such as a number of users beyond the 32-bit range.
 * Holds the rejected text so that the read error can name it.
 */
public final class InvalidValueException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	private final String value;

	InvalidValueException(final String value, final String message) {
		super(message);
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
