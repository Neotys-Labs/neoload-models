package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.neotys.neoload.model.v3.binding.io.IO.Format;


/**
 * Verifies the exact textual output of {@code write} (field order and formatting) for every
 * supported fixture. Unlike {@link IORoundTripTest} (which compares the resulting objects and is
 * therefore insensitive to field order), this compares the serialized text to a golden file under
 * {@code src/test/resources/write/} that pins the expected field order.
 *
 * <p>The source fixtures are not necessarily written back verbatim (defaults are materialized, some
 * values are normalized: SLA thresholds without unit, condition operators as words, ...), hence the
 * dedicated golden files rather than a comparison with the read fixture.
 */
public class IOWriteTest extends AbstractIOElementsTest {

	private static final String[] FIXTURES = {
			"test-custom-action-only-required",
			"test-custom-action-required-and-optional",
			"test-delay-with-unit-only-required",
			"test-descriptor-only-required",
			"test-descriptor-required-and-optional",
			"test-if-only-required",
			"test-if-required-and-optional",
			"test-javascript-only-required",
			"test-javascript-required-and-optional",
			"test-loop-only-required",
			"test-loop-required-and-optional",
			"test-populations-only-required",
			"test-populations-required-and-optional",
			"test-request-only-required",
			"test-request-required-and-optional",
			"test-scenarios-only-required",
			"test-scenarios-required-and-optional",
			"test-servers-only-required",
			"test-servers-required-and-optional",
			"test-slaprofiles-only-required",
			"test-slaprofiles-required-and-optional",
			"test-switch-only-required",
			"test-switch-required-and-optional",
			"test-think-time-with-unit-only-required",
			"test-transaction-only-required",
			"test-transaction-required-and-optional",
			"test-userpaths-only-required",
			"test-userpaths-required-and-optional",
			"test-variable-extractor-only-required",
			"test-variable-extractor-required-and-optional",
			"test-variable-only-required",
			"test-while-only-required",
			"test-while-required-and-optional",
			"test-assert-content-only-required",
			"test-assert-content-required-and-optional"
	};

	@Test
	public void writeProducesExpectedYaml() throws IOException {
		for (final String fixture : FIXTURES) {
			assertWriteMatchesGolden(fixture, "yaml", Format.YAML);
		}
	}

	@Test
	public void writeProducesExpectedJson() throws IOException {
		for (final String fixture : FIXTURES) {
			assertWriteMatchesGolden(fixture, "json", Format.JSON);
		}
	}

	private void assertWriteMatchesGolden(final String fixture, final String extension, final Format format) throws IOException {
		final File source = getFile(fixture, extension);
		final File golden = getFile(fixture, extension);

		final IO mapper = new IO();
		final ProjectDescriptor descriptor = mapper.read(source);
		validate(descriptor);

		final String expected = getContent(golden, StandardCharsets.UTF_8).replace("\r\n", "\n");
		final String actual = mapper.write(descriptor, format).replace("\r\n", "\n");

		assertEquals("Serialized output changed for fixture '" + fixture + "." + extension + "'", expected, actual);
	}
}
