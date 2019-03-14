package com.neotys.neoload.model.v3.util;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import com.neotys.neoload.model.v3.project.Project;

import java.util.List;
import java.util.Set;

import static com.neotys.neoload.model.v3.project.Project.DEFAULT_NAME;

public class ProjectUtils {

	@VisibleForTesting
	public static String checkUniqueName(final List<Project> projects) {
		final Set<String> asCodeProjectNames = Sets.newHashSet();
		projects.forEach(project -> {
			if (!DEFAULT_NAME.equals(project.getName())) {
				asCodeProjectNames.add(project.getName());
			}
		});

		if (asCodeProjectNames.size() > 1) {
			throw new IllegalArgumentException("Several project names have been defined, only one is allowed");
		}

		if (asCodeProjectNames.size() == 1) {
			return asCodeProjectNames.iterator().next();
		}

		throw new IllegalArgumentException("Project name must be defined in one as code file");
	}
}
