package com.neotys.neoload.model.v3.util;

import static org.junit.Assert.assertEquals;

import com.neotys.neoload.model.v3.project.scenario.*;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration.Type;
import java.util.Arrays;
import org.junit.Test;

public class ScenarioUtilsTest {
	// Users
	private static final int MIN_USERS = 10;
	private static final int MAX_USERS = 50;
	
	// Load Duration
	private static final LoadDuration DURATION_30_SECONDS = LoadDuration.builder()
			.type(Type.TIME)
			.value(30)
			.build();
	private static final LoadDuration DURATION_60_SECONDS = LoadDuration.builder()
			.type(Type.TIME)
			.value(60)
			.build();
	private static final LoadDuration DURATION_120_SECONDS = LoadDuration.builder()
			.type(Type.TIME)
			.value(120)
			.build();
	private static final LoadDuration DURATION_50_ITERATIONS = LoadDuration.builder()
			.type(Type.ITERATION)
			.value(50)
			.build();
	private static final LoadDuration DURATION_100_ITERATIONS = LoadDuration.builder()
			.type(Type.ITERATION)
			.value(100)
			.build();
	
	// Population Policy
	// Constant Load Policy
	private static final PopulationPolicy POPULATION_CONSTANT_MAX_USERS = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(ConstantLoadPolicy.builder()
					.users(MAX_USERS)
					.build())
			.build();
	private static final PopulationPolicy POPULATION_CONSTANT_MAX_USERS_AND_60_SECONDS = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(ConstantLoadPolicy.builder()
					.users(MAX_USERS)
					.duration(DURATION_60_SECONDS)
					.build())
			.build();
	private static final PopulationPolicy POPULATION_CONSTANT_MAX_USERS_WITH_START_STOP_120_SECONDS_TOTAL = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(ConstantLoadPolicy.builder()
					.users(MAX_USERS)
					.duration(DURATION_60_SECONDS)
					.startAfter(StartAfter.builder().type(StartAfter.Type.TIME).value(30).build())
					.stopAfter(StopAfter.builder().type(StopAfter.Type.TIME).value(30).build())
					.build())
			.build();
	private static final PopulationPolicy POPULATION_CONSTANT_MAX_USERS_AND_100_ITERATIONS = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(ConstantLoadPolicy.builder()
					.users(MAX_USERS)
					.duration(DURATION_100_ITERATIONS)
					.build())
			.build();
	// Peaks Load Policy
	private static final PopulationPolicy POPULATION_PEAKS_MAX_USERS = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(PeaksLoadPolicy.builder()
					.minimum(PeakLoadPolicy.builder()
							.users(MIN_USERS)
							.build())
					.maximum(PeakLoadPolicy.builder()
							.users(MAX_USERS)
							.build())
					.build())
			.build();
	private static final PopulationPolicy POPULATION_PEAKS_MAX_USERS_AND_60_SECONDS = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(PeaksLoadPolicy.builder()
					.minimum(PeakLoadPolicy.builder()
							.users(MIN_USERS)
							.build())
					.maximum(PeakLoadPolicy.builder()
							.users(MAX_USERS)
							.build())
					.duration(DURATION_60_SECONDS)
					.build())
			.build();

