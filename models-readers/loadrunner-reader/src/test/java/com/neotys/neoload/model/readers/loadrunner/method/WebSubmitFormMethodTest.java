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
import com.neotys.neoload.model.repository.ImmutableParameter;
import com.neotys.neoload.model.repository.ImmutablePostSubmitFormRequest;
import com.neotys.neoload.model.repository.ImmutableServer;
import com.neotys.neoload.model.repository.Page;
import com.neotys.neoload.model.repository.PostSubmitFormRequest;
import com.neotys.neoload.model.repository.Request.HttpMethod;
import com.neotys.neoload.model.repository.Server;
@SuppressWarnings("squid:S2699")
public class WebSubmitFormMethodTest {
	
	@Test
	public void testGetElement() {		
		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		LOAD_RUNNER_VISITOR.getCurrentHeaders().clear();
		LOAD_RUNNER_VISITOR.getGlobalHeaders().clear();
		
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
		
		final ImmutablePage actualWebUrl = (ImmutablePage) (new WeburlMethod()).getElement(LOAD_RUNNER_VISITOR, webUrlMethod, METHOD_CALL_CONTEXT).get(0);
		
		final MethodCall webLinkMethod = ImmutableMethodCall.builder()
				.name("\"webLinkMethod\"")
				.addParameters("\"Form\"")
				.addParameters("\"Text=Form\"")
				.addParameters("\"Snapshot=t19.inf\"")
				.addParameters("LAST")
				.build();
		
		(new WeblinkMethod()).getElement(LOAD_RUNNER_VISITOR, webLinkMethod, METHOD_CALL_CONTEXT);
		
		final MethodCall webSubmitFormMethod = ImmutableMethodCall.builder()
				.name("\"webSubmitFormMethod\"")
				.addParameters("\"doit.jsp\"")
				.addParameters("\"Snapshot=t20.inf\"")
				.addParameters("ITEMDATA")
				.addParameters("\"Name=firstname\"")
				.addParameters("\"Value=a\"")
				.addParameters("ENDITEM")			
				.addParameters("\"Name=lastname\"")
				.addParameters("\"Value=b\"")
				.addParameters("ENDITEM")
				.addParameters("\"Name=email\"")
				.addParameters("\"Value=c@d.fr\"")
				.addParameters("ENDITEM")
				.addParameters("\"Name=address\"")
				.addParameters("\"Value=e\"")
				.addParameters("ENDITEM")
				.addParameters("\"Name=sex\"")
				.addParameters("\"Value=Male\"")
				.addParameters("ENDITEM")
				.addParameters("LAST")
				.build();
		
		final ImmutablePage actualWebSubmitForm = (ImmutablePage) (new WebsubmitformMethod()).getElement(LOAD_RUNNER_VISITOR, webSubmitFormMethod, METHOD_CALL_CONTEXT).get(0);
				
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
				
				final GetRequest expectedRequest2 = ImmutableGetFollowLinkRequest.builder()
				.name("Form")				
				.server(expectedServer)
				.text("Form")
				.referer(expectedRequest1)
				.httpMethod(HttpMethod.GET)
				.build();
				
		final PostSubmitFormRequest expectedRequest3 = ImmutablePostSubmitFormRequest.builder()
				.name("doit.jsp")
				.server(expectedServer)
				.httpMethod(HttpMethod.POST)
				.referer(expectedRequest2)
				.addPostParameters(ImmutableParameter.builder().name("firstname").value("a").build())
				.addPostParameters(ImmutableParameter.builder().name("lastname").value("b").build())
				.addPostParameters(ImmutableParameter.builder().name("email").value("c@d.fr").build())
				.addPostParameters(ImmutableParameter.builder().name("address").value("e").build())
				.addPostParameters(ImmutableParameter.builder().name("sex").value("Male").build())
				.build();
		final Page expectedPage = ImmutablePage.builder()
				.addChilds(expectedRequest3)
				.thinkTime(0)
				.name("doit.jsp")
				.isDynamic(true)
				.build();

		assertEquals(expectedPage, actualWebSubmitForm);		
	}	
}
