package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.sla.SlaProfile;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.KPI;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold.Scope;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Operator;
import com.neotys.neoload.model.v3.project.sla.SlaThresholdCondition.Severity;


public class IOSlaProfilesTest extends AbstractIOElementsTest {

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

		final Project project = Project.builder()
				.name("MyProject")
				.addSlaProfiles(slaProfile)
				.build();

		return project;
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

		final Project project = Project.builder()
				.name("MyProject")
				.addSlaProfiles(slaProfile)
				.build();

		return project;
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
}
