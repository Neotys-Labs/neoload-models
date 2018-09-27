package com.neotys.neoload.model.writers.neoload;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.repository.*;
import com.neotys.neoload.model.repository.CustomActionParameter.Type;
import com.neotys.neoload.model.repository.Request.HttpMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class WrittingTestUtils {
	private WrittingTestUtils() {}
	
	public static final Server SERVER_TEST = ImmutableServer.builder()
            .name("server_test")
            .host("host_test.com")
            .port("8080")
            .scheme("http")
            .build();
	
	public static final ImmutableGetPlainRequest REQUEST_TEST = ImmutableGetPlainRequest.builder()
            .name("request_test")
            .path("/test_path")
            .server(SERVER_TEST)
            .httpMethod(HttpMethod.GET)
            .build();
	
	public static final Parameter PARAMETER_TEST = ImmutableParameter.builder()
			.name("param_name")
			.value("param_value")
			.build();
	
	public static final Parameter PARAMETER_TEST_EMPTY_VALUE = ImmutableParameter.builder()
			.name("param_name")
			.value("")
			.build();
	
	public static final Parameter PARAMETER_TEST_NO_VALUE = ImmutableParameter.builder()
			.name("param_name")
			.build();
	
	public static final Parameter POST_PARAMETER_TEST = ImmutableParameter.builder()
			.name("post param_name")
			.value("post_param Value")
			.build(); 
	
	public static final GetPlainRequest REQUEST_TEST2 = ImmutableGetPlainRequest.builder()
            .name("request_test")
            .path("/test_path")
            .server(SERVER_TEST)
            .httpMethod(HttpMethod.GET)
            .addParameters(PARAMETER_TEST)
            .build();
	
	public static final PostFormRequest REQUEST_TEST3 = ImmutablePostFormRequest.builder()
            .name("request_test")
            .path("/test_path")
            .server(SERVER_TEST)
            .httpMethod(HttpMethod.POST)
            .addParameters(PARAMETER_TEST)
            .addPostParameters(POST_PARAMETER_TEST)
            .build();
	
	public static final PostTextRequest REQUEST_TEST4 = ImmutablePostTextRequest.builder()
            .name("request_test")
            .path("/test_path")
            .server(SERVER_TEST)
            .httpMethod(HttpMethod.POST)
            .addParameters(PARAMETER_TEST)
            .data("texte a convertir en binaire")
            .build();

	public static final byte[] BINARY_DATA_TEST = {116, 101, 120, 116, 101, 32, 97, 32, 99, 111, 110, 118, 101, 114, 116, 105, 114, 32, 101, 110, 32, 98, 105, 110, 97, 105, 114, 101};
	
	public static final PostRequest REQUEST_TEST5 = ImmutablePostBinaryRequest.builder()
            .name("request_test")
            .path("/test_path")
            .server(SERVER_TEST)
            .httpMethod(HttpMethod.POST)
            .addParameters(PARAMETER_TEST)
            .binaryData(BINARY_DATA_TEST)
            .build();
	
	public static final Page PAGE_TEST = ImmutablePage.builder()
			.addChilds(WrittingTestUtils.REQUEST_TEST)
			.thinkTime(0)
			.name("page_name")
			.isDynamic(false)
			.build();
	
	public static final Server SERVER_JACK9090_TEST = ImmutableServer.builder()
            .name("jack")
            .host("jack")
            .port("9090")
            .scheme("http")
            .build();
	
	public static final GetPlainRequest GET_REQUEST_TEST = ImmutableGetPlainRequest.builder()
			.name("GET_REQUEST_TEST")
			.path("/loadtest/")
			.server(SERVER_JACK9090_TEST)
			.httpMethod(HttpMethod.GET)
			.build();

	public static final GetPlainRequest GET_REQUEST_WITH_RECORDED_FILES;

	static {
		final ImmutableGetPlainRequest.Builder builder = ImmutableGetPlainRequest.builder().name("GET_REQUEST_TEST")
				.path("/loadtest/")
				.server(SERVER_JACK9090_TEST)
				.httpMethod(HttpMethod.GET);
		try {
			builder.recordedFiles(ImmutableRecordedFiles.builder()
					.recordedRequestBodyFile(new File(WrittingTestUtils.class.getResource("requestBody.txt").toURI()).getAbsolutePath())
					.recordedRequestHeaderFile(new File(WrittingTestUtils.class.getResource("requestHeader.txt").toURI()).getAbsolutePath())
					.recordedResponseBodyFile(new File(WrittingTestUtils.class.getResource("responseBody.html").toURI()).getAbsolutePath())
					.recordedResponseHeaderFile(new File(WrittingTestUtils.class.getResource("responseHeader.txt").toURI()).getAbsolutePath())
					.build());

		} catch (final URISyntaxException e) {
			e.printStackTrace();
		}
		GET_REQUEST_WITH_RECORDED_FILES = builder.build();
	}

	public static final Page PAGE_GET_REQUEST_TEST = ImmutablePage.builder()
			.addChilds(GET_REQUEST_TEST)
			.thinkTime(0)
			.name("PAGE_GET_REQUEST_TEST")
			.isDynamic(false)
			.build();
	
	public static final GetFollowLinkRequest GET_FOLLOW_LINK_REQUEST_TEST = ImmutableGetFollowLinkRequest.builder()
			.name("GET_FOLLOW_LINK_REQUEST_TEST")				
			.server(SERVER_JACK9090_TEST)
			.text("Form")
			.referer(GET_REQUEST_TEST)
			.httpMethod(HttpMethod.GET)
			.build();
	
	public static final Page PAGE_GET_FOLLOW_LINK_REQUEST_TEST = ImmutablePage.builder()
			.addChilds(GET_FOLLOW_LINK_REQUEST_TEST)
			.thinkTime(0)
			.name("Form")
			.isDynamic(false)
			.build();
	
	public static final PostSubmitFormRequest POST_SUBMIT_FORM_REQUEST_TEST = ImmutablePostSubmitFormRequest.builder()
			.name("POST_SUBMIT_FORM_REQUEST_TEST")
			.server(SERVER_JACK9090_TEST)
			.httpMethod(HttpMethod.POST)
			.referer(GET_FOLLOW_LINK_REQUEST_TEST)
			.addPostParameters(ImmutableParameter.builder().name("firstname").value("a").build())
			.addPostParameters(ImmutableParameter.builder().name("lastname").value("b").build())
			.addPostParameters(ImmutableParameter.builder().name("email").value("c@d.fr").build())
			.addPostParameters(ImmutableParameter.builder().name("address").value("e").build())
			.addPostParameters(ImmutableParameter.builder().name("sex").value("Male").build())
			.build();

	public static final PostMultipartRequest POST_MULTIPART_REQUEST_TEST = ImmutablePostMultipartRequest.builder()
			.name("POST_MULTIPART_REQUEST_TEST")
			.server(SERVER_JACK9090_TEST)
			.httpMethod(HttpMethod.POST)
			.addParts(ImmutablePart.builder().name("stringPart").value("partValue").charSet("UTF-8").build())
			.addParts(ImmutablePart.builder().name("filePart").contentType("image/jpg").sourceFilename("filename.jpg").build())
			.addParts(ImmutablePart.builder().name("filePart").contentType("image/jpg").sourceFilename("filename.jpg").filename("sentFileName.jpg").transferEncoding("transfer").build())
			.build();
	
	public static final Page PAGE_GET_SUBMIT_FORM_REQUEST_TEST = ImmutablePage.builder()
			.addChilds(POST_SUBMIT_FORM_REQUEST_TEST)
			.thinkTime(0)
			.name("PAGE_GET_SUBMIT_FORM_REQUEST_TEST")
			.isDynamic(false)
			.build();
	
	public static final Container CONTAINER_TEST = ImmutableContainer.builder()
			.addChilds(PAGE_TEST)
			.name("Container_name")
			.build();
	
	static final CustomAction SET_OK_CODE_CUSTOM_ACTION = ImmutableCustomAction.builder()
			.name("setOkCode")
			.type("SetText")
			.isHit(false)
			.parameters(ImmutableList.of(ImmutableCustomActionParameter.builder()
					.name("objectId")
					.value("x")
					.type(Type.TEXT)
					.build()))
			.build();
	
	static final CustomAction IS_OBJECT_AVAILABLE_CUSTOM_ACTION = ImmutableCustomAction.builder()
			.name("isObjectAvailable")
			.type("IsAvailable")
			.isHit(false)
			.parameters(ImmutableList.of(ImmutableCustomActionParameter.builder()
					.name("objectId")
					.value("x")
					.type(Type.TEXT)
					.build()))
			.build();
	
	public static final IfThenElse IF_THEN_TEST_2 = ImmutableIfThenElse.builder()
			.name("condition")
			.conditions(ImmutableConditions.builder()
					.addConditions(ImmutableCondition.builder()
							.operand1("${sapgui_is_object_available_2}")
							.operator(Condition.Operator.EQUALS)
							.operand2("true")
							.build()
							)
					.matchType(Conditions.MatchType.ANY)
					.build())
			.then(ImmutableContainer.builder()
					.name("Then")
					.addChilds(SET_OK_CODE_CUSTOM_ACTION)
					.build())
			.getElse(ImmutableContainer.builder()
					.name("Else")					
					.build())
			.build();
	
	public static final IfThenElse IF_THEN_TEST_1 = ImmutableIfThenElse.builder()
			.name("condition")
			.conditions(ImmutableConditions.builder()
					.addConditions(ImmutableCondition.builder()
							.operand1("${sapgui_is_object_available_1}")
							.operator(Condition.Operator.EQUALS)
							.operand2("true")
							.build()
							)
					.matchType(Conditions.MatchType.ANY)
					.build())
			.then(ImmutableContainer.builder()
					.name("Then")
					.addChilds(SET_OK_CODE_CUSTOM_ACTION)					
					.build())
			.getElse(ImmutableContainer.builder()
					.name("Else")
					.addChilds(SET_OK_CODE_CUSTOM_ACTION, IS_OBJECT_AVAILABLE_CUSTOM_ACTION, IF_THEN_TEST_2)					
					.build())
			.build();
	

	private static final List<String> COLUMNS;
	static {
		COLUMNS = new ArrayList<>();
		COLUMNS.add("colonneTest");
	}
	
	public static final FileVariable VARIABLE_TEST = ImmutableFileVariable.builder()
			.name("variable_test")
			.columnsDelimiter(",")
			.fileName("path_du_fichier")
			.numOfFirstRowData(2)
			.order(FileVariable.VariableOrder.SEQUENTIAL)
			.policy(FileVariable.VariablePolicy.EACH_ITERATION)
			.firstLineIsColumnName(true)
			.scope(FileVariable.VariableScope.GLOBAL)
			.columnsNames(COLUMNS)
			.noValuesLeftBehavior(FileVariable.VariableNoValuesLeftBehavior.CYCLE)
			.build();
	
	public static final FileVariable VARIABLE_TEST2 = ImmutableFileVariable.builder()
			.name("variable_test")
			.columnsDelimiter(",")
			.fileName("path_du_fichier")
			.numOfFirstRowData(2)
			.order(FileVariable.VariableOrder.RANDOM)
			.policy(FileVariable.VariablePolicy.EACH_USE)
			.firstLineIsColumnName(true)
			.scope(FileVariable.VariableScope.LOCAL)
			.columnsNames(COLUMNS)
			.noValuesLeftBehavior(FileVariable.VariableNoValuesLeftBehavior.STOP)
			.build();
	
	public static final FileVariable VARIABLE_TEST3 = ImmutableFileVariable.builder()
			.name("variable_test")
			.columnsDelimiter(",")
			.fileName("path_du_fichier")
			.numOfFirstRowData(2)
			.order(FileVariable.VariableOrder.SEQUENTIAL)
			.policy(FileVariable.VariablePolicy.EACH_VUSER)
			.firstLineIsColumnName(true)
			.scope(FileVariable.VariableScope.UNIQUE)
			.columnsNames(COLUMNS)
			.noValuesLeftBehavior(FileVariable.VariableNoValuesLeftBehavior.CYCLE)
			.build();
	
	public static Document generateEmptyDocument() throws ParserConfigurationException{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		return docBuilder.newDocument();
	}

	public static Element generateTestRootElement(Document doc) {
		final Element rootElement = doc.createElement("test-root");
        doc.appendChild(rootElement);
		return rootElement;
	}
	
	public static String getXmlString(Document doc) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		
		StringWriter writer = new StringWriter();
	    StreamResult result = new StreamResult(writer);

		transformer.transform(source, result);

		return writer.toString();
	}
}
