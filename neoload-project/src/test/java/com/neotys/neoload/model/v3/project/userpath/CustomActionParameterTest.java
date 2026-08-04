package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neotys.neoload.model.v3.project.userpath.CustomActionParameter.Type;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Pins the accepted YAML/JSON vocabulary of {@link CustomActionParameter.Type}. It carries no
 * {@code @JsonProperty}, so the wire value is the enum name.
 */
public class CustomActionParameterTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static String wire(final Object enumValue) throws Exception {
        final String json = MAPPER.writeValueAsString(enumValue);
        return json.substring(1, json.length() - 1);
    }

    @Test
    public void typeValues() {
        assertArrayEquals(new Type[]{Type.TEXT, Type.PASSWORD}, Type.values());
    }

    @Test
    public void typeWire() throws Exception {
        assertEquals("TEXT", wire(Type.TEXT));
        assertEquals("PASSWORD", wire(Type.PASSWORD));
    }
}
