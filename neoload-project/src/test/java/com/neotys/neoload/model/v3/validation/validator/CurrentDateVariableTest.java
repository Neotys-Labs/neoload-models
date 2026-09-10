package com.neotys.neoload.model.v3.validation.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.variable.CurrentDateVariable;
import com.neotys.neoload.model.v3.project.variable.CurrentDateVariable.Offset;
import com.neotys.neoload.model.v3.project.variable.CurrentDateVariable.OffsetUnit;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import org.junit.Test;

public class CurrentDateVariableTest {

	@Test
	public void validateDefaults() {
		final Validator validator = new Validator();

		final CurrentDateVariable variable = CurrentDateVariable.builder()
				.name("MyCurrentDate")
				.build();

		final Validation validation = validator.validate(variable, NeoLoad.class);
		assertTrue(validation.isValid());
		assertFalse(validation.getMessage().isPresent());
		assertEquals(CurrentDateVariable.DEFAULT_PATTERN, variable.getPattern());
		assertFalse(variable.getOffset().isPresent());
		assertFalse(variable.getParsedOffset().isPresent());
	}

	@Test
	public void validateSpecialPatterns() {
		final Validator validator = new Validator();

		for (final String pattern : new String[]{"currentTimeMillis", "milliseconds since the UNIX epoch"}) {
			final CurrentDateVariable variable = CurrentDateVariable.builder()
					.name("MyCurrentDate")
					.pattern(pattern)
					.build();

			assertTrue(pattern, validator.validate(variable, NeoLoad.class).isValid());
		}
	}

	@Test
	public void validateInvalidPattern() {
		final Validator validator = new Validator();

		final CurrentDateVariable variable = CurrentDateVariable.builder()
				.name("MyCurrentDate")
				.pattern("dd/MM/yyyy 'unterminated")
				.build();

		final Validation validation = validator.validate(variable, NeoLoad.class);
		assertFalse(validation.isValid());
		assertTrue(validation.getMessage().get().contains("pattern"));
	}

	// Offset values are accepted or rejected in IOVariableTest, from YAML fixtures.
	@Test
	public void parseEveryOffsetUnit() {
		for (final OffsetUnit unit : OffsetUnit.values()) {
			final String offset = "-5" + unit.getCode();
			final CurrentDateVariable variable = CurrentDateVariable.builder()
					.name("MyCurrentDate")
					.offset(offset)
					.build();

			final Optional<Offset> parsed = variable.getParsedOffset();
			assertTrue(offset, parsed.isPresent());
			assertEquals(-5, parsed.get().getAmount());
			assertEquals(unit, parsed.get().getUnit());
		}
	}

	@Test
	public void mapOffsetUnitToLegacyDateIncrementType() {
		assertEquals(-1, OffsetUnit.MILLISECOND.getDateIncrementTypeCode());
		assertEquals(0, OffsetUnit.SECOND.getDateIncrementTypeCode());
		assertEquals(1, OffsetUnit.MINUTE.getDateIncrementTypeCode());
		assertEquals(2, OffsetUnit.HOUR.getDateIncrementTypeCode());
		assertEquals(3, OffsetUnit.DAY.getDateIncrementTypeCode());
		assertEquals(4, OffsetUnit.MONTH.getDateIncrementTypeCode());
		assertEquals(5, OffsetUnit.YEAR.getDateIncrementTypeCode());
	}
}
