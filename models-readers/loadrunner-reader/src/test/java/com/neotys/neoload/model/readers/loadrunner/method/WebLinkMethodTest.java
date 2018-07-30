package com.neotys.neoload.model.readers.loadrunner.method;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.method.WebLinkMethod;
import com.neotys.neoload.model.readers.loadrunner.method.WebUrlMethod;
import com.neotys.neoload.model.repository.GetRequest;
import com.neotys.neoload.model.repository.ImmutableGetFollowLinkRequest;
import com.neotys.neoload.model.repository.ImmutableGetPlainRequest;
import com.neotys.neoload.model.repository.ImmutablePage;
import com.neotys.neoload.model.repository.ImmutableServer;
import com.neotys.neoload.model.repository.Page;
import com.neotys.neoload.model.repository.Request.HttpMethod;
import com.neotys.neoload.model.repository.Server;

public class WebLinkMethodTest {
	
	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");
	private static final LoadRunnerVUVisitor LOAD_RUNNER_VISITOR = new LoadRunnerVUVisitor(LOAD_RUNNER_READER, "{", "}", "");
	private static final MethodcallContext METHOD_CALL_CONTEXT = new MethodcallContext(null, 0);
	
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
		
		final ImmutablePage actualWebUrl = (ImmutablePage) (new WebUrlMethod()).getElement(LOAD_RUNNER_VISITOR, webUrlMethod, METHOD_CALL_CONTEXT);
		
		final MethodCall webLinkMethod = ImmutableMethodCall.builder()
				.name("\"webLinkMethod\"")
				.addParameters("\"Form\"")
				.addParameters("\"Text=Form\"")
				.addParameters("\"Snapshot=t19.inf\"")
				.addParameters("LAST")
				.build();
		
		final ImmutablePage actualWebLink = (ImmutablePage) (new WebLinkMethod()).getElement(LOAD_RUNNER_VISITOR, webLinkMethod, METHOD_CALL_CONTEXT);
		
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
				.build();
		
		assertEquals(expectedPage2, actualWebLink);
	}	
}
