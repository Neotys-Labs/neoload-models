package com.neotys.neoload.model.v3.binding.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neotys.neoload.model.v3.project.userpath.Match;

/**
 * Serializes a {@link Match} to its lower-case name ({@code any}/{@code all}), mirroring the
 * {@link com.neotys.neoload.model.v3.binding.serializer.MatchDeserializer} which reads it with
 * {@link Match#of(String)}.
 */
public final class MatchToStringConverter extends StdConverter<Match, String> {

	@Override
	public String convert(final Match match) {
		if (match == null) return null;

		return match.getName();
	}
}
