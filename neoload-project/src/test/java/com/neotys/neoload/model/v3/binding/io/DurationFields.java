package com.neotys.neoload.model.v3.binding.io;

import java.util.function.Function;

/**
 * The v3 as-code YAML fields holding a duration, each with the smallest project that carries a given raw
 * value in that field.
 */
enum DurationFields {
	CONSTANT_RAMPUP("rampup", true, value -> scenario("  populations:\n  - name: C\n    constant_load:\n      users: 1\n      rampup: " + value + "\n")),
	PEAKS_STEP_RAMPUP("step_rampup", true, value -> scenario("  populations:\n  - name: K\n    peaks_load:\n      minimum:\n        users: 1\n        duration: 1 iteration\n"
			+ "      maximum:\n        users: 5\n        duration: 1 iteration\n      start: maximum\n      duration: 15 iterations\n      step_rampup: " + value + "\n")),
	RAMPUP_INCREMENT_RAMPUP("increment_rampup", true, value -> scenario("  populations:\n  - name: R\n    rampup_load:\n      min_users: 1\n      max_users: 5\n      increment_users: 1\n"
			+ "      increment_every: 1 iteration\n      duration: 15 iterations\n      increment_rampup: " + value + "\n")),
	MONITORING_BEFORE("before", true, value -> scenario("  monitoring:\n    before: " + value + "\n")),
	MONITORING_AFTER("after", true, value -> scenario("  monitoring:\n    after: " + value + "\n")),
	RENDEZVOUS_TIMEOUT("timeout", true, value -> scenario("  rendezvous_policies:\n  - name: r\n    timeout: " + value + "\n")),
	DELAY("delay", false, value -> actions("    - delay: " + value + "\n")),
	THINK_TIME("think_time", false, value -> actions("    - think_time: " + value + "\n")),
	PACING("pacing", false, value -> actions("    - transaction:\n        name: T\n        pacing: " + value + "\n        steps:\n        - delay: 1s\n")),
	WEB_PAGE_THINK_TIME("think_time", false, value -> actions("    - web_page:\n        think_time: " + value + "\n        steps:\n        - request:\n            url: http://host:80/\n")),
	WAIT_UNTIL_TIMEOUT("timeout", false, value -> actions(waitUntil("        timeout: " + value + "\n")));

	final String key;
	final boolean seconds;
	private final Function<String, String> yaml;

	DurationFields(final String key, final boolean seconds, final Function<String, String> yaml) {
		this.key = key;
		this.seconds = seconds;
		this.yaml = yaml;
	}

	String yaml(final String rawValue) {
		return yaml.apply(rawValue);
	}

	static String scenario(final String body) {
		final String populations = body.contains("populations:") ? "" : "  populations:\n  - name: C\n    constant_load:\n      users: 1\n";
		return "name: P\nscenarios:\n- name: S\n" + body + populations;
	}

	static String actions(final String steps) {
		return "name: P\nuser_paths:\n- name: U\n  actions:\n    steps:\n" + steps;
	}

	static String waitUntil(final String timeLine) {
		return "    - wait_until:\n        conditions:\n        - \"'${a}' == '1'\"\n" + timeLine;
	}
}
