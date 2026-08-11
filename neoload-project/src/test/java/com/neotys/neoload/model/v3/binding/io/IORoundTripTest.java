package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertEquals;

import com.neotys.neoload.model.v3.binding.io.IO.Format;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * Generic serialization round-trip test: for every supported fixture, reads the file, serializes
 * the resulting model back and re-reads it, then asserts the model is preserved. The comparison is
 * done on the objects (via {@code toString()}) rather than on the raw text, so it is not sensitive
 * to non-canonical formatting in the fixtures and focuses on the correctness of the serialization
 * side.
 *
 * <p>Fixtures whose serialization is not implemented yet are intentionally excluded, see
 * {@code RAPPORT-serialisation-v3-project.md} §4.1 (cases marked 🔴):
 * <ul>
 *   <li>{@code test-if-*}, {@code test-while-*}: {@code Condition} is read from a compact string
 *       (ANTLR) with no inverse serializer, and the {@code match} field serializes in upper-case.</li>
 *   <li>{@code test-slaprofiles-*}: {@code SlaThreshold} is read from a compact string with no
 *       inverse serializer.</li>
 *   <li>{@code test-variable-only-required}: {@code Variable} uses
 *       {@code @JsonTypeInfo(use = Id.NAME, WRAPPER_OBJECT)} but the runtime type is the generated
 *       {@code Immutable*} class, so the wrapper key is written as the class name instead of the
 *       logical name ({@code constant}, {@code file}, ...).</li>
 *   <li>{@code test-loop-*}, {@code test-switch-*}: contain nested types affected by the above.</li>
 *   <li>{@code test-*-required-and-optional} for request (headers) and server (authentication
 *       wrapper).</li>
 * </ul>
 */
public class IORoundTripTest extends AbstractIOElementsTest {

	private static final String[] ROUND_TRIP_FIXTURES = {
			"test-custom-action-only-required",
			"test-custom-action-required-and-optional",
			"test-debug-logger-only-required",
			"test-debug-logger-required-and-optional",
			"test-delay-without-unit-only-required",
			"test-delay-with-unit-only-required",
			"test-descriptor-only-required",
			"test-descriptor-required-and-optional",
			"test-go-to-next-iteration",
			"test-if-only-required",
			"test-if-required-and-optional",
			"test-javascript-only-required",
			"test-javascript-required-and-optional",
			"test-loop-only-required",
			"test-loop-required-and-optional",
			"test-populations-only-required",
			"test-populations-required-and-optional",
			"test-readonly-slaprofiles-only-required",
			"test-readonly-slaprofiles-required-and-optional",
			"test-readonly-while-only-required",
			"test-readonly-while-required-and-optional",
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
			"test-think-time-without-unit-only-required",
			"test-think-time-with-unit-only-required",
			"test-transaction-only-required",
			"test-transaction-required-and-optional",
			"test-try-catch-only-required",
			"test-try-catch-required-and-optional",
			"test-userpaths-only-required",
			"test-userpaths-required-and-optional",
			"test-variable-extractor-only-required",
			"test-variable-extractor-required-and-optional",
			"test-variable-only-required",
			"test-while-only-required",
			"test-while-required-and-optional",
			"test-assert-content-only-required",
			"test-assert-content-required-and-optional",
			"test-fork-only-required",
			"test-fork-required-and-optional",
			"test-variable-modifier-only-required",
			"test-variable-modifier-required-and-optional",
			"test-rendezvous-only-required",
			"test-rendezvous-required-and-optional",
			"test-shared-queue-only-required",
			"test-shared-queue-required-and-optional",
			"test-stop-vu-only-required",
			"test-stop-vu-required-and-optional"
	};

	@Test
	public void roundTripPreservesModelInYaml() throws IOException {
		for (final String fixture : ROUND_TRIP_FIXTURES) {
			assertRoundTrip(fixture, "yaml", Format.YAML);
		}
	}

	@Test
	public void roundTripPreservesModelInJson() throws IOException {
		for (final String fixture : ROUND_TRIP_FIXTURES) {
			assertRoundTrip(fixture, "json", Format.JSON);
		}
	}

	private void assertRoundTrip(final String fixture, final String extension, final Format format) throws IOException {
		final File file = getFile(fixture, extension);

		final IO mapper = new IO();
		final ProjectDescriptor fromFile = mapper.read(file);
		validate(fromFile);

		final String serialized = mapper.write(fromFile, format);
		final ProjectDescriptor reparsed = mapper.read(serialized);
		validate(reparsed);

		assertEquals("Round-trip changed the model for fixture '" + fixture + "." + extension + "'",
				fromFile.toString(), reparsed.toString());
	}
}
