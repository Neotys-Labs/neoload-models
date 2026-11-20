package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.binding.converter.DurationParser;
import com.neotys.neoload.model.v3.project.userpath.Duration;
import com.neotys.neoload.model.v3.project.userpath.DurationLiteral;
import com.neotys.neoload.model.v3.project.userpath.DurationOrVariable;
import com.neotys.neoload.model.v3.project.userpath.DurationVariable;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

/**
 * Reads a {@link DurationOrVariable}: a variable reference such as {@code ${name}}, otherwise a duration read by
 * {@link DurationParser}, a plain number meaning milliseconds.
 */
public final class DurationOrVariableDeserializer extends StdDeserializer<DurationOrVariable> {
	private static final long serialVersionUID = 1L;
	private static final Pattern VARIABLE = Pattern.compile(Duration.VARIABLE_PATTERN);

	public DurationOrVariableDeserializer() {
		super(DurationOrVariable.class);
	}

	@Override
	public DurationOrVariable deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		return parse(parser.getValueAsString());
	}

	/**
	 * @throws com.neotys.neoload.model.v3.binding.converter.InvalidDurationException if the text is neither a variable nor a valid duration
	 */
	static DurationOrVariable parse(final String text) {
		if (text != null && VARIABLE.matcher(text.trim()).matches()) {
			return DurationVariable.builder().value(text.trim()).build();
		}
		return DurationLiteral.builder().value(DurationParser.parse(text, ChronoUnit.MILLIS)).build();
	}
}
