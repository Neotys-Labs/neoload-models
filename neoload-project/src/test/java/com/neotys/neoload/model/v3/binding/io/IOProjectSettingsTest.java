package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;
import static org.junit.Assert.assertEquals;

import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import org.junit.Test;

/**
 * The qTest project and cycle identifiers of the project settings are numbers greater than or equal to 1.
 */
public class IOProjectSettingsTest extends AbstractIOElementsTest {
	private static final String PROJECT_SETTINGS = "project.project_settings.";
	private static final String QTEST_ID_MESSAGE = "must be a number greater than or equal to 1.";

	private ProjectDescriptor read(final String fileName) throws IOException {
		return new IO().read(getFile(fileName, "yaml"));
	}

	private static Validation validateNeoLoad(final ProjectDescriptor descriptor) {
		return new Validator().validate(descriptor, NeoLoad.class);
	}

	@Test
	public void qTestProjectIdZeroIsRejectedWithTheKeyAndTheValue() throws IOException {
		final ProjectDescriptor descriptor = read("test-project-settings-qtest-project-id-zero");

		assertViolations(validateNeoLoad(descriptor), invalidValue("0", PROJECT_SETTINGS + "qtest.project_id", QTEST_ID_MESSAGE));
		assertEquals("0", descriptor.getProject().getProjectSettings().get("qtest.project_id"));
	}

	@Test
	public void negativeQTestCycleIdIsRejectedWithTheKeyAndTheValue() throws IOException {
		final ProjectDescriptor descriptor = read("test-project-settings-qtest-cycle-id-negative");

		assertViolations(validateNeoLoad(descriptor), invalidValue("-1", PROJECT_SETTINGS + "qtest.cycle_id", QTEST_ID_MESSAGE));
	}

	@Test
	public void bothInvalidQTestIdsAreReportedSeparately() throws IOException {
		final ProjectDescriptor descriptor = read("test-project-settings-qtest-ids-invalid");

		assertViolations(validateNeoLoad(descriptor),
				invalidValue("-5", PROJECT_SETTINGS + "qtest.cycle_id", QTEST_ID_MESSAGE),
				invalidValue("0", PROJECT_SETTINGS + "qtest.project_id", QTEST_ID_MESSAGE));
	}
}
