package com.neotys.neoload.model.v3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.server.Server.Scheme;
import com.neotys.neoload.model.v3.project.userpath.Header;
import com.neotys.neoload.model.v3.project.userpath.Request.Method;

public class RequestUtilsTest {
	@Test
	public void parseUrl() {
		boolean throwException = false;
		try {
			RequestUtils.parseUrl(null);
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'url' must not be null or empty."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'url' must not be null or empty.");
		}
		throwException = false;
		try {
			RequestUtils.parseUrl("");
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'url' must not be null or empty."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'url' must not be null or empty.");
		}
		throwException = false;
		try {
			RequestUtils.parseUrl(" \t \r\n ");
		}
		catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The parameter 'url' must not be blank."));
			throwException = true;
		}
		if (!throwException) {
			fail("The parameter 'url' must not blank.");
		}

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
	

	@Test
	public void getParameters() {
		assertNotNull(RequestUtils.getParameters(null));
		assertEquals(0, RequestUtils.getParameters(null).size());
		assertNotNull(RequestUtils.getParameters(""));
		assertEquals(0, RequestUtils.getParameters("").size());
		
		assertNotNull(RequestUtils.getParameters(" \t \r\n "));
		assertEquals(0, RequestUtils.getParameters(" \t \r\n ").size());
		
		Parameter parameter = Parameter.builder()
				.name("name")
				.build();
		assertEquals(Arrays.asList(parameter), RequestUtils.getParameters("name"));
		parameter = Parameter.builder()
				.name("name")
				.value("")
				.build();
		assertEquals(Arrays.asList(parameter), RequestUtils.getParameters("name="));
		parameter = Parameter.builder()
				.name("name")
				.value("value")
				.build();
		assertEquals(Arrays.asList(parameter), RequestUtils.getParameters("name=value"));

		final Parameter parameter1 = Parameter.builder()
				.name("name1")
				.value("value1")
				.build();
		final Parameter parameter2 = Parameter.builder()
				.name("name2")
				.build();
		final Parameter parameter3 = Parameter.builder()
				.name("name3")
				.value("")
				.build();
		final Parameter parameter4 = Parameter.builder()
				.name("name4")
				.value("value4")
				.build();
		assertEquals(Arrays.asList(parameter1, parameter2, parameter3, parameter4), RequestUtils.getParameters("name1=value1&name2&name3=&name4=value4"));
	}
	
	@Test
	public void getEncodeUrlValue() {
		assertFalse(RequestUtils.getEncodeUrlValue(null).isPresent());
		assertFalse(RequestUtils.getEncodeUrlValue("").isPresent());
		assertFalse(RequestUtils.getEncodeUrlValue(" \t \r\n ").isPresent());
		
		assertFalse(RequestUtils.getEncodeUrlValue("var_name").isPresent());
		assertFalse(RequestUtils.getEncodeUrlValue("${var_name}").isPresent());
		
		assertFalse(RequestUtils.getEncodeUrlValue("__encodeURL(${var_name}").isPresent());
		assertFalse(RequestUtils.getEncodeUrlValue("${var_name})").isPresent());

		assertFalse(RequestUtils.getEncodeUrlValue("_encodeURL(${var_name})").isPresent());
		
		assertFalse(RequestUtils.getEncodeUrlValue("_encodeURL()").isPresent());
		assertFalse(RequestUtils.getEncodeUrlValue("_encodeURL( \t \r\n )").isPresent());

		assertEquals("var_name", RequestUtils.getEncodeUrlValue("__encodeURL(var_name)").get());
		assertEquals("var_name", RequestUtils.getEncodeUrlValue(" \t \r\n __encodeURL( \t \r\n var_name \t \r\n ) \t \r\n ").get());
		
		assertEquals("${var_name}", RequestUtils.getEncodeUrlValue("__encodeURL(${var_name})").get());
		assertEquals("${var_name}", RequestUtils.getEncodeUrlValue(" \t \r\n __encodeURL( \t \r\n ${var_name} \t \r\n ) \t \r\n ").get());
	}

	public void isEncodeUrlSyntax() {
		assertFalse(RequestUtils.isEncodeUrlSyntax(null));
		assertFalse(RequestUtils.isEncodeUrlSyntax(""));
		assertFalse(RequestUtils.isEncodeUrlSyntax(" \t \r\n "));
		
		assertFalse(RequestUtils.isEncodeUrlSyntax("var_name"));
		assertFalse(RequestUtils.isEncodeUrlSyntax("${var_name}"));
		
		assertFalse(RequestUtils.isEncodeUrlSyntax("__encodeURL(${var_name}"));
		assertFalse(RequestUtils.isEncodeUrlSyntax("${var_name})"));

		assertFalse(RequestUtils.isEncodeUrlSyntax("_encodeURL(${var_name})"));

		assertTrue(RequestUtils.isEncodeUrlSyntax("__encodeURL(${var_name})"));
		assertTrue(RequestUtils.isEncodeUrlSyntax(" \t \r\n __encodeURL( \t \r\n ${var_name} \t \r\n ) \t \r\n "));
	}

	@Test
	public void isGetLikeMethod() {
		assertFalse(RequestUtils.isGetLikeMethod(null));
		assertTrue(RequestUtils.isGetLikeMethod(Method.GET));
		assertFalse(RequestUtils.isGetLikeMethod(Method.POST));
		assertTrue(RequestUtils.isGetLikeMethod(Method.HEAD));
		assertFalse(RequestUtils.isGetLikeMethod(Method.PUT));
		assertFalse(RequestUtils.isGetLikeMethod(Method.DELETE));
		assertTrue(RequestUtils.isGetLikeMethod(Method.OPTIONS));
		assertTrue(RequestUtils.isGetLikeMethod(Method.TRACE));
		assertFalse(RequestUtils.isGetLikeMethod(Method.CUSTOM));
	}

	@Test
	public void isPostLikeMethod() {
		assertFalse(RequestUtils.isPostLikeMethod(null));
		assertFalse(RequestUtils.isPostLikeMethod(Method.GET));
		assertTrue(RequestUtils.isPostLikeMethod(Method.POST));
		assertFalse(RequestUtils.isPostLikeMethod(Method.HEAD));
		assertTrue(RequestUtils.isPostLikeMethod(Method.PUT));
		assertTrue(RequestUtils.isPostLikeMethod(Method.DELETE));
		assertFalse(RequestUtils.isPostLikeMethod(Method.OPTIONS));
		assertFalse(RequestUtils.isPostLikeMethod(Method.TRACE));
		assertTrue(RequestUtils.isPostLikeMethod(Method.CUSTOM));
	}

	@Test
	public void containFormHeader() {
		assertFalse(RequestUtils.containFormHeader(null));
		assertFalse(RequestUtils.containFormHeader(Arrays.asList()));
		
		List<Header> headers = Arrays.asList(
				Header.builder()
					.name("name1")
					.build(),
				Header.builder()
					.name("name2")
					.build(),
				Header.builder()
					.name("name3")
					.build()				
		);
		assertFalse(RequestUtils.containFormHeader(headers));
		
		headers = Arrays.asList(
				Header.builder()
					.name("name")
					.value("value")
					.build(),
				Header.builder()
					.name("Content-Type")
					.build(),
				Header.builder()
					.name("name")
					.value("value")
					.build()				
		);
		assertFalse(RequestUtils.containFormHeader(headers));

		headers = Arrays.asList(
				Header.builder()
					.name("name")
					.value("value")
					.build(),
				Header.builder()
					.name("Content-Type")
					.value("")
					.build(),
				Header.builder()
					.name("name")
					.value("value")
					.build()				
		);
		assertFalse(RequestUtils.containFormHeader(headers));

		headers = Arrays.asList(
				Header.builder()
					.name("name")
					.value("value")
					.build(),
				Header.builder()
					.name("Content-Type")
					.value("value")
					.build(),
				Header.builder()
					.name("name")
					.value("value")
					.build()				
		);
		assertFalse(RequestUtils.containFormHeader(headers));

		headers = Arrays.asList(
				Header.builder()
					.name("name")
					.value("value")
					.build(),
				Header.builder()
					.name("Content-Type")
					.value("application/x-www-form-urlencoded")
					.build(),
				Header.builder()
					.name("name")
					.value("value")
					.build()				
		);
		assertTrue(RequestUtils.containFormHeader(headers));
	}

	@Test
	public void findHeader() {
		assertFalse(RequestUtils.findHeader(null, "name").isPresent());
		assertFalse(RequestUtils.findHeader(Arrays.asList(), "name").isPresent());
		
		assertFalse(RequestUtils.findHeader(Arrays.asList(Header.builder().name("name").build()), null).isPresent());
		assertFalse(RequestUtils.findHeader(Arrays.asList(Header.builder().name("name").build()), "").isPresent());
		assertFalse(RequestUtils.findHeader(Arrays.asList(Header.builder().name("name").build()), " \t \r\n ").isPresent());

		List<Header> headers = Arrays.asList(
				Header.builder()
					.name("name1")
					.build(),
				Header.builder()
					.name("name2")
					.build(),
				Header.builder()
					.name("name3")
					.build()				
		);
		assertFalse(RequestUtils.findHeader(headers, "name").isPresent());
		assertTrue(RequestUtils.findHeader(headers, "name1").isPresent());
		assertTrue(RequestUtils.findHeader(headers, "name2").isPresent());
		assertTrue(RequestUtils.findHeader(headers, "name3").isPresent());
		
		
		headers = Arrays.asList(
				Header.builder()
					.name("name")
					.value("value1")
					.build(),
				Header.builder()
					.name("name")
					.value("value2")
					.build(),
				Header.builder()
					.name("name")
					.value("value3")
					.build()				
		);
		assertEquals(Header.builder().name("name").value("value1").build(), RequestUtils.findHeader(headers, "name").get());
	}
	
	@Test
	public void isBinary() {
		assertFalse(RequestUtils.isBinary(null));
		assertFalse(RequestUtils.isBinary(""));
		assertFalse(RequestUtils.isBinary(" \t \r\n "));
		assertFalse(RequestUtils.isBinary("application/x-www-form-urlencoded"));
		assertTrue(RequestUtils.isBinary("application/octet-stream"));
	}
	
	@Test
	public void isForm() {
		assertFalse(RequestUtils.isForm(null));
		assertFalse(RequestUtils.isForm(""));
		assertFalse(RequestUtils.isForm(" \t \r\n "));
		assertFalse(RequestUtils.isForm("application/octet-stream"));
		assertTrue(RequestUtils.isForm("application/x-www-form-urlencoded"));		
	}
}
