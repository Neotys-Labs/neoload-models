package com.neotys.neoload.model.v3.validation.constraintvalidators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.UniqueNameEntity;
import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.validation.constraints.UniqueNameCheck;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.Test;

public class UniqueNameCheckTest {
	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

	private static final class Holder {
		@UniqueNameCheck
		private final List<Server> servers;
		@UniqueNameCheck
		private final List<ContentAssertion> assertions;

		private Holder(final List<Server> servers, final List<ContentAssertion> assertions) {
			this.servers = servers;
			this.assertions = assertions;
		}
	}

	private static Server server(final String name) {
		return Server.builder().name(name).host("host").build();
	}

	private static ContentAssertion assertion(final String name) {
		return ContentAssertion.builder().name(Optional.ofNullable(name)).build();
	}

	private static Set<String> violations(final Holder holder) {
		return VALIDATOR.validate(holder).stream()
				.map((ConstraintViolation<Holder> violation) -> violation.getPropertyPath().toString())
				.collect(Collectors.toSet());
	}

	@Test
	public void validatorIsResolvedPerFieldType() {
		final Holder holder = new Holder(
				Arrays.asList(server("a"), server("b"), server("a"), server("b")),
				Arrays.asList(assertion(null), assertion(null), assertion("x"), assertion("x")));

		assertEquals(Set.of("servers", "assertions"), violations(holder));
	}

	@Test
	public void nullListNullItemsAndEmptyNamesAreIgnored() {
		assertTrue(violations(new Holder(null, null)).isEmpty());
		assertTrue(violations(new Holder(Arrays.asList(server(null), null, server(""), server(""), server("a")), Arrays.asList(assertion(null), assertion(""), assertion(""))))
				.isEmpty());
	}

	@Test
	public void comparisonIsCaseSensitive() {
		assertTrue(violations(new Holder(Arrays.asList(server("Foo"), server("foo")), null)).isEmpty());
	}

	@Test
	public void elementIsAUniqueNameEntity() {
		assertTrue(UniqueNameEntity.class.isAssignableFrom(Element.class));
	}
}
