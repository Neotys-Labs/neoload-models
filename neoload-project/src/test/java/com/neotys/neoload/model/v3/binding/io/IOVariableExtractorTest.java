package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static org.junit.Assert.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import java.io.IOException;
import java.util.Optional;
import org.junit.Test;

public class IOVariableExtractorTest extends AbstractIOElementsTest {

	@Test
	public void readVariableExtractorOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getRequestWithVariableExtractorsOnlyRequired());
		assertNotNull(expectedProject);

		read("test-variable-extractor-only-required", expectedProject);
	}

	@Test
	public void readVariableExtractorRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getRequestWithVariableExtractorRequiredAndOptional());
		assertNotNull(expectedProject);

		read("test-variable-extractor-required-and-optional", expectedProject);
	}

	@Test
	public void writeVariableExtractorOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getRequestWithVariableExtractorsOnlyRequired());
		assertNotNull(expectedProject);

		write("test-variable-extractor-only-required", expectedProject);
	}

	@Test
	public void writeVariableExtractorRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getRequestWithVariableExtractorRequiredAndOptional());
		assertNotNull(expectedProject);

		write("test-variable-extractor-required-and-optional", expectedProject);
	}

	private Request getRequestWithVariableExtractorsOnlyRequired() {
		return Request.builder()
				.name("http_request")
				.url("http://www.neotys.com/select?name:neoload")
				.addExtractors(
						VariableExtractor.builder()
								.name("first-match")
								.build(),
						VariableExtractor.builder()
								.name("first-match-on-jsonpath")
								.jsonPath("$.store.book[?(@.price<10)].title")
								.build(),
						VariableExtractor.builder()
								.name("first-match-on-xpath")
								.xpath("//bookstore/book[price<10]/title")
								.build()
				)
				.build();
	}

	private Request getRequestWithVariableExtractorRequiredAndOptional() {
		final VariableExtractor variableExtractor = VariableExtractor.builder()
				.name("MyExtractedVariable")
				.from(VariableExtractor.From.BOTH)
				.regexp("Fea(.*)")
				.matchNumber(-1)
				.template("$0$")
				.decode(Optional.of(VariableExtractor.Decode.URL))
				.extractOnce(true)
				.getDefault("<NOT EXTRACTED>")
				.throwAssertionError(false)
				.build();

		return Request.builder()
				.name("http_request")
				.url("http://www.neotys.com/select?name:neoload")
				.addExtractors(variableExtractor)
				.build();
	}
}
