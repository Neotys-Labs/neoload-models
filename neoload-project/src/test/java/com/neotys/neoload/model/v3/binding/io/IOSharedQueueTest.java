package com.neotys.neoload.model.v3.binding.io;


import static junit.framework.TestCase.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.variable.SharedQueueSwapFile;
import com.neotys.neoload.model.v3.project.variable.SharedQueueVariable;
import java.io.IOException;
import org.junit.Test;

public class IOSharedQueueTest extends AbstractIOElementsTest {

	@Test
	public void readSharedQueueOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingSharedQueueOnlyRequired();
		assertNotNull(expectedProject);

		read("test-shared-queue-only-required", expectedProject);
	}

	@Test
	public void writeSharedQueueOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingSharedQueueOnlyRequired();
		assertNotNull(expectedProject);

		write("test-shared-queue-only-required", expectedProject);
	}

	@Test
	public void readSharedQueueRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingSharedQueueRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-shared-queue-required-and-optional", expectedProject);
	}

	@Test
	public void writeSharedQueueRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingSharedQueueRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-shared-queue-required-and-optional", expectedProject);
	}

	private Project buildProjectContainingSharedQueueOnlyRequired() {
		final SharedQueueVariable minimalSharedQueueVariable = SharedQueueVariable.builder()
				.name("MySharedQueue")
				.build();

		return Project.builder()
				.name("MyProject")
				.addVariables(minimalSharedQueueVariable)
				.build();
	}

	private Project buildProjectContainingSharedQueueRequiredAndOptional() {
		final SharedQueueVariable fullSharedQueueVariable = SharedQueueVariable.builder()
				.name("MySharedQueue")
				.description("A producer/consumer shared queue")
				.queueSize(5000)
				.consumerTimeout(2000L)
				.swapFile(SharedQueueSwapFile.builder()
						.path("data/my_queue_swap.csv")
						.delimiter(",")
						.isLoadFromFile(true)
						.isSaveToFile(false)
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addVariables(fullSharedQueueVariable)
				.build();
	}
}
