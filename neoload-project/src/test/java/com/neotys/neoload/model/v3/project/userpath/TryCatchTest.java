package com.neotys.neoload.model.v3.project.userpath;


import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class TryCatchTest {
	@Test
	public void testFlattenSwitchWithoutDefault(){
		final TryCatch tryCatch = ImmutableTryCatch.builder()
			.name("tryCatch")
			.description("Hunt or be hunted")
			.getTry(Container.builder().addSteps(Delay.builder().value("500").build(), Delay.builder().value("2500").build()).build())
			.getCatch(Container.builder().addSteps(Delay.builder().value("500").build(), Delay.builder().value("2500").build()).build())
			.build();
		assertEquals(7, tryCatch.flattened().count());
	}

}