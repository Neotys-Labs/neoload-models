package com.neotys.neoload.model.writers;

import java.util.List;

import com.neotys.neoload.model.population.UserPathPolicy;

public class UserPathPolicyUtils {

	private UserPathPolicyUtils() {
		super();
	}
	
	public static List<UserPathPolicy> computeDistribution(final List<UserPathPolicy> userPaths) {
//		if ((userPaths == null) || (userPaths.isEmpty())) return userPaths;
//		
//		int countOfDistributions = userPaths.size();
//		int countOfDistributionsNotDefined = 0;
//		double totalOfDistributions = 0;
//		
//		userPaths.forEach(userPath -> {
//			final Double distribution = userPath.getDistribution();
//			if (distribution != null) {
//				totalOfDistributions = totalOfDistributions + distribution.doubleValue();
//			}
//			else {
//				countOfDistributionsNotDefined = countOfDistributionsNotDefined + 1;
//			}
//		});
//		
//		if (countOfDistributionsNotDefined == 0) {
//			return userPaths;
//		}
//		if (totalOfDistributions >= 100) {
//			
//		}
		
		
		
		
		//UserPathPolicyWriter.of(userPaths).writeXML(document, xmlPopulation));
		
		return null;
	}

}
