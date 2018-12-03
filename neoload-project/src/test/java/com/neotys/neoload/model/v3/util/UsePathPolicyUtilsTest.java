package com.neotys.neoload.model.v3.util;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.population.UserPathPolicy;


public class UsePathPolicyUtilsTest {
	@Test
	public void computeDistributionsWithUserPathPolicy() {
		List<UserPathPolicy> userPathPolicies = Arrays.asList(
				UserPathPolicy.builder()
					.name("UserPath")
					.distribution(100.0)
					.build()
		);
		List<UserPathPolicy> newUserPathPolicies = UserPathPolicyUtils.computeDistributions(userPathPolicies);
		assertEquals(userPathPolicies, newUserPathPolicies);
		
		userPathPolicies = Arrays.asList(
				UserPathPolicy.builder()
					.name("UserPath1")
					.build()
		);
		newUserPathPolicies = UserPathPolicyUtils.computeDistributions(userPathPolicies);
		assertEquals(1, newUserPathPolicies.size());
		assertEquals(Optional.ofNullable(new Double(100.0)), newUserPathPolicies.get(0).getDistribution());
		userPathPolicies = Arrays.asList(
				UserPathPolicy.builder()
					.name("UserPath1")
					.build(),
				UserPathPolicy.builder()
					.name("UserPath2")
					.build()
		);
		newUserPathPolicies = UserPathPolicyUtils.computeDistributions(userPathPolicies);
		assertEquals(2, newUserPathPolicies.size());
		assertEquals(Optional.ofNullable(new Double(50.0)), newUserPathPolicies.get(0).getDistribution());
		assertEquals(Optional.ofNullable(new Double(50.0)), newUserPathPolicies.get(1).getDistribution());
		userPathPolicies = Arrays.asList(
				UserPathPolicy.builder()
					.name("UserPath1")
					.build(),
				UserPathPolicy.builder()
					.name("UserPath2")
					.build(),
				UserPathPolicy.builder()
					.name("UserPath3")
					.build()
		);
		newUserPathPolicies = UserPathPolicyUtils.computeDistributions(userPathPolicies);
		assertEquals(3, newUserPathPolicies.size());
		assertEquals(Optional.ofNullable(new Double(33.3)), newUserPathPolicies.get(0).getDistribution());
		assertEquals(Optional.ofNullable(new Double(33.3)), newUserPathPolicies.get(1).getDistribution());
		assertEquals(Optional.ofNullable(new Double(33.4)), newUserPathPolicies.get(2).getDistribution());
		
		userPathPolicies = Arrays.asList(
				UserPathPolicy.builder()
					.name("UserPath1")
					.distribution(25.0)
					.build(),
				UserPathPolicy.builder()
					.name("UserPath2")
					.build(),
				UserPathPolicy.builder()
					.name("UserPath3")
					.distribution(25.0)
					.build()
		);
		newUserPathPolicies = UserPathPolicyUtils.computeDistributions(userPathPolicies);
		assertEquals(3, newUserPathPolicies.size());
		assertEquals(Optional.ofNullable(new Double(25.0)), newUserPathPolicies.get(0).getDistribution());
		assertEquals(Optional.ofNullable(new Double(50.0)), newUserPathPolicies.get(1).getDistribution());
		assertEquals(Optional.ofNullable(new Double(25.0)), newUserPathPolicies.get(2).getDistribution());
		userPathPolicies = Arrays.asList(
				UserPathPolicy.builder()
					.name("UserPath1")
					.build(),
				UserPathPolicy.builder()
					.name("UserPath2")
					.distribution(50.0)
					.build(),
				UserPathPolicy.builder()
					.name("UserPath3")
					.build()
		);
		newUserPathPolicies = UserPathPolicyUtils.computeDistributions(userPathPolicies);
		assertEquals(3, newUserPathPolicies.size());
		assertEquals(Optional.ofNullable(new Double(25.0)), newUserPathPolicies.get(0).getDistribution());
		assertEquals(Optional.ofNullable(new Double(50.0)), newUserPathPolicies.get(1).getDistribution());
		assertEquals(Optional.ofNullable(new Double(25.0)), newUserPathPolicies.get(2).getDistribution());
		userPathPolicies = Arrays.asList(
				UserPathPolicy.builder()
					.name("UserPath1")
					.build(),
				UserPathPolicy.builder()
					.name("UserPath2")
					.build(),
				UserPathPolicy.builder()
					.name("UserPath3")
					.build(),
				UserPathPolicy.builder()
					.name("UserPath4")
					.distribution(50.0)
					.build()
		);
		newUserPathPolicies = UserPathPolicyUtils.computeDistributions(userPathPolicies);
		assertEquals(4, newUserPathPolicies.size());
		assertEquals(Optional.ofNullable(new Double(16.7)), newUserPathPolicies.get(0).getDistribution());
		assertEquals(Optional.ofNullable(new Double(16.7)), newUserPathPolicies.get(1).getDistribution());
		assertEquals(Optional.ofNullable(new Double(16.6)), newUserPathPolicies.get(2).getDistribution());
		assertEquals(Optional.ofNullable(new Double(50.0)), newUserPathPolicies.get(3).getDistribution());
			
		userPathPolicies = Arrays.asList(
				UserPathPolicy.builder()
					.name("UserPath1")
					.distribution(100.0)
					.build(),
				UserPathPolicy.builder()
					.name("UserPath2")
					.build()
		);
		newUserPathPolicies = UserPathPolicyUtils.computeDistributions(userPathPolicies);
		assertEquals(2, newUserPathPolicies.size());
		assertEquals(Optional.ofNullable(new Double(100.0)), newUserPathPolicies.get(0).getDistribution());
		assertEquals(Optional.ofNullable(new Double(0.0)), newUserPathPolicies.get(1).getDistribution());
		userPathPolicies = Arrays.asList(
				UserPathPolicy.builder()
					.name("UserPath1")
					.distribution(100.0)
					.build(),
				UserPathPolicy.builder()
					.name("UserPath2")
					.distribution(100.0)
					.build(),
				UserPathPolicy.builder()
					.name("UserPath3")
					.build()
		);
		newUserPathPolicies = UserPathPolicyUtils.computeDistributions(userPathPolicies);
		assertEquals(3, newUserPathPolicies.size());
		assertEquals(Optional.ofNullable(new Double(100.0)), newUserPathPolicies.get(0).getDistribution());
		assertEquals(Optional.ofNullable(new Double(100.0)), newUserPathPolicies.get(1).getDistribution());
		assertEquals(Optional.ofNullable(new Double(0.0)), newUserPathPolicies.get(2).getDistribution());
		userPathPolicies = Arrays.asList(
				UserPathPolicy.builder()
					.name("UserPath1")
					.distribution(100.0)
					.build(),
				UserPathPolicy.builder()
					.name("UserPath2")
					.distribution(100.0)
					.build(),
				UserPathPolicy.builder()
					.name("UserPath3")
					.build(),
				UserPathPolicy.builder()
					.name("UserPath4")
					.distribution(100.0)
					.build()
		);
		newUserPathPolicies = UserPathPolicyUtils.computeDistributions(userPathPolicies);
		assertEquals(4, newUserPathPolicies.size());
		assertEquals(Optional.ofNullable(new Double(100.0)), newUserPathPolicies.get(0).getDistribution());
		assertEquals(Optional.ofNullable(new Double(100.0)), newUserPathPolicies.get(1).getDistribution());
		assertEquals(Optional.ofNullable(new Double(0.0)), newUserPathPolicies.get(2).getDistribution());
		assertEquals(Optional.ofNullable(new Double(100.0)), newUserPathPolicies.get(3).getDistribution());
	}

