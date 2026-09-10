package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static org.junit.Assert.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Part;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.Request.Method;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Test;

public class IORequestPartsTest extends AbstractIOElementsTest {

	@Test
	public void readRequestPartsOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getRequestPartsOnlyRequired());
		assertNotNull(expectedProject);

		read("test-request-parts-only-required", expectedProject);
	}

	@Test
	public void writeRequestPartsOnlyRequired() throws IOException {
		final Project expectedProject = buildProject(getRequestPartsOnlyRequired());
		assertNotNull(expectedProject);

		write("test-request-parts-only-required", expectedProject);
	}

	@Test
	public void readRequestPartsRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getRequestPartsRequiredAndOptional());
		assertNotNull(expectedProject);

		read("test-request-parts-required-and-optional", expectedProject);
	}

	@Test
	public void writeRequestPartsRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProject(getRequestPartsRequiredAndOptional());
		assertNotNull(expectedProject);

		write("test-request-parts-required-and-optional", expectedProject);
	}

	private static Request getRequestPartsOnlyRequired() {
		return Request.builder()
				.url("https://example.com/upload")
				.method(Method.POST.name())
				.parts(Arrays.asList(Part.builder()
						.name("comment")
						.value("hello")
						.build()))
				.build();
	}

	private static Request getRequestPartsRequiredAndOptional() {
		final Part textPart = Part.builder()
				.name("comment")
				.contentType("text/plain")
				.charSet("UTF-8")
				.transferEncoding("8bit")
				.value("hello")
				.build();
		final Part filePart = Part.builder()
				.name("file")
				.contentType("image/jpeg")
				.filename("upload.jpg")
				.sourceFilename("upload.jpg")
				.build();
		return Request.builder()
				.url("https://example.com/upload")
				.method(Method.POST.name())
				.parts(Arrays.asList(textPart, filePart))
				.build();
	}
}
