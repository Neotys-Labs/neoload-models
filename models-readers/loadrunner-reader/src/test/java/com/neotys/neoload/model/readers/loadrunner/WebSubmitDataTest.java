package com.neotys.neoload.model.readers.loadrunner;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.repository.ImmutablePage;
import com.neotys.neoload.model.repository.ImmutableParameter;
import com.neotys.neoload.model.repository.ImmutablePostFormRequest;
import com.neotys.neoload.model.repository.ImmutableServer;
import com.neotys.neoload.model.repository.ImmutableTextValidator;
import com.neotys.neoload.model.repository.ImmutableVariableExtractor;
import com.neotys.neoload.model.repository.Page;
import com.neotys.neoload.model.repository.Parameter;
import com.neotys.neoload.model.repository.PostRequest;
import com.neotys.neoload.model.repository.Request;
import com.neotys.neoload.model.repository.Request.HttpMethod;
import com.neotys.neoload.model.repository.Server;
import com.neotys.neoload.model.repository.Validator;
import com.neotys.neoload.model.repository.VariableExtractor;

public class WebSubmitDataTest {
	
	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");
	private static final LoadRunnerVUVisitor LOAD_RUNNER_VISITOR = new LoadRunnerVUVisitor(LOAD_RUNNER_READER, "{", "}", "");
		
	public static final MethodCall WEB_SUBMIT_DATA_TEST = ImmutableMethodCall.builder()
			.name("\"test_web_submit_data\"")
			.addParameters("\"test_web_submit_data\"")
			.addParameters("\"Action=https://server.test.com/test/path?ArgWithValue2=value%204\"")
			.addParameters("\"Method=POST\"")
			.addParameters("\"RecContentType=application/json\"")
			.addParameters("\"Referer=referer_test\"")
			.addParameters("\"Mode=HTML\"")
			.addParameters("ITEMDATA")
			.addParameters("\"Name=param1\"")
			.addParameters("\"Value=value1\"")
			.addParameters("ENDITEM")
			.addParameters("\"Name=param 2\"")
			.addParameters("\"Value=Value 2\"")
			.addParameters("ENDITEM")
			.addParameters("\"Name=Param{WithVariable}End\"")
			.addParameters("\"Value=Value{MyVar}End\"")
			.addParameters("ENDITEM")
			.addParameters("LAST")
			.build();
	
	public static final Server SERVER_TEST = ImmutableServer.builder()
            .name("server.test.com")
            .host("server.test.com")
            .port("443")
            .scheme("https")
            .build();
	
	public static final Parameter PARAM_TEST_1 = ImmutableParameter.builder()
			.name("param1")
			.value("value1")
			.build();

	public static final Parameter PARAM_TEST_2 = ImmutableParameter.builder()
			.name("param 2")
			.value("Value 2")
			.build();

	public static final Parameter PARAM_TEST_3 = ImmutableParameter.builder()
			.name("ArgWithValue2")
			.value("value 4")
			.build();
	public static final Parameter PARAM_TEST_4 = ImmutableParameter.builder()
			.name("Param${WithVariable}End")
			.value("Value${MyVar}End")
			.build();
	
	public static final PostRequest REQUEST_TEST = ImmutablePostFormRequest.builder()
            .name("/test/path")
            .path("/test/path")
            .server(SERVER_TEST)
            .httpMethod(HttpMethod.POST)
            .addPostParameters(PARAM_TEST_1)
            .addPostParameters(PARAM_TEST_2)
			.addPostParameters(PARAM_TEST_4)
            .addParameters(PARAM_TEST_3)
            .build();

	public static final Page PAGE_TEST = ImmutablePage.builder()
			.addChilds(REQUEST_TEST)
			.thinkTime(0)
			.name("test_web_submit_data")
			.isDynamic(true)
			.build();
	
	@Test
	public void buildPostParamsFromExtractTest() {
		final List<Parameter> expectedResult = new ArrayList<>();
		expectedResult.add(PARAM_TEST_1);
		expectedResult.add(PARAM_TEST_2);
		List<String> input = new ArrayList<>();
		input.add("Name=param1");
		input.add("Value=value1");
		input.add("ENDITEM");
		input.add("Name=param 2");
		input.add("Value=Value 2");
		input.add("ENDITEM");
		final List<Parameter> generatedResult = WebRequest.buildPostParamsFromExtract(input);
		
		assertEquals(expectedResult, generatedResult);
	}

	@Test
	public void toElementTest() {
		final ImmutablePage pageGenerated = (ImmutablePage) WebSubmitData.toElement(LOAD_RUNNER_VISITOR, WEB_SUBMIT_DATA_TEST);

		final Page expectedPage = ImmutablePage.builder()
				.addChilds(ImmutablePostFormRequest.builder().from(REQUEST_TEST).name(pageGenerated.getChilds().get(0).getName()).build())
				.thinkTime(0)
				.name("test_web_submit_data")
				.isDynamic(true)
				.build();
		
		assertEquals(expectedPage, pageGenerated);
	}

	@Test
	public void toElementTestWithEmptyExtractorsAndValidators() {
		final ImmutablePage pageGenerated = (ImmutablePage) WebSubmitData.toElement(LOAD_RUNNER_VISITOR, WEB_SUBMIT_DATA_TEST);

		final Page expectedPage = ImmutablePage.builder()
				.addChilds(ImmutablePostFormRequest.builder().from(REQUEST_TEST).name(pageGenerated.getChilds().get(0).getName()).build())
				.thinkTime(0)
				.name("test_web_submit_data")
				.isDynamic(true)
				.build();

		assertEquals(expectedPage, pageGenerated);
	}

	@Test
	public void toElementTestWithExtractorsAndValidators() {		
		final List<VariableExtractor> variableExtractors = ImmutableList.of(ImmutableVariableExtractor.builder()
				.name("myName")
				.startExpression("")
				.endExpression("")
				.exitOnError(false)
				.extractType(VariableExtractor.ExtractType.BODY)
				.build());
		final List<Validator> validators = ImmutableList.of(ImmutableTextValidator.builder()
				.name("myValidator")
				.validationText("")
				.haveToContains(true)
				.build());
		
		LOAD_RUNNER_VISITOR.getCurrentExtractors().addAll(variableExtractors);
		LOAD_RUNNER_VISITOR.getCurrentValidators().addAll(validators);
		
		ImmutablePage pageGenerated = (ImmutablePage) WebSubmitData.toElement(LOAD_RUNNER_VISITOR, WEB_SUBMIT_DATA_TEST);

		final Request requestGenerated = (Request) pageGenerated.getChilds().get(0);
		pageGenerated = pageGenerated.withChilds(ImmutableList.of(requestGenerated));

		assertEquals(variableExtractors, ((Request) pageGenerated.getChilds().get(0)).getExtractors());
		assertEquals(validators, ((Request) pageGenerated.getChilds().get(0)).getValidators());
		LOAD_RUNNER_VISITOR.getCurrentExtractors().clear();
		LOAD_RUNNER_VISITOR.getCurrentValidators().clear();
	}
}
