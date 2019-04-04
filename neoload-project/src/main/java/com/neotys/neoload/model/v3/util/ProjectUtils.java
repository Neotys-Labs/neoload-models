package com.neotys.neoload.model.v3.util;

import java.util.List;
import java.util.Set;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import com.neotys.neoload.model.v3.project.Project;

public class ProjectUtils {

	private ProjectUtils() {
		throw new IllegalStateException("Utility class");
	}

	@VisibleForTesting
	public static String checkUniqueName(final List<Project> projects) {
		final Set<String> asCodeProjectNames = Sets.newHashSet();
		projects.forEach(project -> project.getName().ifPresent(name -> asCodeProjectNames.add(name)));

		if (asCodeProjectNames.size() > 1) {
			throw new IllegalArgumentException("Several project names have been defined, only one is allowed");
		}

		if (asCodeProjectNames.size() == 1) {
			return asCodeProjectNames.iterator().next();
		}

		throw new IllegalArgumentException("Project name must be defined in one as code file");
	}
}