	private static final PopulationPolicy POPULATION_PEAKS_MAX_USERS_WITH_START_STOP_120_SECONDS_TOTAL = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(PeaksLoadPolicy.builder()
					.minimum(PeakLoadPolicy.builder()
							.users(MIN_USERS)
							.build())
					.maximum(PeakLoadPolicy.builder()
							.users(MAX_USERS)
							.build())
					.duration(DURATION_60_SECONDS)
					.startAfter(StartAfter.builder().type(StartAfter.Type.TIME).value(30).build())
					.stopAfter(StopAfter.builder().type(StopAfter.Type.TIME).value(30).build())
					.build())
			.build();
	private static final PopulationPolicy POPULATION_PEAKS_MAX_USERS_AND_100_ITERATIONS = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(PeaksLoadPolicy.builder()
					.minimum(PeakLoadPolicy.builder()
							.users(MIN_USERS)
							.build())
					.maximum(PeakLoadPolicy.builder()
							.users(MAX_USERS)
							.build())
					.duration(DURATION_100_ITERATIONS)
					.build())
			.build();
	// Rampup Load Policy
	private static final PopulationPolicy POPULATION_RAMPUP = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(RampupLoadPolicy.builder()
					.minUsers(MIN_USERS)
					.incrementEvery(LoadDuration.builder()
							.type(Type.TIME)
							.value(1)
							.build())
					.incrementUsers(1)
					.build())
			.build();
	private static final PopulationPolicy POPULATION_RAMPUP_60_SECONDS = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(RampupLoadPolicy.builder()
					.minUsers(MIN_USERS)
					.incrementEvery(LoadDuration.builder()
							.type(Type.TIME)
							.value(1)
							.build())
					.incrementUsers(1)
					.duration(DURATION_60_SECONDS)
					.build())
			.build();
	private static final PopulationPolicy POPULATION_RAMPUP_100_ITERATIONS = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(RampupLoadPolicy.builder()
					.minUsers(MIN_USERS)
					.incrementEvery(LoadDuration.builder()
							.type(Type.TIME)
							.value(1)
							.build())
					.incrementUsers(1)
					.duration(DURATION_100_ITERATIONS)
					.build())
			.build();
	private static final PopulationPolicy POPULATION_RAMPUP_MAX_USERS = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(RampupLoadPolicy.builder()
					.minUsers(MIN_USERS)
					.incrementEvery(LoadDuration.builder()
							.type(Type.TIME)
							.value(1)
							.build())
					.incrementUsers(1)
					.maxUsers(MAX_USERS)
					.build())
			.build();
	private static final PopulationPolicy POPULATION_RAMPUP_MAX_USERS_AND_60_SECONDS = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(RampupLoadPolicy.builder()
					.minUsers(MIN_USERS)
					.incrementEvery(LoadDuration.builder()
							.type(Type.TIME)
							.value(1)
							.build())
					.incrementUsers(1)
					.maxUsers(MAX_USERS)
					.duration(DURATION_60_SECONDS)
					.build())
			.build();

	private static final PopulationPolicy POPULATION_RAMPUP_MAX_USERS_WITH_START_STOP_120_SECONDS_TOTAL = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(RampupLoadPolicy.builder()
					.minUsers(MIN_USERS)
					.incrementEvery(LoadDuration.builder()
							.type(Type.TIME)
							.value(1)
							.build())
					.incrementUsers(1)
					.maxUsers(MAX_USERS)
					.duration(DURATION_60_SECONDS)
					.startAfter(StartAfter.builder().type(StartAfter.Type.TIME).value(30).build())
					.stopAfter(StopAfter.builder().type(StopAfter.Type.TIME).value(30).build())
					.build())
			.build();
	private static final PopulationPolicy POPULATION_RAMPUP_MAX_USERS_AND_100_ITERATIONS = PopulationPolicy.builder()
			.name("MyPopulation")
			.loadPolicy(RampupLoadPolicy.builder()
					.minUsers(MIN_USERS)
					.incrementEvery(LoadDuration.builder()
							.type(Type.TIME)
							.value(1)
							.build())
					.incrementUsers(1)
					.maxUsers(MAX_USERS)
					.duration(DURATION_100_ITERATIONS)
					.build())
			.build();
	
	// Expected Load Summary
	private static final LoadSummary LOAD_SUMMARY = LoadSummary.builder()
			.build();
	private static final LoadSummary LOAD_SUMMARY_MAX_USERS = LoadSummary.builder()
			.maxUsers(MAX_USERS)
			.build();
	private static final LoadSummary LOAD_SUMMARY_MAX_USERS_AND_60_SECONDS = LoadSummary.builder()
			.maxUsers(MAX_USERS)
			.duration(DURATION_60_SECONDS)
			.build();
	private static final LoadSummary LOAD_SUMMARY_MAX_USERS_AND_120_SECONDS = LoadSummary.builder()
			.maxUsers(MAX_USERS)
			.duration(DURATION_120_SECONDS)
			.build();
	private static final LoadSummary LOAD_SUMMARY_MAX_USERS_AND_100_ITERATIONS = LoadSummary.builder()
			.maxUsers(MAX_USERS)
			.duration(DURATION_100_ITERATIONS)
			.build();
	
