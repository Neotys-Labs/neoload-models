package com.neotys.neoload.model.v3.project.duration;

import java.util.Objects;
import java.util.Optional;

/**
 * A duration read from a project: a literal amount, a {@code ${variable}} reference, or the raw text
 * that follows no grammar, kept as-is so that validation reports it and writing gives it back unchanged.
 */
public abstract class DurationValue {
	private final DurationGrammar grammar;
	private final Long amount;
	private final String variable;
	private final String invalidText;

	DurationValue(final DurationGrammar grammar, final Long amount, final String variable, final String invalidText) {
		this.grammar = grammar;
		this.amount = amount;
		this.variable = variable;
		this.invalidText = invalidText;
	}

	public final boolean isLiteral() {
		return amount != null;
	}

	public final boolean isVariable() {
		return variable != null;
	}

	public final boolean isInvalid() {
		return invalidText != null;
	}

	/**
	 * @return the literal amount in the unit of the duration.
	 * @throws IllegalStateException if the duration is not a literal.
	 */
	public final long getAmount() {
		if (amount == null) {
			throw new IllegalStateException("Not a literal duration: " + toText());
		}
		return amount;
	}

	/**
	 * @return the referenced variable name, without its delimiters.
	 */
	public final Optional<String> getVariable() {
		return Optional.ofNullable(variable);
	}

	/**
	 * @return the compact text of a literal (e.g. {@code 1m30s}), {@code ${name}} for a variable, the raw text when invalid.
	 */
	public final String toText() {
		if (amount != null) {
			return grammar.format(amount);
		}
		if (variable != null) {
			return "${" + variable + "}";
		}
		return invalidText;
	}

	@Override
	public final boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		final DurationValue that = (DurationValue) other;
		return Objects.equals(amount, that.amount) && Objects.equals(variable, that.variable) && Objects.equals(invalidText, that.invalidText);
	}

	@Override
	public final int hashCode() {
		return Objects.hash(getClass(), amount, variable, invalidText);
	}

	@Override
	public final String toString() {
		return toText();
	}
}
