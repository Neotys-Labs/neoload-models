package com.neotys.neoload.model.v3.project.variable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OffsetTest {
	private static void assertInvalid(final String text) {
		final Offset offset = Offset.parse(text);
		assertTrue(text, offset.isInvalid());
		assertEquals(text, offset.toText());
	}

	@Test
	public void parsesNegativeOffset() {
		final Offset offset = Offset.parse("-5d");
		assertFalse(offset.isInvalid());
		assertEquals(-5, offset.getAmount());
		assertEquals(IncrementTimeUnit.DAY, offset.getUnit());
		assertEquals(Offset.of(-5, IncrementTimeUnit.DAY), offset);
	}

	@Test
	public void parsesEveryUnitAndWritesItBack() {
		for (final IncrementTimeUnit unit : IncrementTimeUnit.values()) {
			final String text = "-5" + unit.getCode();
			final Offset offset = Offset.parse(text);
			assertEquals(text, -5, offset.getAmount());
			assertEquals(text, unit, offset.getUnit());
			assertEquals(text, offset.toText());
		}
	}

	@Test
	public void malformedOffsetsAreInvalidAndKeepTheirText() {
		assertInvalid("5x");
		assertInvalid("d5");
		assertInvalid("");
		assertInvalid("5");
		assertInvalid("3 d");
		assertInvalid("3h2m");
	}

	@Test
	public void overflowIsInvalidInsteadOfThrowing() {
		assertInvalid("99999999999d");
	}

	@Test(expected = IllegalStateException.class)
	public void invalidOffsetHasNoAmount() {
		Offset.parse("5x").getAmount();
	}

	@Test
	public void equalityIsByValue() {
		assertEquals(Offset.parse("3h"), Offset.of(3, IncrementTimeUnit.HOUR));
		assertEquals(Offset.parse("3h").hashCode(), Offset.of(3, IncrementTimeUnit.HOUR).hashCode());
		assertNotEquals(Offset.parse("3h"), Offset.parse("3m"));
		assertNotEquals(Offset.parse("5x"), Offset.parse("6x"));
	}
}
