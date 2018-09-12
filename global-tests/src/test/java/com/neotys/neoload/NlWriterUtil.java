package com.neotys.neoload;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.writers.neoload.ContainerWriter;
import com.neotys.neoload.model.writers.neoload.WriterUtils;

public class NlWriterUtil {
	
	public static String write(final Container model) throws Exception {
		return write(model, getTmpFile());
	}

	public static String write(final Container model, final String outputfolder) throws Exception {

		final DocumentBuilderFactory repositoryDocFactory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder repositoryDocBuilder;
		repositoryDocBuilder = repositoryDocFactory.newDocumentBuilder();
		final Document document = repositoryDocBuilder.newDocument();
		final Element element = document.createElement("root");
		document.appendChild(element);
		ContainerWriter.of(model, "action-container").writeXML(document, element, outputfolder);
		final TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.VERSION, "1.1");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		final DOMSource repositorySource = new DOMSource(document);
		final File result = File.createTempFile(LrToNlTest.class.toString(), "");
		StreamResult repositoryStream = new StreamResult(result);
		transformer.transform(repositorySource, repositoryStream);
		final String fullXML = new String(Files.readAllBytes(Paths.get(result.getAbsolutePath()))).replaceAll("[\\r\\n]", "");		
		if(fullXML.contains("</action-container>")){
			return fullXML.substring(fullXML.indexOf("</action-container>") + "</action-container>".length(), fullXML.indexOf("</root>"));
		}
		return fullXML.substring(fullXML.indexOf("weightsEnabled=\"false\"/>") + "weightsEnabled=\"false\"/>".length(), fullXML.indexOf("</root>"));			
	}
	
	public static String getTmpFile() {
		try {
			final File temp = File.createTempFile(LrToNlTest.class.toString(), "");
			temp.deleteOnExit();
			return temp.getAbsolutePath();
		} catch (Exception e) {
		}
		return null;
	}
		
	public static String getExpectedJSXml(final com.neotys.neoload.model.core.Element element, final String actualXml, final String expectedJSName) {
		final String uid = WriterUtils.getElementUid(element);
		return "<js-action filename=\"scripts/jsAction_" + uid + ".js\" name=\"" + expectedJSName + "\" ts=\"" + NlWriterUtil.getTimestamp(actualXml) + "\" uid=\"" + uid + "\"/>";
	}
	
	public static String readFile(final String file) throws IOException {
		return com.google.common.io.Files.asCharSource(new File(file), Charset.defaultCharset()).read();
	}
	
	private static String getTimestamp(final String xml){
		return xml.substring(xml.indexOf("ts=") + 4, xml.indexOf("ts=") + 17);
	}	
}
