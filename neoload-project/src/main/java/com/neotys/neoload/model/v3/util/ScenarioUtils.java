package com.neotys.neoload.model.v3.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Optional;

import com.google.common.primitives.Doubles;
import com.neotys.neoload.model.v3.project.scenario.ConstantLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.CustomLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration.Type;
import com.neotys.neoload.model.v3.project.scenario.LoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PeaksLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.RampupLoadPolicy;

public final class ScenarioUtils {
	private static final int UNDETERMINED_MAX_VUS = -1;
	private static final LoadSummary DEFAULT_SUMMARY = LoadSummary.builder()
			.maxUsers(Optional.of(0))
			.duration(Optional.of(LoadDuration.builder()
					.type(Type.TIME)
					.value(0)
					.build()))
			.build();
	
	private ScenarioUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	public static LoadSummary getSummary(final PopulationPolicy populationPolicy) {
		checkNotNull(populationPolicy);
		
		final LoadPolicy loadPolicy = populationPolicy.getLoadPolicy();
		final int maxUsers = getMaxUsers(loadPolicy);
		final LoadDuration duration = getDuration(loadPolicy);
		return LoadSummary.builder()
				.maxUsers(fromMaxUsers(maxUsers))
				.duration(fromLoadDuration(duration))
				.build();
	}
	
	public static LoadSummary getSummaryFromPopulationPolicies(final List<PopulationPolicy> populationPolicies) {
		checkNotNull(populationPolicies);
		
		if (populationPolicies.isEmpty()) {
			return DEFAULT_SUMMARY;
		}

		if (populationPolicies.size() == 1) {
			return getSummary(populationPolicies.get(0));
		}
		
		int maxUsers = DEFAULT_SUMMARY.getMaxUsers().get();
		LoadDuration duration = DEFAULT_SUMMARY.getDuration().get();
		for (final PopulationPolicy populationPolicy : populationPolicies) {
			final LoadPolicy loadPolicy = populationPolicy.getLoadPolicy();
			
			maxUsers = computeMaxUsers(loadPolicy, maxUsers);
			duration = computeDuration(loadPolicy, duration);			
		}
		
		return LoadSummary.builder()
				.maxUsers(fromMaxUsers(maxUsers))
				.duration(fromLoadDuration(duration))
				.build();
	}

	public static LoadSummary getSummaryFromLoadSummaries(final List<LoadSummary> loadSummaries) {
		checkNotNull(loadSummaries);
		
		if (loadSummaries.isEmpty()) {
			return DEFAULT_SUMMARY;
		}

		if (loadSummaries.size() == 1) {
			return loadSummaries.get(0);
		}
		
		int maxUsers = DEFAULT_SUMMARY.getMaxUsers().get();
		LoadDuration duration = DEFAULT_SUMMARY.getDuration().get();
		for (final LoadSummary loadSummary : loadSummaries) {
			maxUsers = computeMaxUsers(loadSummary, maxUsers);
			duration = computeDuration(loadSummary, duration);			
		}
		
		return LoadSummary.builder()
				.maxUsers(fromMaxUsers(maxUsers))
				.duration(fromLoadDuration(duration))
				.build();
	}
	
	protected static int computeMaxUsers(final LoadPolicy loadPolicy, final int maxUsers) {
		if (maxUsers == UNDETERMINED_MAX_VUS) { // If max vus is -1 -> Undetermined max vus
			return UNDETERMINED_MAX_VUS;
		}		
		return computeMaxUsers(getMaxUsers(loadPolicy), maxUsers);
	}
	
	protected static int computeMaxUsers(final LoadSummary loadSummary, final int maxUsers) {
		if (maxUsers == UNDETERMINED_MAX_VUS) { // If max vus is -1 -> Undetermined max vus
			return UNDETERMINED_MAX_VUS;
		}
		return computeMaxUsers(toMaxUsers(loadSummary.getMaxUsers()), maxUsers);
	}

	private static int computeMaxUsers(final int currentMaxUsers, final int maxUsers) {
		if (currentMaxUsers == UNDETERMINED_MAX_VUS) { // If current max vus is -1 -> Undetermined max vus
			return UNDETERMINED_MAX_VUS;
		}		
		
		return maxUsers + currentMaxUsers;
	}

	protected static LoadDuration computeDuration(final LoadPolicy loadPolicy, final LoadDuration maxDuration) {
		if (maxDuration == null) { // If max duration is null -> Unlimited load duration
			return null;
		}		
		return computeDuration(getDuration(loadPolicy), maxDuration);
	}

	protected static LoadDuration computeDuration(final LoadSummary loadSummary, final LoadDuration maxDuration) {
		if (maxDuration == null) { // If max duration is null -> Unlimited load duration
			return null;
		}
		return computeDuration(toLoadDuration(loadSummary.getDuration()), maxDuration);
	}
	
