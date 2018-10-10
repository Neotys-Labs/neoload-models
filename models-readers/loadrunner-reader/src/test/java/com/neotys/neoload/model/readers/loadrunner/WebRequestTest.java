package com.neotys.neoload.model.readers.loadrunner;

import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_READER;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_VISITOR;
import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.repository.GetRequest;
import com.neotys.neoload.model.repository.ImmutableGetPlainRequest;
import com.neotys.neoload.model.repository.ImmutableParameter;
import com.neotys.neoload.model.repository.ImmutableServer;
import com.neotys.neoload.model.repository.Request;
import com.neotys.neoload.model.repository.Request.HttpMethod;
import com.neotys.neoload.model.repository.Server;
@SuppressWarnings("squid:S2699")
public class WebRequestTest {
		
	public static final MethodCall WEB_URL_FULL_TEST = ImmutableMethodCall.builder()
			.name("\"test_web_url\"")
			.addParameters("\"URL=https://server.test.com/test/path?testArgNoValue&testArgWithValue=value\"")
			.addParameters("\"Resource=0\"")
			.addParameters("\"RecContentType=text/xml\"")
			.addParameters("\"Referer=\"")
			.addParameters("\"Mode=HTML\"")
			.addParameters("EXTRARES")
			.addParameters("\"URL=https://server.test.com/path/resource/1?ArgWithValue2=value%202\"")
			.addParameters("ENDITEM")
			.addParameters("\"URL=https://external.server.test.com/resource_path_2?OnlyOneArg\"")
			.addParameters("ENDITEM")
			.addParameters("LAST")
			.build();
	
	public static final MethodCall WEB_SUBMIT_DATA_INVALID_METHOD = ImmutableMethodCall.builder()
			.name("\"test_web_submit_data\"")
			.addParameters("\"Action=https://server.test.com/test/path?testArgNoValue&testArgWithValue=value\"")
			.addParameters("\"Method=INVALID\"")
			.addParameters("\"RecContentType=application/json\"")
			.addParameters("\"Referer=referer_test\"")
			.addParameters("\"Snapshot=tX.inf\"")
			.addParameters("\"Mode=HTML\"")
			.addParameters("EXTRARES")
			.addParameters("\"URL=https://server.test.com/path/resource/1?ArgWithValue2=value%202\"")
			.addParameters("ENDITEM")
			.addParameters("LAST")
			.build();
	
	public static final MethodCall WEB_SUBMIT_DATA_TEST = ImmutableMethodCall.builder()
			.name("\"test_web_submit_data\"")
			.addParameters("\"Action=https://server.test.com/test/path?testArgNoValue&testArgWithValue=value\"")
			.addParameters("\"Method=POST\"")
			.addParameters("\"RecContentType=application/json\"")
			.addParameters("\"Referer=referer_test\"")
			.addParameters("\"Snapshot=tX.inf\"")
			.addParameters("\"Mode=HTML\"")
			.addParameters("ITEMDATA")
			.addParameters("\"Name=itemName\"")
			.addParameters("\"Value=valueName\"")
			.addParameters("ENDITEM")
			.addParameters("\"Name=itemName2\"")
			.addParameters("\"Value=Value2\"")
			.addParameters("ENDITEM")
			.addParameters("EXTRARES")
			.addParameters("\"URL=https://server.test.com/path/resource/1?ArgWithValue2=value%202\"")
			.addParameters("ENDITEM")
			.addParameters("LAST")
			.build();
	
	public static final Server SERVER_TEST = ImmutableServer.builder()
            .name("test_server.com")
            .host("test_server.com")
            .port("8080")
            .scheme("https")
            .build();

	public static final Server SERVER_TEST2 = ImmutableServer.builder()
            .name("test_server.com_1")
            .host("test_server.com")
            .port("80")
            .scheme("https")
            .build();
	
	@Test
	public void buildRequestFromURLTest() throws MalformedURLException {
		final URL urlTest = new URL("https://test_server.com:8080/request/path?param1=value1&param2&param3=value%203");
		
		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		LOAD_RUNNER_VISITOR.getCurrentHeaders().clear();
		LOAD_RUNNER_VISITOR.getGlobalHeaders().clear();
		
		final GetRequest generatedResult = WebRequest.buildGetRequestFromURL(LOAD_RUNNER_VISITOR, urlTest, Optional.empty(), ImmutableList.of());

		// no matter the request name, it is generated randomly
		Request expectedRequestResult = ImmutableGetPlainRequest.builder()
				.server(SERVER_TEST)
				.path("/request/path")
				.name(generatedResult.getName())
				.httpMethod(HttpMethod.GET)
				.addParameters(ImmutableParameter.builder().name("param1").value("value1").build())
				.addParameters(ImmutableParameter.builder().name("param2").build())
				.addParameters(ImmutableParameter.builder().name("param3").value("value 3").build())
				.build();
		
		assertEquals(expectedRequestResult, generatedResult);
	}
	
	@Test
	public void getUrlListTest() throws MalformedURLException {
		List<String> input = new ArrayList<>();
		input.add("\"URL=https://server-test.com/path/1/photo1.png\"");
		input.add("\"Referer=https://www.google.fr/\"");
		input.add("ENDITEM");
		input.add("\"URL=https://server-test.com/path2/photo2.png\"");
		input.add("\"Referer=https://www.google.fr/\"");
		input.add("ENDITEM");
		
		List<URL> generatedResult = WebRequest.getUrlList(input, null);
		
		List<URL> expectedResult = new ArrayList<>();
		expectedResult.add(new URL("https://server-test.com/path/1/photo1.png"));
		expectedResult.add(new URL("https://server-test.com/path2/photo2.png"));
		
		assertEquals(expectedResult, generatedResult);
	}


