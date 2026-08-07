package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neotys.neoload.model.v3.project.variable.ChangePolicyElement.ChangePolicy;
import com.neotys.neoload.model.v3.project.variable.OutOfValueElement.OutOfValue;
import com.neotys.neoload.model.v3.project.variable.OrderElement.Order;
import com.neotys.neoload.model.v3.project.variable.ScopeElement.Scope;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Pins the accepted YAML/JSON vocabulary of the {@link Variable} enums: both the exact set of
 * constants and, for each, the wire string produced by serialization ({@code @JsonProperty} value).
 * A rename/removal/reorder that would break the external contract is caught here.
 */
public class VariableTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Serialized value of an enum constant (its @JsonProperty wire string), without the JSON quotes.
    private static String wire(final Object enumValue) throws Exception {
        final String json = MAPPER.writeValueAsString(enumValue);
        return json.substring(1, json.length() - 1);
    }

    @Test
    public void changePolicyValues() {
        assertArrayEquals(new ChangePolicy[]{
                ChangePolicy.EACH_USE, ChangePolicy.EACH_REQUEST, ChangePolicy.EACH_PAGE,
                ChangePolicy.EACH_USER, ChangePolicy.EACH_ITERATION
        }, ChangePolicy.values());
    }

    @Test
    public void changePolicyWire() throws Exception {
        assertEquals("each_use", wire(ChangePolicy.EACH_USE));
        assertEquals("each_request", wire(ChangePolicy.EACH_REQUEST));
        assertEquals("each_page", wire(ChangePolicy.EACH_PAGE));
        assertEquals("each_user", wire(ChangePolicy.EACH_USER));
        assertEquals("each_iteration", wire(ChangePolicy.EACH_ITERATION));
    }

    @Test
    public void scopeValues() {
        assertArrayEquals(new Scope[]{Scope.UNIQUE, Scope.GLOBAL, Scope.LOCAL}, Scope.values());
    }

    @Test
    public void scopeWire() throws Exception {
        assertEquals("unique", wire(Scope.UNIQUE));
        assertEquals("global", wire(Scope.GLOBAL));
        assertEquals("local", wire(Scope.LOCAL));
    }

    @Test
    public void orderValues() {
        assertArrayEquals(new Order[]{Order.SEQUENTIAL, Order.RANDOM, Order.ANY}, Order.values());
    }

    @Test
    public void orderWire() throws Exception {
        assertEquals("sequential", wire(Order.SEQUENTIAL));
        assertEquals("random", wire(Order.RANDOM));
        assertEquals("any", wire(Order.ANY));
    }

    @Test
    public void outOfValueValues() {
        assertArrayEquals(new OutOfValue[]{OutOfValue.CYCLE, OutOfValue.STOP, OutOfValue.NO_VALUE}, OutOfValue.values());
    }

    @Test
    public void outOfValueWire() throws Exception {
        assertEquals("cycle", wire(OutOfValue.CYCLE));
        assertEquals("stop_test", wire(OutOfValue.STOP));
        assertEquals("no_value_code", wire(OutOfValue.NO_VALUE));
    }
}
