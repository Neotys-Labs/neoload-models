package com.neotys.neoload.model.v3.binding.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.neotys.neoload.model.v3.binding.converter.PercentageParser;
import com.neotys.neoload.model.v3.project.userpath.PercentageLiteral;
import com.neotys.neoload.model.v3.project.userpath.PercentageOrVariable;
import com.neotys.neoload.model.v3.project.userpath.PercentageVariable;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Reads a {@link PercentageOrVariable}: a variable reference such as {@code ${name}}, otherwise an integer
 * percentage read by {@link PercentageParser}, the percent sign being optional.
 */
public final class PercentageOrVariableDeserializer extends StdDeserializer<PercentageOrVariable> {
	private static final long serialVersionUID = 1L;
	private static final Pattern VARIABLE = Pattern.compile("\\$\\{[^}]+\\}");

	public PercentageOrVariableDeserializer() {
		super(PercentageOrVariable.class);
	}

	@Override
	public PercentageOrVariable deserialize(final JsonParser parser, final DeserializationContext ctx) throws IOException {
		return parse(parser.getValueAsString());
	}

	/**
	 * @throws com.neotys.neoload.model.v3.binding.converter.InvalidPercentageException if the text is neither a variable nor a valid percentage
	 */
	static PercentageOrVariable parse(final String text) {
		if (text != null && VARIABLE.matcher(text).matches()) {
			return PercentageVariable.builder().value(text).build();
		}
		return PercentageLiteral.builder().value(PercentageParser.parse(text)).build();
	}
}
