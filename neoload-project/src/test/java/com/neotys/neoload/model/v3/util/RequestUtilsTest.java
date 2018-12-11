package com.neotys.neoload.model.v3.util;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.server.Server.Scheme;


public class RequestUtilsTest {
	@Test
	public void parseUrl() {
		

		// http://www.neotys.com
		URL expectedUrl = URL.builder()
				.server(Server.builder()
						.name("www.neotys.com")
						.scheme(Scheme.HTTP)
						.host("www.neotys.com")
						.port("80")
						.build())
				.path("")
				.build();		
		URL actualUrl = RequestUtils.parseUrl("http://www.neotys.com");
		assertEquals(expectedUrl, actualUrl);

		// http://${host-neotys}
		expectedUrl = URL.builder()
				.server(Server.builder()
						.name("host-neotys")
						.scheme(Scheme.HTTP)
						.host("${host-neotys}")
						.port("80")
						.build())
				.path("")
				.build();		
		actualUrl = RequestUtils.parseUrl("http://${host-neotys}");
		assertEquals(expectedUrl, actualUrl);

		// http://www.neotys.com:8080
		expectedUrl = URL.builder()
				.server(Server.builder()
						.name("www.neotys.com")
						.scheme(Scheme.HTTP)
						.host("www.neotys.com")
						.port("8080")
						.build())
				.path("")
				.build();		
		actualUrl = RequestUtils.parseUrl("http://www.neotys.com:8080");
		assertEquals(expectedUrl, actualUrl);

		// http://${host-neotys}:${port-neotys}
		expectedUrl = URL.builder()
				.server(Server.builder()
						.name("host-neotys")
						.scheme(Scheme.HTTP)
						.host("${host-neotys}")
						.port("${port-neotys}")
						.build())
				.path("")
				.build();		
		actualUrl = RequestUtils.parseUrl("http://${host-neotys}:${port-neotys}");
		assertEquals(expectedUrl, actualUrl);

		// http://www.neotys.com:8080/
		expectedUrl = URL.builder()
				.server(Server.builder()
						.name("www.neotys.com")
						.scheme(Scheme.HTTP)
						.host("www.neotys.com")
						.port("8080")
						.build())
				.path("/")
				.build();		
		actualUrl = RequestUtils.parseUrl("http://www.neotys.com:8080/");
		assertEquals(expectedUrl, actualUrl);

		// http://${host-neotys}:${port-neotys}/
		expectedUrl = URL.builder()
				.server(Server.builder()
						.name("host-neotys")
						.scheme(Scheme.HTTP)
						.host("${host-neotys}")
						.port("${port-neotys}")
						.build())
				.path("/")
				.build();		
		actualUrl = RequestUtils.parseUrl("http://${host-neotys}:${port-neotys}/");
		assertEquals(expectedUrl, actualUrl);
		
		// http://www.neotys.com:80/select?name=neoload
		expectedUrl = URL.builder()
				.server(Server.builder()
						.name("www.neotys.com")
						.scheme(Scheme.HTTP)
						.host("www.neotys.com")
						.port("80")
						.build())
				.path("/select")
				.query("name=neoload")
				.build();
		actualUrl = RequestUtils.parseUrl("http://www.neotys.com:80/select?name=neoload");
		assertEquals(expectedUrl, actualUrl);
		
		// /select?name=neoload
		expectedUrl = URL.builder()
				.path("/select")
				.query("name=neoload")
				.build();
		actualUrl = RequestUtils.parseUrl("/select?name=neoload");
		assertEquals(expectedUrl, actualUrl);

		// http://petstore.swagger.io:80/v2/pet/${ExtractedVariable_id}
		expectedUrl = URL.builder()
				.server(Server.builder()
						.name("petstore.swagger.io")
						.scheme(Scheme.HTTP)
						.host("petstore.swagger.io")
						.port("80")
						.build())
				.path("/v2/pet/${ExtractedVariable_id}")
				.build();
		actualUrl = RequestUtils.parseUrl("http://petstore.swagger.io:80/v2/pet/${ExtractedVariable_id}");
		assertEquals(expectedUrl, actualUrl);
		
	}
}
