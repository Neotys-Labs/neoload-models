package com.neotys.neoload.model.v3.project.userpath;

public final class Percentages {
	private Percentages() {
	}

	public static PercentageOrVariable of(final String integerOrVariable) {
		if (integerOrVariable.startsWith("${")) {
			return PercentageVariable.builder().value(integerOrVariable).build();
		}
		return PercentageLiteral.builder().value(Integer.parseInt(integerOrVariable)).build();
	}
}
