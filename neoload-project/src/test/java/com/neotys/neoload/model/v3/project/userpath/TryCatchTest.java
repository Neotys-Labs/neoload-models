package com.neotys.neoload.model.v3.project.userpath;

import static org.junit.Assert.assertEquals;

import java.util.Optional;
import org.junit.Test;

public class TryCatchTest {

	@Test
	public void testFlattenWithCatch() {
		final TryCatch tryCatch = TryCatch.builder()
				.name("my_try_catch")
				.getTry(Container.builder()
						.addSteps(Delay.builder().value("500").build(), Delay.builder().value("2500").build())
						.build())
				.getCatch(Container.builder()
						.addSteps(Delay.builder().value("1000").build())
						.build())
				.build();
		// 1 tryCatch + 1 try container + 2 delays + 1 catch container + 1 delay = 6
		assertEquals(6, tryCatch.flattened().count());
	}

	@Test
	public void testFlattenWithoutCatch() {
		final TryCatch tryCatch = TryCatch.builder()
				.name("my_try_catch")
				.getTry(Container.builder()
						.addSteps(Delay.builder().value("500").build())
						.build())
				.build();
		// 1 tryCatch + 1 try container + 1 delay = 3
		assertEquals(3, tryCatch.flattened().count());
	}

	@Test
	public void testDefaultValues() {
		final TryCatch tryCatch = TryCatch.builder()
				.getTry(Container.builder()
						.addSteps(Delay.builder().value("500").build())
						.build())
				.build();
		assertEquals("try_catch", tryCatch.getName());
		assertEquals(Optional.empty(), tryCatch.getCaughtExceptions());
	}
}