package com.neotys.neoload.model.readers.loadrunner.method;

import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_VISITOR;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.METHOD_CALL_CONTEXT;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.repository.GetRequest;
import com.neotys.neoload.model.repository.ImmutableGetFollowLinkRequest;
import com.neotys.neoload.model.repository.ImmutableGetPlainRequest;
import com.neotys.neoload.model.repository.ImmutablePage;
import com.neotys.neoload.model.repository.ImmutableServer;
import com.neotys.neoload.model.repository.Page;
import com.neotys.neoload.model.repository.Request.HttpMethod;
import com.neotys.neoload.model.repository.Server;
@SuppressWarnings("squid:S2699")
public class WebLinkMethodTest {
	
	@Test
	public void testGetElement() {		
		final MethodCall webUrlMethod = ImmutableMethodCall.builder()
				.name("\"webUrlMethod\"")
				.addParameters("\"loadtest\"")
				.addParameters("\"URL=http://jack:9090/loadtest/\"")
				.addParameters("\"Resource=0\"")
				.addParameters("\"RecContentType=text/htm\"")
				.addParameters("\"Referer=\"")
				.addParameters("\"Snapshot=t18.inf\"")
				.addParameters("\"Mode=HTML\"")
				.addParameters("LAST")
				.build();
				
		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		LOAD_RUNNER_VISITOR.getCurrentHeaders().clear();
		LOAD_RUNNER_VISITOR.getGlobalHeaders().clear();
		
		final ImmutablePage actualWebUrl = (ImmutablePage) (new WeburlMethod()).getElement(LOAD_RUNNER_VISITOR, webUrlMethod, METHOD_CALL_CONTEXT).get(0);
		
		final MethodCall webLinkMethod = ImmutableMethodCall.builder()
				.name("\"webLinkMethod\"")
				.addParameters("\"Form\"")
				.addParameters("\"Text=Form\"")
				.addParameters("\"Snapshot=t19.inf\"")
				.addParameters("LAST")
				.build();
		
		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		LOAD_RUNNER_VISITOR.getCurrentHeaders().clear();
		LOAD_RUNNER_VISITOR.getGlobalHeaders().clear();
		
		final ImmutablePage actualWebLink = (ImmutablePage) (new WeblinkMethod()).getElement(LOAD_RUNNER_VISITOR, webLinkMethod, METHOD_CALL_CONTEXT).get(0);
		
		final Server expectedServer = ImmutableServer.builder()
	            .name("jack")
	            .host("jack")
	            .port("9090")
	            .scheme("http")
	            .build();
		
		final GetRequest expectedRequest1 = ImmutableGetPlainRequest.builder()
				.name(actualWebUrl.getChilds().get(0).getName())
				.path("/loadtest/")
				.server(expectedServer)
				.httpMethod(HttpMethod.GET)
				.build();
		final Page expectedPage1 = ImmutablePage.builder()
				.addChilds(expectedRequest1)
				.thinkTime(0)
				.name("loadtest")
				.isDynamic(true)
				.build();

		assertEquals(expectedPage1, actualWebUrl);
		
		final GetRequest expectedRequest2 = ImmutableGetFollowLinkRequest.builder()
				.name(actualWebLink.getChilds().get(0).getName())				
				.server(expectedServer)
				.text("Form")
				.referer(expectedRequest1)
				.httpMethod(HttpMethod.GET)
				.build();
		final Page expectedPage2 = ImmutablePage.builder()
				.addChilds(expectedRequest2)
				.thinkTime(0)
				.name("Form")
				.isDynamic(true)
				.build();
		
		assertEquals(expectedPage2, actualWebLink);
	}	
}
