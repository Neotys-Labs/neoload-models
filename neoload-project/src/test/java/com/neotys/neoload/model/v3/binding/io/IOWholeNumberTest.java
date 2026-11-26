package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.neotys.neoload.model.v3.project.variable.CounterVariable;
import com.neotys.neoload.model.v3.project.variable.RandomNumberVariable;
import java.io.IOException;
import org.junit.Test;

/**
 * The whole-number fields of the as-code YAML are read as 32-bit integers: a decimal or a number beyond the 32-bit
 * range fails the read with an error naming the field and the value.
 */
public class IOWholeNumberTest {
	private static final String NOT_A_WHOLE_NUMBER = "must be a whole number.";
	private static final String NOT_A_32_BIT_WHOLE_NUMBER = "must be a whole number from -2147483648 to 2147483647.";

	private static String variable(final String type, final String properties) {
		return "name: P\nvariables:\n- " + type + ":\n    name: V\n    " + properties;
	}

	private static <T> T readVariable(final String type, final String properties, final Class<T> variableType) throws IOException {
		return variableType.cast(new IO().read(variable(type, properties)).getProject().getVariables().get(0));
	}

	private static String readError(final String content) {
		return assertThrows(IOException.class, () -> new IO().read(content)).getMessage();
	}

	@Test
	public void counterBoundsAreReadAsNegativeIntegers() throws IOException {
		final CounterVariable counter = readVariable("counter", "start: -10\n    end: -5\n    increment: 1\n", CounterVariable.class);

		assertEquals(-10, counter.getStart());
		assertEquals(-5, counter.getEnd());
	}

	@Test
	public void randomNumberBoundsAreReadAsNegativeIntegers() throws IOException {
		final RandomNumberVariable randomNumber = readVariable("random_number", "min: -2147483648\n    max: -1\n", RandomNumberVariable.class);

		assertEquals(Integer.MIN_VALUE, randomNumber.getMin());
		assertEquals(-1, randomNumber.getMax());
	}

	@Test
	public void decimalCounterStartFailsTheReadNamingFieldAndValue() {
		final String message = readError(variable("counter", "start: 1.5\n    end: 10\n    increment: 1\n"));

		assertEquals("Incorrect value '1.5' for 'variables[0].start': " + NOT_A_WHOLE_NUMBER, message);
	}

	@Test
	public void decimalCounterEndFailsTheReadNamingFieldAndValue() {
		final String message = readError(variable("counter", "start: 0\n    end: 10.2\n    increment: 1\n"));

		assertEquals("Incorrect value '10.2' for 'variables[0].end': " + NOT_A_WHOLE_NUMBER, message);
	}

	@Test
	public void decimalRandomNumberMinFailsTheReadNamingFieldAndValue() {
		final String message = readError(variable("random_number", "min: 0.5\n    max: 10\n"));

		assertEquals("Incorrect value '0.5' for 'variables[0].min': " + NOT_A_WHOLE_NUMBER, message);
	}

	@Test
	public void decimalRandomNumberMaxFailsTheReadNamingFieldAndValue() {
		final String message = readError(variable("random_number", "min: 0\n    max: 9.9\n"));

		assertEquals("Incorrect value '9.9' for 'variables[0].max': " + NOT_A_WHOLE_NUMBER, message);
	}

	@Test
	public void decimalSharedQueueConsumerTimeoutFailsTheReadNamingFieldAndValue() {
		final String message = readError(variable("shared_queue", "queue_size: 10\n    consumer_timeout: 1.5\n"));

		assertEquals("Incorrect value '1.5' for 'variables[0].consumer_timeout': " + NOT_A_WHOLE_NUMBER, message);
	}

	@Test
	public void decimalExtractorMatchNumberInAStepFailsTheReadNamingTheFullPath() {
		final String message = readError(DurationFields.actions("    - request:\n        url: http://host:80/\n        extractors:\n        - name: E\n          regexp: a(.*)\n          match_number: 1.5\n"));

		assertEquals("Incorrect value '1.5' for 'user_paths[0].actions.steps[0].request.extractors[0].match_number': " + NOT_A_WHOLE_NUMBER, message);
	}

	@Test
	public void decimalExtractorMatchNumberInANestedStepFailsTheReadNamingTheFullPath() {
		final String message = readError(DurationFields.actions("    - delay: 1s\n    - transaction:\n        name: T\n        steps:\n        - request:\n            url: http://host:80/\n            extractors:\n            - name: E\n              regexp: a(.*)\n              match_number: 1.5\n"));

		assertEquals("Incorrect value '1.5' for 'user_paths[0].actions.steps[1].transaction.steps[0].request.extractors[0].match_number': " + NOT_A_WHOLE_NUMBER, message);
	}

	@Test
	public void decimalInJsonFailsTheReadNamingFieldAndValue() {
		final String message = readError("{\"name\": \"P\", \"variables\": [{\"counter\": {\"name\": \"V\", \"start\": 1.5, \"end\": 10, \"increment\": 1}}]}");

		assertEquals("Incorrect value '1.5' for 'variables[0].start': " + NOT_A_WHOLE_NUMBER, message);
	}

	@Test
	public void constantLoadUsersBeyondIntFailsTheReadNamingFieldAndValue() {
		final String message = readError(DurationFields.scenario("  populations:\n  - name: C\n    constant_load:\n      users: 2147483648\n"));

		assertEquals("Incorrect value '2147483648' for 'scenarios[0].populations[0].constant_load.users': " + NOT_A_32_BIT_WHOLE_NUMBER, message);
	}

	@Test
	public void fileStartFromLineBeyondIntFailsTheReadNamingFieldAndValue() {
		final String message = readError(variable("file", "path: data/cities.csv\n    start_from_line: 9999999999\n"));

		assertEquals("Incorrect value '9999999999' for 'variables[0].start_from_line': " + NOT_A_32_BIT_WHOLE_NUMBER, message);
	}

	@Test
	public void counterEndBelowIntFailsTheReadNamingFieldAndValue() {
		final String message = readError(variable("counter", "start: 0\n    end: -2147483649\n    increment: 1\n"));

		assertEquals("Incorrect value '-2147483649' for 'variables[0].end': " + NOT_A_32_BIT_WHOLE_NUMBER, message);
	}

	@Test
	public void rendezvousReleaseUsersBeyondIntFailsTheReadNamingFieldAndValue() {
		final String message = readError(DurationFields.scenario("  rendezvous_policies:\n  - name: r\n    when: 3000000000\n"));

		assertEquals("Incorrect value '3000000000' for 'scenarios[0].rendezvous_policies[0].when': "
				+ "must be manual, a percentage from 1% to 100% or a number of users from 1 to 2147483647.", message);
	}

	@Test
	public void quotedRendezvousReleaseUsersBeyondIntFailsTheReadNamingFieldAndValue() {
		final String message = readError(DurationFields.scenario("  rendezvous_policies:\n  - name: r\n    when: \"3000000000\"\n"));

		assertEquals("Incorrect value '3000000000' for 'scenarios[0].rendezvous_policies[0].when': "
				+ "must be manual, a percentage from 1% to 100% or a number of users from 1 to 2147483647.", message);
	}
}
