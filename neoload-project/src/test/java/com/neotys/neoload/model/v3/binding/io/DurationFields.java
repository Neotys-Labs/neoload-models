package com.neotys.neoload.model.v3.binding.io;

import java.util.function.Function;

/**
 * The v3 as-code YAML fields holding a duration, each with the smallest project that carries a given raw
 * value in that field.
 */
enum DurationFields {
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

	static String actions(final String steps) {
		return "name: P\nuser_paths:\n- name: U\n  actions:\n    steps:\n" + steps;
	}

	static String waitUntil(final String timeLine) {
		return "    - wait_until:\n        conditions:\n        - \"'${a}' == '1'\"\n" + timeLine;
	}
}
