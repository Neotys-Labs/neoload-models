package com.neotys.neoload.model.v3.binding.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.neotys.neoload.model.v3.binding.serializer.ifthenelse.ConditionHelper;
import com.neotys.neoload.model.v3.project.userpath.Condition;

/**
 * Serializes a {@link Condition} to its compact textual form (e.g. {@code ${var} equals 2}),
 * mirroring {@link ConditionDeserializer}.
 */
public final class ConditionSerializer extends StdSerializer<Condition> {
	private static final long serialVersionUID = -9106185970275309525L;

	public ConditionSerializer() {
		super(Condition.class);
	}

	@Override
	public void serialize(final Condition condition, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		final String text = ConditionHelper.convertToString(condition);
		// For YAML, force the double-quoted scalar style so the condition renders as
		// "'operand 1' equals 'operand 2'" instead of the single-quoted-with-doubling form.
		// MINIMIZE_QUOTES is disabled only around this write, then restored.
		if (generator instanceof YAMLGenerator) {
			final YAMLGenerator yaml = (YAMLGenerator) generator;
			final boolean wasMinimize = yaml.isEnabled(YAMLGenerator.Feature.MINIMIZE_QUOTES);
			yaml.disable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
			try {
				yaml.writeString(text);
			} finally {
				if (wasMinimize) {
					yaml.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
				}
			}
		} else {
			generator.writeString(text);
		}
	}
}
