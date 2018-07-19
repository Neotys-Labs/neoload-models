package com.neotys.neoload.model.readers.loadrunner;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import com.neotys.neoload.model.listener.TestEventListener;
import org.junit.Test;

import com.neotys.neoload.model.repository.GetRequest;
import com.neotys.neoload.model.repository.ImmutableGetRequest;
import com.neotys.neoload.model.repository.ImmutablePage;
import com.neotys.neoload.model.repository.ImmutableParameter;
import com.neotys.neoload.model.repository.ImmutableServer;
import com.neotys.neoload.model.repository.Page;
import com.neotys.neoload.model.repository.Request.HttpMethod;
import com.neotys.neoload.model.repository.Server;

public class WebUrlTest {
	
	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");
	private static final LoadRunnerVUVisitor LOAD_RUNNER_VISITOR = new LoadRunnerVUVisitor(LOAD_RUNNER_READER, "{", "}", "");
		
	public static final MethodCall WEB_URL_FULL_TEST = ImmutableMethodCall.builder()
			.name("\"test_web_url\"")
			.addParameters("\"test_web_url_full\"")
			.addParameters("\"URL=https://server.test.com/test/path?testArgNoValue&testArgWithValue=value\"")
			.addParameters("\"Resource=0\"")
			.addParameters("\"RecContentType=text/xml\"")
			.addParameters("\"Referer=\"")
			.addParameters("\"Snapshot=t7.inf\"")
			.addParameters("\"Mode=HTML\"")
			.addParameters("EXTRARES")
			.addParameters("\"URL=/path/resource/1?ArgWithValue2=value%202\"")
			.addParameters("\"Referer=https://server.test.com/test/path\"")
			.addParameters("ENDITEM")
			.addParameters("\"Url=https://external.server.test.com/resource_path_2?OnlyOneArg\"")
			.addParameters("\"Referer=https://server.test.com/test/path\"")
			.addParameters("ENDITEM")
			.addParameters("LAST")
			.build();
	
	public static final MethodCall WEB_URL_SIMPLE_TEST = ImmutableMethodCall.builder()
			.name("\"test_web_url\"")
			.addParameters("\"test_web_url_simple\"")
			.addParameters("\"URL=https://server.test.com/\"")
			.addParameters("\"Resource=0\"")
			.addParameters("\"RecContentType=text/xml\"")
			.addParameters("\"Referer=\"")
			.addParameters("\"Snapshot=t7.inf\"")
			.addParameters("\"Mode=HTML\"")
			.addParameters("EXTRARES")
			.addParameters("\"URL=https://server.test.com\"")
			.addParameters("ENDITEM")
			.addParameters("LAST")
			.build();
	
	public static final MethodCall WEB_URL_VERY_SIMPLE_TEST = ImmutableMethodCall.builder()
			.name("\"test_web_url\"")
			.addParameters("\"test_web_url_very_simple\"")
			.addParameters("\"URL=https://server.test.com/\"")
			.addParameters("\"Resource=0\"")
			.addParameters("\"RecContentType=text/xml\"")
			.addParameters("\"Referer=\"")
			.addParameters("\"Snapshot=t7.inf\"")
			.addParameters("\"Mode=HTML\"")
			.addParameters("LAST")
			.build();
	
	public static final Server SERVER_TEST = ImmutableServer.builder()
            .name("server.test.com")
            .host("server.test.com")
            .port("443")
            .scheme("https")
            .build();
	
	public static final GetRequest REQUEST_TEST = ImmutableGetRequest.builder()
            .name("/")
            .path("/")
            .server(SERVER_TEST)
            .httpMethod(HttpMethod.GET)
            .build();

	public static final Page PAGE_TEST = ImmutablePage.builder()
			.addChilds(REQUEST_TEST)
			.thinkTime(0)
			.name("test_web_url_very_simple")
			.build();

	@Test
	public void toElementTest() {		
		final ImmutablePage pageGenerated = (ImmutablePage) WebUrl.toElement(LOAD_RUNNER_VISITOR, WEB_URL_VERY_SIMPLE_TEST);

		final GetRequest request = ImmutableGetRequest.builder()
				.name(pageGenerated.getChilds().get(0).getName())
				.path("/")
				.server(SERVER_TEST)
				.httpMethod(HttpMethod.GET)
				.build();
		final Page expectedPage = ImmutablePage.builder()
				.addChilds(request)
				.thinkTime(0)
				.name("test_web_url_very_simple")
				.build();

		assertEquals(expectedPage, pageGenerated);
	}

	@Test
	public void toElementWithResourceTest() {
		final ImmutablePage pageGenerated = (ImmutablePage) WebUrl.toElement(LOAD_RUNNER_VISITOR, WEB_URL_SIMPLE_TEST);

		final GetRequest resource = ImmutableGetRequest.builder()
				.name(pageGenerated.getChilds().get(1).getName())
				.path("")
				.server(SERVER_TEST)
				.httpMethod(HttpMethod.GET)
				.build();

		final Page expectedPage = ImmutablePage.builder()
				.name("test_web_url_simple")
				.thinkTime(0)
				.addChilds(ImmutableGetRequest.builder().from(REQUEST_TEST).name(pageGenerated.getChilds().get(0).getName()).build(), resource)
				.build();

		assertEquals(expectedPage, pageGenerated);
	}

	@Test
	public void toElementWithRelativeResourceTest() {
		final ImmutablePage pageGenerated = (ImmutablePage) WebUrl.toElement(LOAD_RUNNER_VISITOR, WEB_URL_FULL_TEST);

		// https://server.test.com/test/path?testArgNoValue&testArgWithValue=value"
		ImmutableGetRequest mainRequest = ImmutableGetRequest.builder()
				.httpMethod(HttpMethod.GET)
				.name(pageGenerated.getChilds().get(0).getName())
				.path("/test/path")
				.server(SERVER_TEST)
				.addParameters(ImmutableParameter.builder().name("testArgNoValue").value(Optional.empty()).build(), ImmutableParameter.builder().name("testArgWithValue").value("value").build())
				.build();

		ImmutableGetRequest resource = ImmutableGetRequest.builder()
				.path("/path/resource/1")
				.httpMethod(HttpMethod.GET)
				.server(SERVER_TEST)
				.name(pageGenerated.getChilds().get(1).getName())
				.addParameters(ImmutableParameter.builder().name("ArgWithValue2").value("value 2").build())
				.build();

		ImmutableGetRequest resource2 = ImmutableGetRequest.builder()
				.path("/resource_path_2")
				.httpMethod(HttpMethod.GET)
				.server(ImmutableServer.builder().name("external.server.test.com").host("external.server.test.com").scheme("https").port("443").build())
				.name(pageGenerated.getChilds().get(2).getName())
				.addParameters(ImmutableParameter.builder().name("OnlyOneArg").value(Optional.empty()).build())
				.build();
		Page expectedPage = ImmutablePage.builder()
				.addChilds(mainRequest, resource, resource2)
				.thinkTime(0)
				.name("test_web_url_full")
				.build();

		assertEquals(expectedPage, pageGenerated);
	}
}
