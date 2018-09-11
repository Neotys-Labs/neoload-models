package com.neotys.neoload;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Charsets;
import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.writers.neoload.ContainerWriter;
import com.neotys.neoload.model.writers.neoload.WriterUtils;

public class LrToNlTest {

	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "");

	@Test
	public void test_lr_think_time() throws Exception{
		final Container model = read("lr_think_time(1);");
		final String actualXml = write(model);
		final String uid = WriterUtils.getElementUid(model.getChilds().get(0));
		final String expectedXml = "<delay-action duration=\"1000\" isThinkTime=\"true\" name=\"delay\" uid=\"" + uid + "\"/>"; 
		Assert.assertEquals(expectedXml, actualXml);
	}

	private static Container read(final String content) {
		try {
			return LOAD_RUNNER_READER.parseCppFile("{", "}", getInputStream(content), "MyContainer", Charsets.UTF_8);
		} catch (final Exception e) {
		}
		return null;
	}

	private static String write(final Container model) throws Exception {

		final DocumentBuilderFactory repositoryDocFactory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder repositoryDocBuilder;
		repositoryDocBuilder = repositoryDocFactory.newDocumentBuilder();
		final Document document = repositoryDocBuilder.newDocument();
		final Element element = document.createElement("root");
		document.appendChild(element);
		ContainerWriter.of(model, "action-container").writeXML(document, element, getTmpFile());
		final TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.VERSION, "1.1");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		final DOMSource repositorySource = new DOMSource(document);
		final File result = File.createTempFile(LrToNlTest.class.toString(), "");
		StreamResult repositoryStream = new StreamResult(result);
		transformer.transform(repositorySource, repositoryStream);
		final String fullXML = new String(Files.readAllBytes(Paths.get(result.getAbsolutePath())));
		return fullXML.substring(fullXML.indexOf("</action-container>") + "</action-container>".length() + 2, fullXML.indexOf("</root>") -2);

	}

	private static InputStream getInputStream(final String content) throws Exception {
		final File temp = File.createTempFile(LrToNlTest.class.toString(), "");
		temp.deleteOnExit();
		final BufferedWriter out = new BufferedWriter(new FileWriter(temp));
		out.write("Action(){" + content + "}");
		out.close();
		return new FileInputStream(temp);

	}

	private static String getTmpFile() {
		try {
			final File temp = File.createTempFile(LrToNlTest.class.toString(), "");
			temp.deleteOnExit();
			return temp.getAbsolutePath();
		} catch (Exception e) {
		}
		return null;
	}

}
