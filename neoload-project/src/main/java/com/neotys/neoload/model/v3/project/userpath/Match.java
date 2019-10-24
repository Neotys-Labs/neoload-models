package com.neotys.neoload.model.v3.project.userpath;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Match {

    ANY, ALL;

    public static Match of(final String name) {
        if (!Strings.isNullOrEmpty(name)) {
            try {
                return Match.valueOf(name.toUpperCase());
            } catch (final IllegalArgumentException iae) {
                LOGGER.warn("Match operator not supported");
            }
        }
        throw new IllegalArgumentException("The Match must be: 'any' or 'all'.");
    }

    public String getName() {
        return name().toLowerCase();
    }

    public final static String MATCH = "match";

    private static final Logger LOGGER = LoggerFactory.getLogger(Match.class);
}
