package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.Apm;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.CustomLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.DynatraceAnomalyRule;
import com.neotys.neoload.model.v3.project.scenario.ImmutableCustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.ImmutableLoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.MonitoringParameters;
import com.neotys.neoload.model.v3.project.scenario.PeakLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy.Peak;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.RendezvousPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.scenario.StartAfter;
import com.neotys.neoload.model.v3.project.scenario.StopAfter;
import com.neotys.neoload.model.v3.project.scenario.WhenRelease;

import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static org.junit.Assert.assertNotNull;


public class IOScenariosTest extends AbstractIOElementsTest {

	private static Scenario getScenarioOnlyRequired() {
		final PopulationPolicy population1 = PopulationPolicy.builder()
				.name("MyPopulation1")
				.loadPolicy(ConstantLoadPolicy.builder()
						.users(500)
						.build())
				.build();

		final PopulationPolicy population2 = PopulationPolicy.builder()
				.name("MyPopulation2")
				.loadPolicy(RampupLoadPolicy.builder()
						.minUsers(1)
						.incrementUsers(10)
						.incrementEvery(LoadDuration.builder()
								.value(5)
								.type(LoadDuration.Type.TIME)
								.build())
						.build())
				.build();

		final PopulationPolicy population3 = PopulationPolicy.builder()
				.name("MyPopulation3")
				.loadPolicy(PeaksLoadPolicy.builder()
						.minimum(PeakLoadPolicy.builder()
								.users(100)
								.duration(LoadDuration.builder()
										.value(120)
										.type(LoadDuration.Type.TIME)
										.build())
								.build())
						.maximum(PeakLoadPolicy.builder()
								.users(500)
								.duration(LoadDuration.builder()
										.value(120)
										.type(LoadDuration.Type.TIME)
										.build())
								.build())
						.start(Peak.MINIMUM)
						.build())
				.build();

		ImmutableLoadDuration loadDuration = LoadDuration.builder()
				.value(100)
				.type(LoadDuration.Type.TIME)
				.build();
		ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
				.when(loadDuration)
				.users(300)
				.build();

		final PopulationPolicy population4 = PopulationPolicy.builder()
				.name("MyPopulation4")
				.loadPolicy(CustomLoadPolicy.builder()
						.steps(Collections.singletonList(customPolicyStep))
						.build())
				.build();

		return Scenario.builder()
				.name("MyScenario")
				.addPopulations(population1, population2, population3, population4)
				.build();
	}

