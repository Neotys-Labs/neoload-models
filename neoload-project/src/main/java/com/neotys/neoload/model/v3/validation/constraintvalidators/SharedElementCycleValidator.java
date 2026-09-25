package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.validation.constraints.SharedElementCycleCheck;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintValidatorContext;

public final class SharedElementCycleValidator extends AbstractConstraintValidator<SharedElementCycleCheck, List<Step>> {

	@Override
	public boolean isValid(final List<Step> sharedElements, final ConstraintValidatorContext context) {
		final Map<String, List<String>> referenceGraph = new LinkedHashMap<>();
		for (final Step sharedElement : sharedElements) {
			referenceGraph.put(sharedElement.getName(), SharedElementSupport.referencedNames(sharedElement.flattened()));
		}

		final List<String> cycle = findCycle(referenceGraph);
		if (cycle.isEmpty()) {
			return true;
		}

		SharedElementSupport.fail(context, "must not contain a shared_elements reference cycle: "
				+ String.join(" -> ", cycle) + ".");
		return false;
	}

	private static List<String> findCycle(final Map<String, List<String>> graph) {
		final Set<String> visited = new HashSet<>();
		final LinkedHashSet<String> stack = new LinkedHashSet<>();
		for (final String node : graph.keySet()) {
			if (!visited.contains(node)) {
				final List<String> cycle = findCycleFrom(node, graph, visited, stack);
				if (!cycle.isEmpty()) {
					return cycle;
				}
			}
		}
		return Collections.emptyList();
	}

	private static List<String> findCycleFrom(final String node, final Map<String, List<String>> graph,
			final Set<String> visited, final LinkedHashSet<String> stack) {
		visited.add(node);
		stack.add(node);
		for (final String next : graph.getOrDefault(node, Collections.emptyList())) {
			if (stack.contains(next)) {
				final List<String> path = new ArrayList<>(stack);
				final List<String> cycle = new ArrayList<>(path.subList(path.indexOf(next), path.size()));
				cycle.add(next);
				return cycle;
			}
			if (!visited.contains(next)) {
				final List<String> cycle = findCycleFrom(next, graph, visited, stack);
				if (!cycle.isEmpty()) {
					return cycle;
				}
			}
		}
		stack.remove(node);
		return Collections.emptyList();
	}
}
