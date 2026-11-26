package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.HttpBasedRequest;
import com.neotys.neoload.model.v3.project.userpath.HttpBasedRequestWithResponse;
import com.neotys.neoload.model.v3.project.userpath.NameValue;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.SoapRequest;
import com.neotys.neoload.model.v3.project.userpath.WebSocketChannel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class IOHttpBasedRequestTest extends AbstractIOElementsTest {

	private Project load(final String fixture) throws IOException {
		return new IO().read(content(fixture)).getProject();
	}

	private String written(final String fixture) throws IOException {
		return new IO().write(load(fixture), Format.YAML);
	}

	private String content(final String fixture) throws IOException {
		return getContent(getFile(fixture, "yaml"), StandardCharsets.UTF_8).replace("\r\n", "\n");
	}

	private static <T> T firstStep(final Project project, final Class<T> type) {
		return type.cast(project.getUserPaths().get(0).getActions().getSteps().get(0));
	}

	private static List<String> render(final HttpBasedRequest request) {
		return request.getParameters().stream()
				.map(p -> p.getName() + p.getValue().map(v -> "=" + v).orElse(""))
				.collect(Collectors.toList());
	}

	@Test
	public void queryInUrlBecomesLeadingParameters() throws IOException {
		final Request request = firstStep(load("test-http-request-query-in-url"), Request.class);

		assertEquals("/search", request.getUrl());
		assertEquals(List.of("q=neoload", "page=1", "page=2", "debug", "lang=en"), render(request));
		assertFalse(request.getParameters().get(3).getValue().isPresent());
	}

	@Test
	public void urlWithoutQueryIsLeftUntouched() throws IOException {
		final Request request = firstStep(load("test-http-request-no-query"), Request.class);

		assertEquals("/search", request.getUrl());
		assertEquals(List.of("lang=en"), render(request));
		assertEquals(content("test-http-request-no-query"), written("test-http-request-no-query"));
	}

	@Test
	public void parametersFieldAloneIsKept() throws IOException {
		final Request request = firstStep(load("test-http-request-parameters-only"), Request.class);

		assertEquals(List.of("a=1", "a=2", "flag"), render(request));
	}

	@Test
	public void savedYamlHasCleanUrl() throws IOException {
		final String yaml = written("test-http-request-query-in-url");
		final Request reloaded = firstStep(new IO().read(yaml).getProject(), Request.class);

		assertFalse(yaml.contains("?"));
		assertEquals("/search", reloaded.getUrl());
		assertEquals(List.of("q=neoload", "page=1", "page=2", "debug", "lang=en"), render(reloaded));
	}

	@Test
	public void formEncodedBodyIsNotAffected() throws IOException {
		final Request request = firstStep(load("test-http-request-form-body"), Request.class);

		assertEquals("user=bob&pwd=x", request.getBody().orElseThrow());
		assertTrue(request.getParameters().isEmpty());
		assertEquals(content("test-http-request-form-body"), written("test-http-request-form-body"));
	}

	@Test
	public void soapRequestSupportsParametersAndHeaders() throws IOException {
		final SoapRequest soap = firstStep(load("test-http-soap-request-query-in-url"), SoapRequest.class);

		assertEquals("/quote", soap.getUrl());
		assertEquals(List.of("symbol=TCS", "lang=en"), render(soap));
		assertEquals(List.of(NameValue.builder().name("SOAPAction").value("getQuote").build()), soap.getHeaders());
		assertEquals(content("test-http-soap-request-query-in-url-clean"), written("test-http-soap-request-query-in-url"));
	}

	@Test
	public void websocketChannelSupportsParametersAndHeaders() throws IOException {
		final WebSocketChannel channel = firstStep(load("test-http-websocket-channel-query-in-url"), WebSocketChannel.class);

		assertEquals("/ws", channel.getUrl());
		assertEquals(List.of("token=abc", "lang=en"), render(channel));
		assertEquals(List.of(NameValue.builder().name("Origin").value("https://example.com").build()), channel.getHeaders());
		assertEquals(content("test-http-websocket-channel-query-in-url-clean"), written("test-http-websocket-channel-query-in-url"));
	}

	@Test
	public void soapRequestAcceptsExtractors() throws IOException {
		final SoapRequest soap = firstStep(load("test-http-soap-request-extractors"), SoapRequest.class);

		assertEquals(1, soap.getExtractors().size());
		assertEquals("MyVariable1", soap.getExtractors().get(0).getName());
		assertEquals(content("test-http-soap-request-extractors"), written("test-http-soap-request-extractors"));
	}

	@Test
	public void websocketChannelAcceptsFollowRedirectsAndResponseStorage() throws IOException {
		final WebSocketChannel channel = firstStep(load("test-http-websocket-channel-redirects-storage"), WebSocketChannel.class);

		assertTrue(channel.getFollowRedirects());
		assertEquals("wsResponseFile", channel.getResponseStorage().orElseThrow().getVariable());
		assertEquals(content("test-http-websocket-channel-redirects-storage"), written("test-http-websocket-channel-redirects-storage"));
	}

	@Test
	public void websocketChannelFollowRedirectsDefaultIsNotWritten() throws IOException {
		final WebSocketChannel channel = firstStep(load("test-http-websocket-channel-redirects-default"), WebSocketChannel.class);

		assertFalse(channel.getFollowRedirects());
		assertFalse(written("test-http-websocket-channel-redirects-default").contains(HttpBasedRequestWithResponse.FOLLOW_REDIRECTS));
	}

	@Test
	public void existingProjectWithQueryAndNoParametersLoads() throws IOException {
		final Request request = firstStep(load("test-request-only-required-legacy-query"), Request.class);

		assertEquals("http://www.neotys.com/select", request.getUrl());
		assertEquals(List.of("name:neoload"), render(request));
		assertTrue(written("test-request-only-required-legacy-query").contains("- request:"));
	}

	@Test
	public void emptyQuerySegmentsAreSkipped() throws IOException {
		final Request middle = firstStep(load("test-http-request-query-empty-segments"), Request.class);
		final Request leading = firstStep(load("test-http-request-query-leading-ampersand"), Request.class);

		assertEquals("/p", middle.getUrl());
		assertEquals(List.of("a=1", "b=2"), render(middle));
		assertEquals("/p", leading.getUrl());
		assertEquals(List.of("b=2"), render(leading));
		assertEquals(List.of("a=1", "b=2"), render(firstStep(new IO().read(written("test-http-request-query-empty-segments")).getProject(), Request.class)));
	}

	@Test
	public void equalsSignInQueryValueIsKept() throws IOException {
		final Request request = firstStep(load("test-http-request-query-equals-in-value"), Request.class);

		assertEquals(List.of("token=abc==", "a=b=c"), render(request));
		assertEquals(List.of("token=abc==", "a=b=c"), render(firstStep(new IO().read(written("test-http-request-query-equals-in-value")).getProject(), Request.class)));
	}
}
