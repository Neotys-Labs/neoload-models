package com.neotys.neoload.model.readers.jmeter.step.httpRequest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HTTPDefaultSetModelTest {

    @Test
    public void testCheckPath(){
        ImmutableHTTPDefaultSetModel withoutPath = ImmutableHTTPDefaultSetModel.builder()
                .path("")
                .build();
        assertEquals(withoutPath.checkPath(), "/");

        ImmutableHTTPDefaultSetModel withPath = ImmutableHTTPDefaultSetModel.builder()
                .path("/auth")
                .build();
        assertEquals(withPath.checkPath(), "/auth");

    }

    @Test
    public void testCheckProtocol(){
        ImmutableHTTPDefaultSetModel withoutProtocol = ImmutableHTTPDefaultSetModel.builder()
                .protocol("")
                .build();
        assertEquals(withoutProtocol.checkProtocol(), "http");

        ImmutableHTTPDefaultSetModel withProtocol = ImmutableHTTPDefaultSetModel.builder()
                .protocol("https")
                .build();
        assertEquals(withProtocol.checkProtocol(), "https");

    }

    @Test
    public void testCheckDomain(){
        ImmutableHTTPDefaultSetModel withoutDomain = ImmutableHTTPDefaultSetModel.builder()
                .domain("")
                .build();
        assertEquals(withoutDomain.checkDomain(), "localhost");

        ImmutableHTTPDefaultSetModel withProtocol = ImmutableHTTPDefaultSetModel.builder()
                .domain("google.com")
                .build();
        assertEquals(withProtocol.checkDomain(), "google.com");
    }

    @Test
    public void testCheckPort(){
        ImmutableHTTPDefaultSetModel withoutPort = ImmutableHTTPDefaultSetModel.builder()
                .port("")
                .protocol("http")
                .build();
        assertEquals(withoutPort.checkPort(), 80);

        ImmutableHTTPDefaultSetModel withoutPort2 = ImmutableHTTPDefaultSetModel.builder()
                .port("")
                .protocol("https")
                .build();
        assertEquals(withoutPort2.checkPort(), 443);

        ImmutableHTTPDefaultSetModel withPort = ImmutableHTTPDefaultSetModel.builder()
                .port("8080")
                .build();
        assertEquals(withPort.checkPort(), 8080);
    }
}