package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.assertViolations;
import static com.neotys.neoload.model.v3.binding.io.ViolationsAssert.invalidValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.sla.SlaProfile;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KPI;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.Scope;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Operator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Test;

public class IOSlaProfilesTest extends AbstractIOElementsTest {
	private static final String PERCENT = "project.sla_profiles[0].thresholds[0].percent";

	private static String percentileProject(final int percent) {
		return "name: P\nsla_profiles:\n- name: S\n  thresholds:\n  - perc-transaction-resp-time (p" + percent + ") warn >= 1 per test\n";
	}

	private static ProjectDescriptor readYaml(final String yaml) throws IOException {
		return new IO().read(yaml);
	}

	private static Validation validationOf(final ProjectDescriptor descriptor) {
		return new Validator().validate(descriptor, NeoLoad.class);
	}

	private static Integer percent(final ProjectDescriptor descriptor) {
		return descriptor.getProject().getSlaProfiles().get(0).getThresholds().get(0).getPercent().get();
	}

	private static Project getUserPathsOnlyRequired() {
		final SlaProfile slaProfile = SlaProfile.builder()
				.name("MySlaProfile")
				.thresholds(Arrays.asList(
						SlaThreshold.builder()
							.kpi(KPI.AVG_REQUEST_RESP_TIME)
							.addConditions(SlaThresholdCondition.builder()
									.severity(Severity.WARN)
									.operator(Operator.GREATER_THAN)
									.value(1.0)
									.build())
							.build()
						,
						SlaThreshold.builder()
						.kpi(KPI.PERC_TRANSACTION_RESP_TIME)
						.percent(90)
						.addConditions(SlaThresholdCondition.builder()
								.severity(Severity.WARN)
								.operator(Operator.GREATER_THAN)
								.value(1.0)
								.build())
						.build()))
				.build();

		return Project.builder()
				.name("MyProject")
				.addSlaProfiles(slaProfile)
				.build();
	}

	private static Project getUserPathsRequiredAndOptional() {
		final SlaProfile slaProfile = SlaProfile.builder()
				.name("MySlaProfile")
				.description("My Sla Profiles")
				.thresholds(Arrays.asList(
						SlaThreshold.builder()
							.kpi(KPI.AVG_REQUEST_RESP_TIME)
							.addConditions(SlaThresholdCondition.builder()
									.severity(Severity.WARN)
									.operator(Operator.GREATER_THAN)
									.value(0.15)
									.build())
							.addConditions(SlaThresholdCondition.builder()
									.severity(Severity.FAIL)
									.operator(Operator.GREATER_THAN)
									.value(1.25)
									.build())
							.scope(Scope.PER_TEST)
							.build()
						,
						SlaThreshold.builder()
							.kpi(KPI.PERC_TRANSACTION_RESP_TIME)
							.percent(50)
							.addConditions(SlaThresholdCondition.builder()
									.severity(Severity.WARN)
									.operator(Operator.EQUALS)
									.value(0.15)
									.build())
							.addConditions(SlaThresholdCondition.builder()
									.severity(Severity.FAIL)
									.operator(Operator.EQUALS)
									.value(1.25)
									.build())
							.scope(Scope.PER_TEST)
							.build()
						,
						SlaThreshold.builder()
							.kpi(KPI.AVG_RESP_TIME)
							.addConditions(SlaThresholdCondition.builder()
									.severity(Severity.WARN)
									.operator(Operator.LESS_THAN)
									.value(0.15)
									.build())
							.addConditions(SlaThresholdCondition.builder()
									.severity(Severity.FAIL)
									.operator(Operator.LESS_THAN)
									.value(1.25)
									.build())
							.scope(Scope.PER_INTERVAL)
							.build()
						))
				.build();

		return Project.builder()
				.name("MyProject")
				.addSlaProfiles(slaProfile)
				.build();
	}

	@Test
	public void readSlaProfilesOnlyRequired() throws IOException {
		final Project expectedProject = getUserPathsOnlyRequired();
		assertNotNull(expectedProject);

		read("test-slaprofiles-only-required", expectedProject);
	}

	@Test
	public void readSlaProfilesRequiredAndOptional() throws IOException {
		final Project expectedProject = getUserPathsRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-slaprofiles-required-and-optional", expectedProject);
	}

	@Test
	public void writeSlaProfilesOnlyRequired() throws IOException {
		final Project expectedProject = getUserPathsOnlyRequired();
		assertNotNull(expectedProject);

		write("test-slaprofiles-only-required", expectedProject);
	}

	@Test
	public void writeSlaProfilesRequiredAndOptional() throws IOException {
		final Project expectedProject = getUserPathsRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-slaprofiles-required-and-optional", expectedProject);
	}

	@Test
	public void percentileBoundsAreAcceptedOnLoad() throws IOException {
		final ProjectDescriptor p1 = readYaml(percentileProject(1));
		final ProjectDescriptor p99 = readYaml(percentileProject(99));

		assertTrue(validationOf(p1).isValid());
		assertTrue(validationOf(p99).isValid());
		assertEquals(Integer.valueOf(1), percent(p1));
		assertEquals(Integer.valueOf(99), percent(p99));
	}

	@Test
	public void percentileOutsideOneToNinetyNineIsRejectedOnLoad() throws IOException {
		final ProjectDescriptor p0 = readYaml(percentileProject(0));
		final ProjectDescriptor p100 = readYaml(percentileProject(100));

		assertViolations(validationOf(p0), invalidValue("0", PERCENT, "must be between 1 and 99."));
		assertViolations(validationOf(p100), invalidValue("100", PERCENT, "must be between 1 and 99."));
		assertEquals(Integer.valueOf(0), percent(p0));
		assertEquals(Integer.valueOf(100), percent(p100));
	}
}
