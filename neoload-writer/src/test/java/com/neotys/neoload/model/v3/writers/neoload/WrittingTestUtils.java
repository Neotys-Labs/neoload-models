package com.neotys.neoload.model.v3.writers.neoload;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.userpath.*;
import com.neotys.neoload.model.v3.project.variable.FileVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import com.neotys.neoload.model.v3.util.Parameter;
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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class WrittingTestUtils {
	private WrittingTestUtils() {}
	
	public static final Server SERVER_TEST = Server.builder()
            .name("server_test")
            .host("host_test.com")
            .port("8080")
            .scheme(Server.Scheme.HTTP)
            .build();
	
	public static final Request REQUEST_TEST = Request.builder()
            .name("request_test")
			.url("/test_path")
			.server(SERVER_TEST.getName())
            .method("GET")
            .build();
	
	public static final Parameter PARAMETER_TEST = Parameter.builder()
			.name("param_name")
			.value("param_value")
			.build();
	
	public static final Parameter PARAMETER_TEST_EMPTY_VALUE = Parameter.builder()
			.name("param_name")
			.value("")
			.build();
	
	public static final Parameter PARAMETER_TEST_NO_VALUE = Parameter.builder()
			.name("param_name")
			.build();
	
	public static final Parameter POST_PARAMETER_TEST = Parameter.builder()
			.name("post param_name")
			.value("post_param Value")
			.build(); 
	
	public static final Request REQUEST_TEST2 = Request.builder()
            .name("request_test")
            .url("/test_path?param_name=param_value")
            .server(SERVER_TEST.getName())
            .method("GET")
            .build();
	
	public static final Request REQUEST_TEST3 = Request.builder()
            .name("request_test")
			.url("/test_path?param_name=param_value")
            .server(SERVER_TEST.getName())
            .method("POST")
            .body("post param_name=post_param Value")
			.addHeaders(Header.builder().name("Content-Type").value("application/x-www-form-urlencoded").build())
            .build();

	public static final Request REQUEST_TEST3_PUT_METHOD = ImmutableRequest.copyOf(WrittingTestUtils.REQUEST_TEST3).withMethod("PUT");

	public static final Request REQUEST_TEST4 = Request.builder()
            .name("request_test")
            .url("/test_path?param_name=param_value")
            .server(SERVER_TEST.getName())
            .method("POST")
            .body("texte a convertir en binaire")
			.addHeaders(Header.builder().name("Content-Type").value("text/plain").build())
            .build();

	public static final Request REQUEST_TEST4_PUT_METHOD = ImmutableRequest.copyOf(WrittingTestUtils.REQUEST_TEST4).withMethod("PUT");


	public static final byte[] BINARY_DATA_TEST = {116, 101, 120, 116, 101, 32, 97, 32, 99, 111, 110, 118, 101, 114, 116, 105, 114, 32, 101, 110, 32, 98, 105, 110, 97, 105, 114, 101};
	
	public static final Request REQUEST_TEST5 = Request.builder()
            .name("request_test")
			.url("/test_path?param_name=param_value")
            .server(SERVER_TEST.getName())
            .method("POST")
			// TODO !!! should be natively binary in the model
            .body(new String(BINARY_DATA_TEST))
			.addHeaders(Header.builder().name("Content-Type").value("application/octet-stream").build())
            .build();

	// TODO page does not exist in V3
	/*
	public static final Page PAGE_TEST = new Page.builder()
			.addChilds(WrittingTestUtils.REQUEST_TEST)
			.thinkTime(0)
			.name("page_name")
			.isDynamic(false)
			.build();*/
	
	public static final Server SERVER_JACK9090_TEST = Server.builder()
            .name("jack")
            .host("jack")
            .port("9090")
			// TODO why Scheme is an enum
            .scheme(Server.Scheme.HTTP)
            .build();
	
	public static final Request GET_REQUEST_TEST = Request.builder()
			.name("GET_REQUEST_TEST")
			.url("/loadtest/")
			.server(SERVER_JACK9090_TEST.getName())
			.method("GET")
			.build();

	public static final Request GET_REQUEST_WITH_RECORDED_FILES;

	static {
		final Request.Builder builder = Request.builder().name("GET_REQUEST_TEST")
				.url("/loadtest/")
				.server(SERVER_JACK9090_TEST.getName())
				.method("GET");
		// TODO no recorded files in V3
		/*try {

			builder.recordedFiles(ImmutableRecordedFiles.builder()
					.recordedRequestBodyFile(new File(WrittingTestUtils.class.getResource("requestBody.txt").toURI()).getAbsolutePath())
					.recordedRequestHeaderFile(new File(WrittingTestUtils.class.getResource("requestHeader.txt").toURI()).getAbsolutePath())
					.recordedResponseBodyFile(new File(WrittingTestUtils.class.getResource("responseBody.html").toURI()).getAbsolutePath())
					.recordedResponseHeaderFile(new File(WrittingTestUtils.class.getResource("responseHeader.txt").toURI()).getAbsolutePath())
					.build());

		} catch (final URISyntaxException e) {
			e.printStackTrace();
		}*/
		GET_REQUEST_WITH_RECORDED_FILES = builder.build();
	}

	// TODO
	/*public static final Page PAGE_GET_REQUEST_TEST = ImmutablePage.builder()
			.addChilds(GET_REQUEST_TEST)
			.thinkTime(0)
			.name("PAGE_GET_REQUEST_TEST")
			.isDynamic(false)
			.build();*/

	// TODO
	/*public static final GetFollowLinkRequest GET_FOLLOW_LINK_REQUEST_TEST = ImmutableGetFollowLinkRequest.builder()
			.name("GET_FOLLOW_LINK_REQUEST_TEST")				
			.server(SERVER_JACK9090_TEST)
			.text("Form")
			.referer(GET_REQUEST_TEST)
			.httpMethod(HttpMethod.GET)
			.build();*/

	// TODO
	/*
	public static final Page PAGE_GET_FOLLOW_LINK_REQUEST_TEST = ImmutablePage.builder()
			.addChilds(GET_FOLLOW_LINK_REQUEST_TEST)
			.thinkTime(0)
			.name("Form")
			.isDynamic(false)
			.build();*/

	public static final Request POST_SUBMIT_FORM_REQUEST_TEST = Request.builder()
			.name("POST_SUBMIT_FORM_REQUEST_TEST")
			.server(SERVER_JACK9090_TEST.getName())
			.method("POST")
			.body("firstname=a&lastname=b&email=c@d.fr&address=e&sex=Male")
			.addHeaders(Header.builder().name("Content-Type").value("application/x-www-form-urlencoded").build())
			// TDOD
			//.referer(GET_FOLLOW_LINK_REQUEST_TEST)
			.build();

	// TODO
	/*
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
			.build();*/
	
	public static final Container CONTAINER_TEST = Container.builder()
			// TODO
			//.addSteps(PAGE_TEST)
			.addSteps(REQUEST_TEST)
			.name("Container_name")
			.build();

	public static final Container SHARED_CONTAINER_TEST = Container.builder()
			// TODO
			//.addSteps(PAGE_TEST)
			.name("Shared_Container_name")
			// TODO
			//.isShared(true)
			.build();

	/*public static final Container CONTAINER_WITH_SHARED_CHILD_TEST = new ImmutableContainer.Builder()
			.addChilds(SHARED_CONTAINER_TEST)
			.name("Container_with_shared_child")
			.build();*/


	public static final CustomAction SET_OK_CODE_CUSTOM_ACTION = CustomAction.builder()
			.name("setOkCode")
			.type("SetText")
			.asRequest(false)
			.parameters(ImmutableList.of(CustomActionParameter.builder()
					.name("objectId")
					.value("x")
					.type(CustomActionParameter.Type.TEXT)
					.build()))
			.build();
	
	public static final CustomAction IS_OBJECT_AVAILABLE_CUSTOM_ACTION = CustomAction.builder()
			.name("isObjectAvailable")
			.type("IsAvailable")
			.asRequest(false)
			.parameters(ImmutableList.of(CustomActionParameter.builder()
					.name("objectId")
					.value("x")
					.type(CustomActionParameter.Type.TEXT)
					.build()))
			.build();
	
	public static final If IF_THEN_TEST_2 = If.builder()
			.name("condition")
			.addConditions(Condition.builder()
							.operand1("${sapgui_is_object_available_2}")
							.operator(Condition.Operator.EQUALS)
							.operand2("true")
							.build())
			.match(If.Match.ANY)
			.then(Container.builder()
					.name("Then")
					.addSteps(SET_OK_CODE_CUSTOM_ACTION)
					.build())
			.getElse(Container.builder()
					.name("Else")
					.build())
			.build();
	
	public static final If IF_THEN_TEST_1 = If.builder()
			.name("condition")
			.addConditions(Condition.builder()
						.operand1("${sapgui_is_object_available_1}")
						.operator(Condition.Operator.EQUALS)
						.operand2("true")
						.build())
			.match(If.Match.ANY)
			.then(Container.builder()
					.name("Then")
					.addSteps(SET_OK_CODE_CUSTOM_ACTION)
					.build())
			.getElse(Container.builder()
					.name("Else")
					.addSteps(SET_OK_CODE_CUSTOM_ACTION, IS_OBJECT_AVAILABLE_CUSTOM_ACTION, IF_THEN_TEST_2)
					.build())
			.build();
	
	private static final List<String> COLUMNS;
	static {
		COLUMNS = new ArrayList<>();
		COLUMNS.add("colonneTest");
	}
	
	public static final FileVariable VARIABLE_TEST = FileVariable.builder()
			.name("variable_test")
			.delimiter(",")
			.path("path_du_fichier")
			.startFromLine(2)
			.order(Variable.Order.SEQUENTIAL)
			.changePolicy(Variable.ChangePolicy.EACH_ITERATION)
			.isFirstLineColumnNames(true)
			.scope(Variable.Scope.GLOBAL)
			.columnNames(COLUMNS)
			.outOfValue(Variable.OutOfValue.CYCLE)
			.build();
	
	public static final FileVariable VARIABLE_TEST2 = FileVariable.builder()
			.name("variable_test")
			.delimiter(",")
			.path("path_du_fichier")
			.startFromLine(2)
			.order(Variable.Order.RANDOM)
			.changePolicy(Variable.ChangePolicy.EACH_USE)
			.isFirstLineColumnNames(true)
			.scope(Variable.Scope.LOCAL)
			.columnNames(COLUMNS)
			.outOfValue(Variable.OutOfValue.STOP)
			.build();
	
	public static final FileVariable VARIABLE_TEST3 = FileVariable.builder()
			.name("variable_test")
			.delimiter(",")
			.path("path_du_fichier")
			.startFromLine(2)
			.order(Variable.Order.SEQUENTIAL)
			.changePolicy(Variable.ChangePolicy.EACH_USER)
			.isFirstLineColumnNames(true)
			.scope(Variable.Scope.UNIQUE)
			.columnNames(COLUMNS)
			.outOfValue(Variable.OutOfValue.CYCLE)
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
