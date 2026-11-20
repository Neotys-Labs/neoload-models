package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * An explicit {@code null} (or an empty value) in an as-code file is read exactly like a missing value:
 * a required field is rejected, an optional one is "not set", and the text {@code "null"} is never kept.
 */
@RunWith(Parameterized.class)
public class IONullAsMissingTest {

	private static final String USER_PATH_BODY = "  actions:\n    steps:\n    - delay: 1s\n";

	private final String description;
	private final String nullProject;
	private final String omittedProject;
	private final boolean required;

	public IONullAsMissingTest(final String description, final String nullProject, final String omittedProject, final boolean required) {
		this.description = description;
		this.nullProject = nullProject;
		this.omittedProject = omittedProject;
		this.required = required;
	}

	@Parameters(name = "{0}")
	public static Collection<Object[]> cases() {
		return Arrays.asList(new Object[][] {
				yaml("server name null", "servers:\n- name: null\n  host: h\n", "servers:\n- host: h\n", true),
				yaml("server host tilde", "servers:\n- name: S\n  host: ~\n", "servers:\n- name: S\n", true),
				yaml("server port no value", "servers:\n- name: S\n  host: h\n  port:\n", "servers:\n- name: S\n  host: h\n", false),
				yaml("user_path name null", "user_paths:\n- name: null\n" + USER_PATH_BODY, "user_paths:\n-\n" + USER_PATH_BODY, true),
				yaml("population user_path name tilde", "populations:\n- name: P\n  user_paths:\n  - name: ~\n", "populations:\n- name: P\n  user_paths:\n  - {}\n", true),
				yaml("pacing random min null", pacing("{min: null, max: 2s}"), pacing("{max: 2s}"), false),
				yaml("pacing null is not set", pacing("null"), "user_paths:\n- name: U\n  actions:\n    steps:\n    - delay: 1s\n", false),
				yaml("think_time random max no value", thinkTime("{min: 1s, max: }"), thinkTime("{min: 1s}"), true),
				yaml("think_time constant null", thinkTime("null"), thinkTime("{}"), true),
				yaml("delay step name null", "user_paths:\n- name: U\n  actions:\n    steps:\n    - delay:\n        name: null\n        value: 1s\n",
						"user_paths:\n- name: U\n  actions:\n    steps:\n    - delay:\n        value: 1s\n", false),
				yaml("shared_element step name null", "user_paths:\n- name: U\n  actions:\n    steps:\n    - shared_element:\n        name: null\n",
						"user_paths:\n- name: U\n  actions:\n    steps:\n    - shared_element: {}\n", true),
				json("server name null", "{\"name\":\"P\",\"servers\":[{\"name\":null,\"host\":\"h\"}]}", "{\"name\":\"P\",\"servers\":[{\"host\":\"h\"}]}", true),
				json("user_path name null", "{\"name\":\"P\",\"user_paths\":[{\"name\":null,\"actions\":{\"steps\":[{\"delay\":\"1s\"}]}}]}",
						"{\"name\":\"P\",\"user_paths\":[{\"actions\":{\"steps\":[{\"delay\":\"1s\"}]}}]}", true),
				json("pacing random min null", "{\"name\":\"P\",\"user_paths\":[{\"name\":\"U\",\"actions\":{\"pacing\":{\"min\":null,\"max\":\"2s\"},\"steps\":[{\"delay\":\"1s\"}]}}]}",
						"{\"name\":\"P\",\"user_paths\":[{\"name\":\"U\",\"actions\":{\"pacing\":{\"max\":\"2s\"},\"steps\":[{\"delay\":\"1s\"}]}}]}", false),
				json("think_time random max null", "{\"name\":\"P\",\"user_paths\":[{\"name\":\"U\",\"actions\":{\"steps\":[{\"think_time\":{\"min\":\"1s\",\"max\":null}}]}}]}",
						"{\"name\":\"P\",\"user_paths\":[{\"name\":\"U\",\"actions\":{\"steps\":[{\"think_time\":{\"min\":\"1s\"}}]}}]}", true),
				yaml("if match null", ifStep("match: null"), ifStep(""), false),
				yaml("sla threshold null", sla("\n  - null"), sla("[]"), true),
				yaml("delay step duration null", "user_paths:\n- name: U\n  actions:\n    steps:\n    - delay: null\n",
						"user_paths:\n- name: U\n  actions:\n    steps:\n    - delay: {}\n", true),
				json("sla threshold null", "{\"name\":\"P\",\"sla_profiles\":[{\"name\":\"S\",\"thresholds\":[null]}]}",
						"{\"name\":\"P\",\"sla_profiles\":[{\"name\":\"S\",\"thresholds\":[]}]}", true),
		});
	}

	private static Object[] yaml(final String description, final String nullBody, final String omittedBody, final boolean required) {
		return new Object[] { "YAML " + description, "name: P\n" + nullBody, "name: P\n" + omittedBody, required };
	}

	private static Object[] json(final String description, final String nullProject, final String omittedProject, final boolean required) {
		return new Object[] { "JSON " + description, nullProject, omittedProject, required };
	}

	private static String pacing(final String value) {
		return "user_paths:\n- name: U\n  actions:\n    pacing: " + value + "\n    steps:\n    - delay: 1s\n";
	}

	private static String thinkTime(final String value) {
		return "user_paths:\n- name: U\n  actions:\n    steps:\n    - think_time: " + value + "\n";
	}

	private static String ifStep(final String conditions) {
		return "user_paths:\n- name: U\n  actions:\n    steps:\n    - if:\n        conditions:\n        - \"'a' == 'a'\"\n        " + conditions + "\n        then:\n          steps:\n          - delay: 1s\n";
	}

	private static String sla(final String thresholds) {
		return "sla_profiles:\n- name: S\n  thresholds: " + thresholds + "\n";
	}

	@Test
	public void null_is_read_like_omitted() {
		final String fromNull = outcome(nullProject);
		final String fromOmitted = outcome(omittedProject);

		assertEquals(description, fromOmitted, fromNull);
		assertFalse(description + ": loaded text must not be \"null\": " + fromNull, fromNull.contains("'null'") || fromNull.contains("=null"));
		assertEquals(description, required, !fromNull.equals("valid"));
	}

	@Test
	public void null_port_reads_as_default_port() throws IOException {
		if (!description.endsWith("server port no value")) {
			return;
		}
		assertEquals("80", new IO().read(nullProject).getProject().getServers().get(0).getPort());
	}

	private static String outcome(final String content) {
		try {
			final Validation validation = new Validator().validate(new IO().read(content), NeoLoad.class);
			return validation.isValid() ? "valid" : "invalid: " + validation.getMessage().get();
		} catch (final JsonProcessingException e) {
			return "error: " + e.getOriginalMessage();
		} catch (final IOException e) {
			return "error: " + e.getMessage();
		}
	}

	@Test
	public void outcome_of_missing_required_field_is_a_violation() {
		assertTrue(description, !required || !outcome(omittedProject).equals("valid"));
	}
}