	@Test
	public void getSummaryForConstantLoadPolicy() {
		assertEquals(LOAD_SUMMARY_MAX_USERS, ScenarioUtils.getSummary(POPULATION_CONSTANT_MAX_USERS));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_60_SECONDS, ScenarioUtils.getSummary(POPULATION_CONSTANT_MAX_USERS_AND_60_SECONDS));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_120_SECONDS, ScenarioUtils.getSummary(POPULATION_CONSTANT_MAX_USERS_WITH_START_STOP_120_SECONDS_TOTAL));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_100_ITERATIONS, ScenarioUtils.getSummary(POPULATION_CONSTANT_MAX_USERS_AND_100_ITERATIONS));
	}

	@Test
	public void getSummaryForPeaksLoadPolicy() {
		assertEquals(LOAD_SUMMARY_MAX_USERS, ScenarioUtils.getSummary(POPULATION_PEAKS_MAX_USERS));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_60_SECONDS, ScenarioUtils.getSummary(POPULATION_PEAKS_MAX_USERS_AND_60_SECONDS));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_120_SECONDS, ScenarioUtils.getSummary(POPULATION_PEAKS_MAX_USERS_WITH_START_STOP_120_SECONDS_TOTAL));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_100_ITERATIONS, ScenarioUtils.getSummary(POPULATION_PEAKS_MAX_USERS_AND_100_ITERATIONS));
	}
	
	@Test
	public void getSummaryForRampupLoadPolicy() {
		// Rampup Load Policy without maxUsers
		assertEquals(LOAD_SUMMARY, ScenarioUtils.getSummary(POPULATION_RAMPUP));
		LoadSummary expecedLoadSummary = LoadSummary.builder()
				.maxUsers(69)
				.duration(DURATION_60_SECONDS)
				.build();
		assertEquals(expecedLoadSummary, ScenarioUtils.getSummary(POPULATION_RAMPUP_60_SECONDS));
		expecedLoadSummary = LoadSummary.builder()
				.maxUsers(109)
				.duration(DURATION_100_ITERATIONS)
				.build();
		assertEquals(expecedLoadSummary, ScenarioUtils.getSummary(POPULATION_RAMPUP_100_ITERATIONS));
		
		// Rampup Load Policy with maxUsers
		assertEquals(LOAD_SUMMARY_MAX_USERS, ScenarioUtils.getSummary(POPULATION_RAMPUP_MAX_USERS));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_60_SECONDS, ScenarioUtils.getSummary(POPULATION_RAMPUP_MAX_USERS_AND_60_SECONDS));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_120_SECONDS, ScenarioUtils.getSummary(POPULATION_RAMPUP_MAX_USERS_WITH_START_STOP_120_SECONDS_TOTAL));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_100_ITERATIONS, ScenarioUtils.getSummary(POPULATION_RAMPUP_MAX_USERS_AND_100_ITERATIONS));
	}
	
	@Test
	public void getSummaryForCustomLoadPolicy() {
		PopulationPolicy populationPolicy = PopulationPolicy.builder()
				.name("MyPopulation")
				.loadPolicy(CustomLoadPolicy.builder()
						.addSteps(CustomPolicyStep.builder()
								.users(MAX_USERS)
								.when(DURATION_30_SECONDS)
								.build())
						.addSteps(CustomPolicyStep.builder()
								.users(MIN_USERS)
								.when(DURATION_60_SECONDS)
								.build())
						.build())
				.build();
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_60_SECONDS, ScenarioUtils.getSummary(populationPolicy));

		final ImmutablePopulationPolicy populationPolicyWithStartStop = PopulationPolicy.builder()
				.from(populationPolicy)
				.loadPolicy(ImmutableCustomLoadPolicy.copyOf((CustomLoadPolicy) populationPolicy.getLoadPolicy())
						.withStartAfter(StartAfter.builder().type(StartAfter.Type.TIME).value(30).build())
						.withStopAfter(StopAfter.builder().type(StopAfter.Type.TIME).value(30).build())
				).build();
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_120_SECONDS, ScenarioUtils.getSummary(populationPolicyWithStartStop));

		populationPolicy = PopulationPolicy.builder()
				.name("MyPopulation")
				.loadPolicy(CustomLoadPolicy.builder()
						.addSteps(CustomPolicyStep.builder()
								.users(MAX_USERS)
								.when(DURATION_50_ITERATIONS)
								.build())
						.addSteps(CustomPolicyStep.builder()
								.users(MIN_USERS)
								.when(DURATION_100_ITERATIONS)
								.build())
						.build())
				.build();
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_100_ITERATIONS, ScenarioUtils.getSummary(populationPolicy));
	}
	
	@Test
	public void getSummaryFromPopulationPolicies() {
		LoadSummary expecedLoadSummary = LoadSummary.builder()
				.maxUsers(0)
				.duration(LoadDuration.builder()
						.type(Type.TIME)
						.value(0)
						.build())
				.build();
		assertEquals(expecedLoadSummary, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList()));
		
		// Constant Load Policy
		assertEquals(LOAD_SUMMARY_MAX_USERS, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_CONSTANT_MAX_USERS)));
		assertEquals(LoadSummary.builder().maxUsers(2 * MAX_USERS).build(), ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_CONSTANT_MAX_USERS, POPULATION_CONSTANT_MAX_USERS_AND_60_SECONDS)));
		assertEquals(LoadSummary.builder().maxUsers(2 * MAX_USERS).build(), ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_CONSTANT_MAX_USERS, POPULATION_CONSTANT_MAX_USERS_AND_100_ITERATIONS)));
		assertEquals(LoadSummary.builder().maxUsers(3 * MAX_USERS).build(), ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_CONSTANT_MAX_USERS, POPULATION_CONSTANT_MAX_USERS_AND_60_SECONDS, POPULATION_CONSTANT_MAX_USERS_AND_100_ITERATIONS)));
		assertEquals(LoadSummary.builder().maxUsers(2 * MAX_USERS).build(), ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_CONSTANT_MAX_USERS_AND_60_SECONDS, POPULATION_CONSTANT_MAX_USERS_AND_100_ITERATIONS)));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_60_SECONDS, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_CONSTANT_MAX_USERS_AND_60_SECONDS)));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_100_ITERATIONS, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_CONSTANT_MAX_USERS_AND_100_ITERATIONS)));

		// Peaks Load Policy
		assertEquals(LOAD_SUMMARY_MAX_USERS, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_PEAKS_MAX_USERS)));
		assertEquals(LoadSummary.builder().maxUsers(2 * MAX_USERS).build(), ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_PEAKS_MAX_USERS, POPULATION_PEAKS_MAX_USERS_AND_60_SECONDS)));
		assertEquals(LoadSummary.builder().maxUsers(2 * MAX_USERS).build(), ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_PEAKS_MAX_USERS, POPULATION_PEAKS_MAX_USERS_AND_100_ITERATIONS)));
		assertEquals(LoadSummary.builder().maxUsers(3 * MAX_USERS).build(), ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_PEAKS_MAX_USERS, POPULATION_PEAKS_MAX_USERS_AND_60_SECONDS, POPULATION_PEAKS_MAX_USERS_AND_100_ITERATIONS)));
		assertEquals(LoadSummary.builder().maxUsers(2 * MAX_USERS).build(), ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_PEAKS_MAX_USERS_AND_60_SECONDS, POPULATION_PEAKS_MAX_USERS_AND_100_ITERATIONS)));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_60_SECONDS, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_PEAKS_MAX_USERS_AND_60_SECONDS)));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_100_ITERATIONS, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_PEAKS_MAX_USERS_AND_100_ITERATIONS)));

		// Rampup Load Policy
		assertEquals(LOAD_SUMMARY, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP)));
		assertEquals(LOAD_SUMMARY, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP, POPULATION_RAMPUP_60_SECONDS)));
		assertEquals(LOAD_SUMMARY, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP, POPULATION_RAMPUP_100_ITERATIONS)));
		assertEquals(LOAD_SUMMARY, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP, POPULATION_RAMPUP_60_SECONDS, POPULATION_RAMPUP_100_ITERATIONS)));
		assertEquals(LOAD_SUMMARY, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP, POPULATION_RAMPUP_60_SECONDS, POPULATION_RAMPUP_100_ITERATIONS, POPULATION_RAMPUP_MAX_USERS, POPULATION_RAMPUP_MAX_USERS_AND_60_SECONDS, POPULATION_RAMPUP_MAX_USERS_AND_100_ITERATIONS)));
		expecedLoadSummary = LoadSummary.builder()
				.maxUsers(69)
				.duration(DURATION_60_SECONDS)
				.build();
		assertEquals(expecedLoadSummary, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP_60_SECONDS)));
		expecedLoadSummary = LoadSummary.builder()
				.maxUsers(69 + MAX_USERS)
				.duration(DURATION_60_SECONDS)
				.build();
		assertEquals(expecedLoadSummary, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP_60_SECONDS, POPULATION_RAMPUP_MAX_USERS_AND_60_SECONDS)));
		expecedLoadSummary = LoadSummary.builder()
				.maxUsers(109)
				.duration(DURATION_100_ITERATIONS)
				.build();
		assertEquals(expecedLoadSummary, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP_100_ITERATIONS)));
		expecedLoadSummary = LoadSummary.builder()
				.maxUsers(109 + MAX_USERS)
				.build();
		assertEquals(expecedLoadSummary, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP_100_ITERATIONS, POPULATION_RAMPUP_MAX_USERS_AND_100_ITERATIONS)));
		assertEquals(LOAD_SUMMARY_MAX_USERS, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP_MAX_USERS)));
		assertEquals(LoadSummary.builder().maxUsers(2 * MAX_USERS).build(), ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP_MAX_USERS, POPULATION_RAMPUP_MAX_USERS_AND_60_SECONDS)));
		assertEquals(LoadSummary.builder().maxUsers(2 * MAX_USERS).build(), ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP_MAX_USERS, POPULATION_RAMPUP_MAX_USERS_AND_100_ITERATIONS)));
		assertEquals(LoadSummary.builder().maxUsers(3 * MAX_USERS).build(), ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP_MAX_USERS, POPULATION_RAMPUP_MAX_USERS_AND_60_SECONDS, POPULATION_RAMPUP_MAX_USERS_AND_100_ITERATIONS)));
		assertEquals(LoadSummary.builder().maxUsers(2 * MAX_USERS).build(), ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP_MAX_USERS_AND_60_SECONDS, POPULATION_RAMPUP_MAX_USERS_AND_100_ITERATIONS)));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_60_SECONDS, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP_MAX_USERS_AND_60_SECONDS)));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_100_ITERATIONS, ScenarioUtils.getSummaryFromPopulationPolicies(Arrays.asList(POPULATION_RAMPUP_MAX_USERS_AND_100_ITERATIONS)));
	}
	
	@Test
	public void getSummaryFromLoadSummaries() {
		LoadSummary expecedLoadSummary = LoadSummary.builder()
				.maxUsers(0)
				.duration(LoadDuration.builder()
						.type(Type.TIME)
						.value(0)
						.build())
				.build();
		assertEquals(expecedLoadSummary, ScenarioUtils.getSummaryFromLoadSummaries(Arrays.asList()));
		
		assertEquals(LOAD_SUMMARY_MAX_USERS, ScenarioUtils.getSummaryFromLoadSummaries(Arrays.asList(LOAD_SUMMARY_MAX_USERS)));
		assertEquals(LoadSummary.builder().maxUsers(2 * MAX_USERS).build(), ScenarioUtils.getSummaryFromLoadSummaries(Arrays.asList(LOAD_SUMMARY_MAX_USERS, LOAD_SUMMARY_MAX_USERS_AND_60_SECONDS)));
		assertEquals(LoadSummary.builder().maxUsers(2 * MAX_USERS).build(), ScenarioUtils.getSummaryFromLoadSummaries(Arrays.asList(LOAD_SUMMARY_MAX_USERS, LOAD_SUMMARY_MAX_USERS_AND_100_ITERATIONS)));
		assertEquals(LoadSummary.builder().maxUsers(3 * MAX_USERS).build(), ScenarioUtils.getSummaryFromLoadSummaries(Arrays.asList(LOAD_SUMMARY_MAX_USERS, LOAD_SUMMARY_MAX_USERS_AND_60_SECONDS, LOAD_SUMMARY_MAX_USERS_AND_100_ITERATIONS)));
		assertEquals(LoadSummary.builder().maxUsers(2 * MAX_USERS).build(), ScenarioUtils.getSummaryFromLoadSummaries(Arrays.asList(LOAD_SUMMARY_MAX_USERS_AND_60_SECONDS, LOAD_SUMMARY_MAX_USERS_AND_100_ITERATIONS)));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_60_SECONDS, ScenarioUtils.getSummaryFromLoadSummaries(Arrays.asList(LOAD_SUMMARY_MAX_USERS_AND_60_SECONDS)));
		assertEquals(LOAD_SUMMARY_MAX_USERS_AND_100_ITERATIONS, ScenarioUtils.getSummaryFromLoadSummaries(Arrays.asList(LOAD_SUMMARY_MAX_USERS_AND_100_ITERATIONS)));
	}
}