    @Test
    public void getUrlListWithContextTest() throws MalformedURLException {
        List<String> input = new ArrayList<>();
        input.add("\"URL=https://server-test.com/path/1/photo1.png\"");
        input.add("\"Referer=https://www.google.fr/\"");
        input.add("ENDITEM");
        input.add("\"URL=path2/photo2.png\"");
        input.add("\"Referer=https://www.google.fr/\"");
		input.add("ENDITEM");
		input.add("\"URL=/path3/photo3.png\"");
		input.add("\"Referer=https://www.google.fr/\"");
		input.add("ENDITEM");
		input.add("\"URL=../path4/photo4.png\"");
		input.add("\"Referer=https://www.google.fr/\"");
        input.add("ENDITEM");

        List<URL> generatedResult = WebRequest.getUrlList(input, new URL("https://server-test.com/context/image.jpg"));

        List<URL> expectedResult = new ArrayList<>();
        expectedResult.add(new URL("https://server-test.com/path/1/photo1.png"));
        expectedResult.add(new URL("https://server-test.com/context/path2/photo2.png"));
		expectedResult.add(new URL("https://server-test.com/path3/photo3.png"));
		expectedResult.add(new URL("https://server-test.com/path4/photo4.png"));

        assertEquals(expectedResult, generatedResult);
    }

	@Test
	public void getUrlListWithInvalidURLTest() throws MalformedURLException {
		List<String> input = ImmutableList.of(
				"\"URL=:/this is an invalid URL\"",
				"\"Referer=https://www.google.fr/\"",
				"ENDITEM",
				"\"URL=https://server-test.com/path2/photo2.png\"",
				"\"Referer=https://www.google.fr/\"",
				"ENDITEM"
		);
		Assertions.assertThat(WebRequest.getUrlList(input,null)).isEqualTo(ImmutableList.of(new URL("https://server-test.com/path2/photo2.png")));
	}

	@Test
    public void getServerTest() throws MalformedURLException {
		final URL urlTest = new URL("https://test_server.com:8080/request/path");
		final URL urlTest2 = new URL("https://test_server.com:80/request/path");		
		final Server serverGenerated1 = LOAD_RUNNER_READER.getServer(urlTest);
		final Server serverGenerated2 = LOAD_RUNNER_READER.getServer(urlTest2);
		
		assertEquals(SERVER_TEST, serverGenerated1);
		assertEquals(SERVER_TEST2, serverGenerated2);
		
	}
	
	@Test
    public void getMethodTestDefault() {
		Request.HttpMethod generatedMethod = WebRequest.getMethod("{", "}", WEB_URL_FULL_TEST);
		assertEquals(Request.HttpMethod.GET, generatedMethod);
	}
	
	@Test
    public void getMethodTestPost() {
		Request.HttpMethod generatedMethod = WebRequest.getMethod("{", "}", WEB_SUBMIT_DATA_TEST);
		assertEquals(Request.HttpMethod.POST, generatedMethod);
	}
	
	@Test
    public void getMethodTestInvalid() {
		Request.HttpMethod generatedMethod = WebRequest.getMethod("{", "}", WEB_SUBMIT_DATA_INVALID_METHOD);
		assertEquals(Request.HttpMethod.GET, generatedMethod);
	}
	
	@Test
	public void getUrlFromMethodParametersTestError1(){
		MethodCall input = ImmutableMethodCall.builder().addAllParameters(ImmutableList.of(
				"\"URL=:/this is an invalid URL\"",
				"\"Referer=https://www.google.fr/\"",
				"ENDITEM",
				"\"URL=https://server-test.com/path2/photo2.png\"",
				"\"Referer=https://www.google.fr/\"",
				"ENDITEM"
		)).name("methodname").build();
		assertEquals(null, WebRequest.getUrlFromMethodParameters("{", "}", input, "Url"));
	}
	
	@Test
	public void getUrlFromMethodParametersTestError2(){
		MethodCall input = ImmutableMethodCall.builder().addAllParameters(ImmutableList.of(
				"\"Referer=https://www.google.fr/\"",
				"ENDITEM",
				"\"Referer=https://www.google.fr/\"",
				"ENDITEM"
		)).name("methodname").build();
		assertEquals(null, WebRequest.getUrlFromMethodParameters("{", "}", input, "Url"));
	}

	@Test
	public void getUrlFromParameterString() {
		URL url = WebRequest.getUrlFromParameterString("{", "}", "http://myserver/index.html");
		assertEquals("myserver", url.getHost());
		assertEquals("/index.html", url.getPath());
	}

	@Test
	public void getUrlFromParameterStringTestWithVariable() {
		URL url = WebRequest.getUrlFromParameterString("{", "}", "{myserver}/index.html");
		assertEquals("${myserver}", url.getHost());
		assertEquals("/index.html", url.getPath());
	}

	@Test
	public void getUrlFromParameterStringTestWithVariableCustomBrace() {
		URL url = WebRequest.getUrlFromParameterString("&", "@", "&myserver@/index.html");
		assertEquals("${myserver}", url.getHost());
		assertEquals("/index.html", url.getPath());
	}

	@Test
	public void getUrlFromParameterStringTestInvalidURL() {
		URL url = WebRequest.getUrlFromParameterString("{", "}", "[myserver]/index.html");
		assertEquals(null, url);
	}

	@Test
	public void testExtractPathOfUrl(){
		assertEquals(Optional.empty(), WebRequest.extractPathFromUrl(null));
		assertEquals(Optional.empty(), WebRequest.extractPathFromUrl(""));
		assertEquals(Optional.of("/path"), WebRequest.extractPathFromUrl("http://www.neotys.com/path"));
	}
	
}
