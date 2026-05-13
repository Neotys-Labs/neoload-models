package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.framework.DynamicParameter;
import com.neotys.neoload.model.v3.project.framework.ExtractionSource;
import com.neotys.neoload.model.v3.project.framework.Framework;


public class IOFrameworkTest extends AbstractIOElementsTest {

	private static Project getFrameworkOnlyRequired() {
		final Framework framework = Framework.builder()
				.name("MyFramework")
				.addParameters(DynamicParameter.builder()
						.name("MyParam")
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addFrameworks(framework)
				.build();
	}

	private static Project getFrameworkRequiredAndOptional() {
		final Framework framework = Framework.builder()
				.name("MyFramework")
				.description("My Framework")
				.isEnabled(false)
				.addParameters(DynamicParameter.builder()
						.name("csrf_token")
						.description("CSRF token")
						.isEnabled(false)
						.extractionSource(ExtractionSource.HEADERS)
						.xpath("//x")
						.jsonPath("$.x")
						.regexp("name=\"csrf\" value=\"(.+?)\"")
						.matchNumber(2)
						.template("$1$$2$")
						.build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addFrameworks(framework)
				.build();
	}

	@Test
	public void readFrameworkOnlyRequired() throws IOException {
		final Project expectedProject = getFrameworkOnlyRequired();
		assertNotNull(expectedProject);

		read("test-framework-only-required", expectedProject);
	}

	@Test
	public void readFrameworkRequiredAndOptional() throws IOException {
		final Project expectedProject = getFrameworkRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-framework-required-and-optional", expectedProject);
	}

	@Test
	public void writeFrameworkOnlyRequired() throws IOException {
		final Project expectedProject = getFrameworkOnlyRequired();
		assertNotNull(expectedProject);

		write("test-framework-only-required", expectedProject);
	}

	@Test
	public void writeFrameworkRequiredAndOptional() throws IOException {
		final Project expectedProject = getFrameworkRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-framework-required-and-optional", expectedProject);
	}
}