	private static LoadDuration computeDuration(final LoadDuration currentDuration, final LoadDuration maxDuration) {
		if (currentDuration == null) { // If current duration is null -> Unlimited load duration
			return null;
		}		
		final LoadDuration.Type currentDurationType = currentDuration.getType();
		if (currentDurationType != LoadDuration.Type.TIME) { // If type is iteration -> Undetermined load duration
			return null;
		}

		return LoadDuration.builder()
				.type(Type.TIME)
				.value(Math.max(currentDuration.getValue(), maxDuration.getValue()))
				.build();
	}

	protected static int getMaxUsers(final LoadPolicy loadPolicy) {
		checkNotNull(loadPolicy);
		
		int maxUsers = 0;
		if (loadPolicy instanceof ConstantLoadPolicy) {
			maxUsers = getMaxUsers((ConstantLoadPolicy) loadPolicy);
		} else if (loadPolicy instanceof PeaksLoadPolicy) {
			maxUsers = getMaxUsers((PeaksLoadPolicy) loadPolicy);
		} else if (loadPolicy instanceof RampupLoadPolicy) {
			maxUsers = getMaxUsers((RampupLoadPolicy)loadPolicy);
		} else if (loadPolicy instanceof CustomLoadPolicy) {
			maxUsers = getMaxUsers((CustomLoadPolicy)loadPolicy);
		}
		return maxUsers;
	}
	
	protected static LoadDuration getDuration(final LoadPolicy loadPolicy) {
		checkNotNull(loadPolicy);
		
		LoadDuration maxDuration = null;
		if (loadPolicy instanceof ConstantLoadPolicy || loadPolicy instanceof PeaksLoadPolicy || loadPolicy instanceof RampupLoadPolicy) {
			maxDuration = loadPolicy.getDuration();
		} else if (loadPolicy instanceof CustomLoadPolicy) {
			maxDuration = getDuration((CustomLoadPolicy)loadPolicy);
		}
		return maxDuration;
	}

	private static int getMaxUsers(final ConstantLoadPolicy loadPolicy) {
		return loadPolicy.getUsers();
	}
	
	private static int getMaxUsers(final PeaksLoadPolicy loadPolicy) {
		return loadPolicy.getMaximum().getUsers();
	}
	
	private static int getMaxUsers(final RampupLoadPolicy loadPolicy) {
		final Integer maxUsers = loadPolicy.getMaxUsers();
		final LoadDuration duration = loadPolicy.getDuration();
		if (duration != null) {
			final int minUsers = loadPolicy.getMinUsers();
			final int incrementUsers = loadPolicy.getIncrementUsers();
			final Integer delayIncrement = loadPolicy.getIncrementEvery().getValue();
			return computeMaxUsersFromRampupLoadPolicy(minUsers, incrementUsers, delayIncrement, maxUsers, duration.getValue());
		} 
		return (maxUsers != null) ? maxUsers : UNDETERMINED_MAX_VUS;		
	}
	
	private static int getMaxUsers(final CustomLoadPolicy loadPolicy) {
		return loadPolicy.getSteps().stream()
				.mapToInt(CustomPolicyStep::getUsers)
				.max()
				.getAsInt();
	}
	
	private static LoadDuration getDuration(final CustomLoadPolicy loadPolicy) {
		final List<CustomPolicyStep> steps = loadPolicy.getSteps();
		return steps.get(steps.size() - 1).getWhen();
	}

	private static int computeMaxUsersFromRampupLoadPolicy(final int minUsers, final int incrementUsers, final Integer delayIncrement, Integer maxUsers, final Integer duration) {
		if (duration == -1) return -1;
		if (maxUsers == null) maxUsers = Integer.MAX_VALUE;
		int ratio = (duration / delayIncrement);
		if (ratio >= 1) {
			final double perfectRatio = (double) ratio * delayIncrement;
			if (Doubles.compare(perfectRatio, duration) == 0) {
				ratio = ratio - 1;
			}
		}
		return Math.min(maxUsers, minUsers + incrementUsers * ratio);
	}
	
	private static int toMaxUsers(final Optional<Integer> maxUsers) {
		if (maxUsers.isPresent()) {
			return maxUsers.get();
		}
		return UNDETERMINED_MAX_VUS;
	}

	private static Optional<Integer> fromMaxUsers(final int maxUsers) {
		if (maxUsers >= 0) {
			return Optional.of(maxUsers);
		}
		return Optional.empty();
	}

	private static LoadDuration toLoadDuration(final Optional<LoadDuration> loadDuration) {
		if (loadDuration.isPresent()) {
			return loadDuration.get();
		}
		return null;
	}

	private static Optional<LoadDuration> fromLoadDuration(final LoadDuration loadDuration) {
		if (loadDuration != null) {
			return Optional.of(loadDuration);
		}
		return  Optional.empty();
	}
}
