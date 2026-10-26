package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.DurationConstant;
import com.neotys.neoload.model.v3.project.userpath.DurationRandom;
import com.neotys.neoload.model.v3.project.userpath.NameValue;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.util.Optional;
import org.junit.Test;

/**
 * Empty strings, optional fields set to {@code null}, the schema version and writing: a missing value is
 * never written as {@code null} or as an empty string, and reads back the same.
 */
public class IONullHandlingTest {

	private static final IO MAPPER = new IO();
	private static final String HEADER = "name: P\n";
	private static final String ONE_DELAY = "    steps:\n    - delay: 1s\n";

	private static String outcome(final String content) throws IOException {
		final Validation validation = new Validator().validate(MAPPER.read(content), NeoLoad.class);
		return validation.isValid() ? "valid" : validation.getMessage().get();
	}

	private static void assertViolationOn(final String content, final String path) throws IOException {
		final String outcome = outcome(content);
		assertTrue(outcome, outcome.contains("'" + path + "'"));
	}

	@Test
	public void empty_server_name_is_rejected() throws IOException {
		assertViolationOn(HEADER + "servers:\n- name: \"\"\n  host: h\n", "project.servers[0].name");
	}

	@Test
	public void empty_server_host_is_rejected() throws IOException {
		assertViolationOn(HEADER + "servers:\n- name: S\n  host: \"\"\n", "project.servers[0].host");
	}

	@Test
	public void empty_json_server_host_is_rejected() throws IOException {
		assertViolationOn("{\"name\":\"P\",\"servers\":[{\"name\":\"S\",\"host\":\"\"}]}", "project.servers[0].host");
	}

	@Test
	public void empty_user_path_name_is_rejected() throws IOException {
		assertViolationOn(HEADER + "user_paths:\n- name: \"\"\n  actions:\n" + ONE_DELAY, "project.user_paths[0].name");
	}

	@Test
	public void empty_json_user_path_name_is_rejected() throws IOException {
		assertViolationOn("{\"name\":\"P\",\"user_paths\":[{\"name\":\"\",\"actions\":{\"steps\":[{\"delay\":\"1s\"}]}}]}", "project.user_paths[0].name");
	}

	@Test
	public void empty_population_user_path_name_is_rejected() throws IOException {
		assertViolationOn(HEADER + "populations:\n- name: Pop\n  user_paths:\n  - name: \"\"\n", "project.populations[0].user_paths[0].name");
	}

	@Test
	public void empty_header_name_is_rejected() throws IOException {
		assertViolationOn(HEADER + "user_paths:\n- name: U\n  actions:\n    steps:\n    - request:\n        url: http://h:80/\n        headers:\n        - \"\": v\n",
				"project.user_paths[0].actions.steps[0].headers[0].name");
	}

	@Test
	public void null_schema_version_is_read_like_a_missing_one() throws IOException {
		final String withNull = "schemaVersion: null\nname: legacy\n";
		final String omitted = "name: legacy\n";

		assertEquals(Project.DEFAULT_SCHEMA_VERSION, MAPPER.read(withNull).getProject().getSchemaVersion());
		assertEquals(MAPPER.read(omitted).getProject().getSchemaVersion(), MAPPER.read(withNull).getProject().getSchemaVersion());
	}

	@Test
	public void optional_user_path_description_null_is_not_set() throws IOException {
		final String project = HEADER + "user_paths:\n- name: U\n  description: null\n  actions:\n" + ONE_DELAY;

		assertEquals("valid", outcome(project));
		assertEquals(Optional.empty(), MAPPER.read(project).getProject().getUserPaths().get(0).getDescription());
	}

	@Test
	public void optional_json_user_path_description_null_is_not_set() throws IOException {
		final String project = "{\"name\":\"P\",\"user_paths\":[{\"name\":\"U\",\"description\":null,\"actions\":{\"steps\":[{\"delay\":\"1s\"}]}}]}";

		assertEquals("valid", outcome(project));
		assertEquals(Optional.empty(), MAPPER.read(project).getProject().getUserPaths().get(0).getDescription());
	}

	@Test
	public void optional_step_description_null_is_not_set() throws IOException {
		final String project = HEADER + "user_paths:\n- name: U\n  actions:\n    steps:\n    - delay:\n        description: null\n        value: 1s\n";

		assertEquals("valid", outcome(project));
		final Delay delay = (Delay) MAPPER.read(project).getProject().getUserPaths().get(0).getActions().getSteps().get(0);
		assertEquals(Optional.empty(), delay.getDescription());
	}

	@Test
	public void optional_server_scheme_null_is_the_default_scheme() throws IOException {
		final String project = HEADER + "servers:\n- name: S\n  host: h\n  scheme: null\n";

		assertEquals("valid", outcome(project));
		assertEquals(Server.DEFAULT_SCHEME, MAPPER.read(project).getProject().getServers().get(0).getScheme());
	}

	@Test
	public void optional_header_value_null_is_not_set() throws IOException {
		final String project = HEADER + "user_paths:\n- name: U\n  actions:\n    steps:\n    - request:\n        url: http://h:80/\n        headers:\n        - Accept: null\n";

		assertEquals("valid", outcome(project));
		final Request request = (Request) MAPPER.read(project).getProject().getUserPaths().get(0).getActions().getSteps().get(0);
		assertEquals(Optional.empty(), request.getHeaders().get(0).getValue());
	}

	@Test
	public void written_project_has_no_null_and_no_empty_entry() throws IOException {
		final Project project = Project.builder()
				.name("P")
				.addServers(Server.builder().name("S").host("h").port("80").build())
				.addUserPaths(UserPath.builder().name("U").actions(Container.builder().name("actions")
						.pacing(DurationRandom.builder().max("2000").build())
						.addSteps(Request.builder().url("http://h:80/").addHeaders(NameValue.builder().name("Accept").build()).build())
						.addSteps(Delay.builder().isThinkTime(true).duration(DurationRandom.builder().max("2000").build()).build())
						.build()).build())
				.build();

		for (final IO.Format format : IO.Format.values()) {
			final String written = MAPPER.write(project, format);
			assertFalse(written, written.contains("null"));
			assertFalse(written, written.contains("\"\"") || written.contains("''"));
			assertEquals(written, "valid", outcome(written));
			assertEquals(written, project, MAPPER.read(written, Project.class));
		}
	}

	@Test
	public void unset_server_name_is_not_written_and_read_back_as_missing() throws IOException {
		final Project project = Project.builder().name("P").addServers(Server.builder().host("h").build()).build();

		for (final IO.Format format : IO.Format.values()) {
			final String written = MAPPER.write(project, format);
			assertFalse(written, written.contains("null"));
			assertFalse(written, written.contains("name: \"\"") || written.contains("name: ''"));
			final IOException error = assertThrows(IOException.class, () -> MAPPER.read(written));
			assertTrue(error.getMessage(), error.getMessage().contains("'name' field is mandatory for Server"));
		}
	}

	@Test
	public void unset_user_path_name_is_not_written_and_read_back_as_violation() throws IOException {
		final Project project = IOHelper.buildProject(Delay.builder().duration(DurationConstant.builder().value("1000").build()).build());
		final Project unnamed = Project.builder().from(project).userPaths(java.util.List.of(UserPath.builder().actions(project.getUserPaths().get(0).getActions()).build())).build();

		for (final IO.Format format : IO.Format.values()) {
			final String written = MAPPER.write(unnamed, format);
			assertFalse(written, written.contains("null"));
			assertTrue(written, outcome(written).contains("'project.user_paths[0].name'"));
		}
	}
}
