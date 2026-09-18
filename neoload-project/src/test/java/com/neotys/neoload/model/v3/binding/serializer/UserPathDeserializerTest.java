package com.neotys.neoload.model.v3.binding.serializer;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

public class UserPathDeserializerTest {

	private static UserPath deserialize(final String json) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new GuavaModule());
		mapper.registerModule(new Jdk8Module());
		return mapper.readValue(json, UserPath.class);
	}

	@Test
	public void legacyAssertionsKeyFallsBackToContentAssertions() throws IOException {
		final UserPath userPath = deserialize("{\"name\":\"MyUserPath\",\"actions\":{\"steps\":[]},"
				+ "\"assertions\":[{\"contains\":\"legacy\"}]}");

		final List<ContentAssertion> expected = ImmutableList.of(ContentAssertion.builder().contains("legacy").build());
		assertEquals(expected, userPath.getContentAssertions());
	}

	@Test
	public void newContentAssertionsKeyLeavesLegacyAssertionsEmpty() throws IOException {
		final UserPath userPath = deserialize("{\"name\":\"MyUserPath\",\"actions\":{\"steps\":[]},"
				+ "\"content_assertions\":[{\"contains\":\"new_key\"}]}");

		final List<ContentAssertion> expected = ImmutableList.of(ContentAssertion.builder().contains("new_key").build());
		assertEquals(expected, userPath.getContentAssertions());
	}
}
