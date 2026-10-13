package com.neotys.neoload.model.v3.project;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * An element defining a min/max range, checked by
 * {@link com.neotys.neoload.model.v3.validation.constraints.MinNotGreaterThanMaxCheck}.
 *
 * <p>Both bounds are exposed as numbers in the same unit so they can be compared. A bound that cannot
 * be compared (a {@code ${variable}}, or a value already rejected by the field's own check) is empty and
 * leaves the range unchecked.
 */
public interface MinMaxRange {
	Optional<BigDecimal> comparableMin();

	Optional<BigDecimal> comparableMax();
}
