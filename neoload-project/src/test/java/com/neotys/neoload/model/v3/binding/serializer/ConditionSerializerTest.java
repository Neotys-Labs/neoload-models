package com.neotys.neoload.model.v3.binding.serializer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neotys.neoload.model.v3.project.userpath.Condition;


public class ConditionSerializerTest {

	@Test
	public void serializeTwoOperands() throws IOException {
		assertEquals("\"operand1 equals operand2\"",
				serialize(Condition.builder()
						.operand1("operand1")
						.operator(Condition.Operator.EQUALS)
						.operand2("operand2")
						.build()));
	}

	@Test
	public void serializeSingleOperand() throws IOException {
		assertEquals("\"${variable} exists\"",
				serialize(Condition.builder()
						.operand1("${variable}")
						.operator(Condition.Operator.EXISTS)
						.build()));
	}

	private static String serialize(final Condition condition) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		final StringWriter writer = new StringWriter();
		final JsonGenerator generator = mapper.getFactory().createGenerator(writer);
		new ConditionSerializer().serialize(condition, generator, mapper.getSerializerProviderInstance());
		generator.flush();
		return writer.toString();
	}
}