	private static Scenario getScenarioRequiredAndOptional() {
		final PopulationPolicy population1 = PopulationPolicy.builder()
				.name("MyPopulation1")
				.loadPolicy(ConstantLoadPolicy.builder()
						.users(500)
						.duration(LoadDuration.builder()
								.value(900)
								.type(LoadDuration.Type.TIME)
								.build())
						.startAfter(StartAfter.builder()
								.value(30)
								.type(StartAfter.Type.TIME)
								.build())
						.rampup(60)
						.stopAfter(StopAfter.builder()
								.value(30)
								.type(StopAfter.Type.TIME)
								.build())
						.build())
				.build();

		final PopulationPolicy population2 = PopulationPolicy.builder()
				.name("MyPopulation2")
				.loadPolicy(RampupLoadPolicy.builder()
						.minUsers(1)
						.maxUsers(500)
						.incrementUsers(10)
						.incrementEvery(LoadDuration.builder()
								.value(1)
								.type(LoadDuration.Type.ITERATION)
								.build())
						.duration(LoadDuration.builder()
								.value(15)
								.type(LoadDuration.Type.ITERATION)
								.build())
						.startAfter(StartAfter.builder()
								.value("MyPopulation1")
								.type(StartAfter.Type.POPULATION)
								.build())
						.rampup(90)
						.stopAfter(StopAfter.builder()
								.type(StopAfter.Type.CURRENT_ITERATION)
								.build())
						.build())
				.build();

		final PopulationPolicy population3 = PopulationPolicy.builder()
				.name("MyPopulation3")
				.loadPolicy(PeaksLoadPolicy.builder()
						.minimum(PeakLoadPolicy.builder()
								.users(100)
								.duration(LoadDuration.builder()
										.value(1)
										.type(LoadDuration.Type.ITERATION)
										.build())
								.build())
						.maximum(PeakLoadPolicy.builder()
								.users(500)
								.duration(LoadDuration.builder()
										.value(1)
										.type(LoadDuration.Type.ITERATION)
										.build())
								.build())
						.start(Peak.MAXIMUM)
						.duration(LoadDuration.builder()
								.value(15)
								.type(LoadDuration.Type.ITERATION)
								.build())
						.startAfter(StartAfter.builder()
								.value(60)
								.type(StartAfter.Type.TIME)
								.build())
						.rampup(15)
						.stopAfter(StopAfter.builder()
								.value(60)
								.type(StopAfter.Type.TIME)
								.build())
						.build())
				.build();

		ImmutableLoadDuration loadDuration = LoadDuration.builder()
				.value(100)
				.type(LoadDuration.Type.TIME)
				.build();
		ImmutableCustomPolicyStep customPolicyStep = CustomPolicyStep.builder()
				.when(loadDuration)
				.users(300)
				.build();

		final PopulationPolicy population4 = PopulationPolicy.builder()
				.name("MyPopulation4")
				.loadPolicy(CustomLoadPolicy.builder()
						.steps(Collections.singletonList(customPolicyStep))
						.startAfter(StartAfter.builder()
								.value(30)
								.type(StartAfter.Type.TIME)
								.build())
						.rampup(60)
						.stopAfter(StopAfter.builder()
								.value(30)
								.type(StopAfter.Type.TIME)
								.build())
						.build())
				.build();

		return Scenario.builder()
				.name("MyScenario")
				.description("My scenario with 4 populations")
				.slaProfile("MySlaProfile")
				.addPopulations(population1, population2, population3, population4)
				.apm(Apm.builder()
						.addDynatraceTags("myDynatraceTag")
						.addDynatraceAnomalyRules(DynatraceAnomalyRule.builder()
								.metricId("builtin:host.cpu.usage")
								.operator("ABOVE")
								.value("90")
								.severity("PERFORMANCE")
								.build())
						.build())
				.addRendezvousPolicies(
						RendezvousPolicy.builder()
								.name("rdv")
								.build(),
						RendezvousPolicy.builder()
								.name("rdv_manual")
								.when(WhenRelease.builder()
										.type(WhenRelease.Type.MANUAL)
										.value("manual")
										.build())
								.timeout(900)
								.build(),
						RendezvousPolicy.builder()
								.name("rdv_percentage")
								.when(WhenRelease.builder()
										.type(WhenRelease.Type.PERCENTAGE)
										.value("50")
										.build())
								.build(),
						RendezvousPolicy.builder()
								.name("rdv_vu_number")
								.when(WhenRelease.builder()
										.type(WhenRelease.Type.VU_NUMBER)
										.value("200")
										.build())
								.timeout(100)
								.build()
				)
				.monitoringParameters(MonitoringParameters.builder().beforeFirstVu(6).afterLastVus(99).build())
				.isStoredVariables(true)
				.excludedUrls(List.of(".*\\.abcd"))
				.build();
	}

	@Test
	public void readScenariosOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getScenarioOnlyRequired());
		assertNotNull(expectedProject);

		read("test-scenarios-only-required", expectedProject);
	}

	@Test
	public void readScenariosRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getScenarioRequiredAndOptional());
		assertNotNull(expectedProject);

		read("test-scenarios-required-and-optional", expectedProject);
	}

	@Test
	public void writeScenariosOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getScenarioOnlyRequired());
		assertNotNull(expectedProject);

		write("test-scenarios-only-required", expectedProject);
	}

	@Test
	public void writeScenariosRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getScenarioRequiredAndOptional());
		assertNotNull(expectedProject);

		write("test-scenarios-required-and-optional", expectedProject);
	}
}
