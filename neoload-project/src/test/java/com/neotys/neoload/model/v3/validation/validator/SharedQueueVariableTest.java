package com.neotys.neoload.model.v3.validation.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.variable.SharedQueueSwapFile;
import com.neotys.neoload.model.v3.project.variable.SharedQueueVariable;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.junit.Test;

public class SharedQueueVariableTest {

	@Test
	public void validateSwapFileWithoutPath() {
		final Validator validator = new Validator();

		final SharedQueueSwapFile swapFile = SharedQueueSwapFile.builder().path("").build();
		final SharedQueueVariable sharedQueueVariable = SharedQueueVariable.builder()
				.name("MySharedQueue")
				.swapFile(swapFile)
				.build();

		final Validation validation = validator.validate(sharedQueueVariable, NeoLoad.class);
		assertFalse(validation.isValid());
		assertTrue(validation.getMessage().get().contains("swap_file.path"));
	}

	@Test
	public void validateSwapFileDelimiterSize() {
		final Validator validator = new Validator();

		final SharedQueueSwapFile swapFile = SharedQueueSwapFile.builder().path("data/my_queue.csv").delimiter("ab").build();
		final SharedQueueVariable sharedQueueVariable = SharedQueueVariable.builder()
				.name("MySharedQueue")
				.swapFile(swapFile)
				.build();

		final Validation validation = validator.validate(sharedQueueVariable, NeoLoad.class);
		assertFalse(validation.isValid());
		assertTrue(validation.getMessage().get().contains("swap_file.delimiter"));
	}

	@Test
	public void validateQueueSizeMinimum() {
		final Validator validator = new Validator();

		final SharedQueueVariable sharedQueueVariable = SharedQueueVariable.builder()
				.name("MySharedQueue")
				.queueSize(0)
				.build();

		final Validation validation = validator.validate(sharedQueueVariable, NeoLoad.class);
		assertFalse(validation.isValid());
		assertTrue(validation.getMessage().get().contains("queue_size"));
	}

	@Test
	public void validateFullyValidSharedQueue() {
		final Validator validator = new Validator();

		final SharedQueueSwapFile swapFile = SharedQueueSwapFile.builder().path("data/my_queue.csv").delimiter(",").build();
		final SharedQueueVariable sharedQueueVariable = SharedQueueVariable.builder()
				.name("MySharedQueue")
				.swapFile(swapFile)
				.build();

		final Validation validation = validator.validate(sharedQueueVariable, NeoLoad.class);
		assertTrue(validation.isValid());
	}
}