	@Test
	public void computeDistributionsWithDouble() {
		double[] computedDistributions = UserPathPolicyUtils.computeDistributions(-1, 0.0);
		assertNotNull(computedDistributions);
		assertEquals(0, computedDistributions.length);
		
		computedDistributions = UserPathPolicyUtils.computeDistributions(0, 0.0);
		assertNotNull(computedDistributions);
		assertEquals(0, computedDistributions.length);
		computedDistributions = UserPathPolicyUtils.computeDistributions(0, 100.0);
		assertNotNull(computedDistributions);
		assertEquals(0, computedDistributions.length);
		
		computedDistributions = UserPathPolicyUtils.computeDistributions(1, 0.0);
		assertNotNull(computedDistributions);
		assertEquals(1, computedDistributions.length);
		assertEquals(100.0, computedDistributions[0], 0.0);
		computedDistributions = UserPathPolicyUtils.computeDistributions(2, 0.0);
		assertNotNull(computedDistributions);
		assertEquals(2, computedDistributions.length);
		assertEquals(50.0, computedDistributions[0], 0.0);
		assertEquals(50.0, computedDistributions[1], 0.0);
		computedDistributions = UserPathPolicyUtils.computeDistributions(3, 0.0);
		assertNotNull(computedDistributions);
		assertEquals(3, computedDistributions.length);
		assertEquals(33.3, computedDistributions[0], 0.0);
		assertEquals(33.3, computedDistributions[1], 0.0);
		assertEquals(33.4, computedDistributions[2], 0.0);
		
		computedDistributions = UserPathPolicyUtils.computeDistributions(1, 50.0);
		assertNotNull(computedDistributions);
		assertEquals(1, computedDistributions.length);
		assertEquals(50.0, computedDistributions[0], 0.0);
		computedDistributions = UserPathPolicyUtils.computeDistributions(2, 50.0);
		assertNotNull(computedDistributions);
		assertEquals(2, computedDistributions.length);
		assertEquals(25.0, computedDistributions[0], 0.0);
		assertEquals(25.0, computedDistributions[1], 0.0);
		computedDistributions = UserPathPolicyUtils.computeDistributions(3, 50.0);
		assertNotNull(computedDistributions);
		assertEquals(3, computedDistributions.length);
		assertEquals(16.7, computedDistributions[0], 0.0);
		assertEquals(16.7, computedDistributions[1], 0.0);
		assertEquals(16.6, computedDistributions[2], 0.0);

		computedDistributions = UserPathPolicyUtils.computeDistributions(1, 100.0);
		assertNotNull(computedDistributions);
		assertEquals(1, computedDistributions.length);
		assertEquals(0.0, computedDistributions[0], 0.0);
		computedDistributions = UserPathPolicyUtils.computeDistributions(2, 200.0);
		assertNotNull(computedDistributions);
		assertEquals(2, computedDistributions.length);
		assertEquals(0.0, computedDistributions[0], 0.0);
		assertEquals(0.0, computedDistributions[1], 0.0);
		computedDistributions = UserPathPolicyUtils.computeDistributions(3, 300.0);
		assertNotNull(computedDistributions);
		assertEquals(3, computedDistributions.length);
		assertEquals(0.0, computedDistributions[0], 0.0);
		assertEquals(0.0, computedDistributions[1], 0.0);
		assertEquals(0.0, computedDistributions[2], 0.0);		
	}

	
	@Test
	public void round() {
		assertEquals(10.0, UserPathPolicyUtils.round(10.0, 1), 0.0);
		assertEquals(10.2, UserPathPolicyUtils.round(10.2, 1), 0.0);
		assertEquals(10.2, UserPathPolicyUtils.round(10.24, 1), 0.0);
		assertEquals(10.3, UserPathPolicyUtils.round(10.25, 1), 0.0);
		assertEquals(10.3, UserPathPolicyUtils.round(10.26, 1), 0.0);
	}
}
