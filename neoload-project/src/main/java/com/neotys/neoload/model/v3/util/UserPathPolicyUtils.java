package com.neotys.neoload.model.v3.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.neotys.neoload.model.v3.project.population.UserPathPolicy;

public class UserPathPolicyUtils {
	private static final int PRECISION = 1;
	private UserPathPolicyUtils() {
		super();
	}

	public static List<UserPathPolicy> computeDistributions(final List<UserPathPolicy> userPathPolicies) {
		if ((userPathPolicies == null) || (userPathPolicies.isEmpty())) return userPathPolicies;
		
		final int countOfDistributions = userPathPolicies.size();
		int countOfDistributionsNotDefined = 0;
		double totalOfDistributions = 0;
		
		// Compute the total of distributions 
		// Check if a distribution is null
		for (final UserPathPolicy userPathPolicy : userPathPolicies) {
			final Double distribution = userPathPolicy.getDistribution();
			if (distribution != null) {
				totalOfDistributions = totalOfDistributions + distribution;
			}
			else {
				countOfDistributionsNotDefined = countOfDistributionsNotDefined + 1;
			}
		}
		// If all distributions are defined, return the list of user path policies	
		if (countOfDistributionsNotDefined == 0) {
			return userPathPolicies;
		}
		
		// Compute each distribution that is not defined
		final double[] computedDistributions = computeDistributions(countOfDistributionsNotDefined, totalOfDistributions);
		
		int indexOfComputedDistributions = 0;
		final List<UserPathPolicy> newUserPathPolicies = new ArrayList<>(countOfDistributions);
		for (final UserPathPolicy userPathPolicy : userPathPolicies) {
			final Double distribution = userPathPolicy.getDistribution();
			if (distribution != null) {
				newUserPathPolicies.add(userPathPolicy);
			}
			else {
				final UserPathPolicy newUserPathPolicy = UserPathPolicy.builder()
						.from(userPathPolicy)
						.distribution(computedDistributions[indexOfComputedDistributions])
						.build();
				newUserPathPolicies.add(newUserPathPolicy);
				
				indexOfComputedDistributions = indexOfComputedDistributions + 1;
			}
		}
		return newUserPathPolicies;
	}
		
	protected static final double[] computeDistributions(final int countOfDistributionsNotDefined, final double totalOfDistributions) {
		if (countOfDistributionsNotDefined <= 0) return new double[0];
		
		final double[] computedDistributions = new double[countOfDistributionsNotDefined];
		if (totalOfDistributions >= 100) {
			Arrays.fill(computedDistributions, 0.0);
		}
		else {
			double computedTotalOfDistributions = 0;
			
			final double remainingOfDistributions = 100.0 - totalOfDistributions;			
			final double division = remainingOfDistributions / (double) countOfDistributionsNotDefined;
			
			for (int i = 0; i < countOfDistributionsNotDefined; i++) {
				double computedDistribution = 0;
				if (i != (countOfDistributionsNotDefined - 1)) {
					computedDistribution = round(division, PRECISION);
					computedTotalOfDistributions = computedTotalOfDistributions + computedDistribution;
				}
				else {
					computedDistribution = round(remainingOfDistributions - computedTotalOfDistributions, PRECISION);
				}
				computedDistributions[i] = computedDistribution;
			}
		}
		return computedDistributions;
	}
	
	protected static double round(final double value, final int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
}
