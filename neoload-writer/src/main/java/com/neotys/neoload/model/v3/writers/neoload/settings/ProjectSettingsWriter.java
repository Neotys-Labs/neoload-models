package com.neotys.neoload.model.v3.writers.neoload.settings;

import com.neotys.neoload.model.v3.project.settings.ContentAssertionsSettings;
import com.neotys.neoload.model.v3.project.settings.DynatraceSettings;
import com.neotys.neoload.model.v3.project.settings.ProjectSettings;
import com.neotys.neoload.model.v3.project.settings.QTestSettings;
import com.neotys.neoload.model.v3.project.settings.ResponsesSettings;
import com.neotys.neoload.model.v3.project.settings.RuntimeSettings;
import com.neotys.neoload.model.v3.writers.neoload.NeoLoadWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Writes the settings.xml file of the NeoLoad project.
 *
 * <p>The settings.xml template holds the DTD and the fixed settings only: nothing in it is overwritten.
 * {@link DynatraceSettingsWriter}, {@link QTestSettingsWriter} and {@link GlobalAssertionSettingsWriter} write the
 * settings of the as-code project, with the defaults declared in the as-code model for the absent ones. The DTD header
 * is copied as is: a DOM document does not write back an internal DTD.</p>
 */
public class ProjectSettingsWriter {

	static final String LINE_SEPARATOR = "\n";
	private static final String SETTINGS_XML_FILE = NeoLoadWriter.ConfigFiles.SETTINGS.getFileName();
	private static final String XML_TAG_NAME = "settings";
	/** The children of the settings element, in the order of the settings.xml DTD. */
	private static final List<String> CHILDREN_ORDER = Arrays.asList("configuration", "clientCertificatesSettings",
			"pageNamingSettings", "jsPolicySettings", "errorHandleSettings", GlobalAssertionSettingsWriter.XML_TAG_NAME,
			"caApmSettings", DynatraceSettingsWriter.XML_TAG_NAME, "appDynamicsSettings", "nudgeSettings",
			QTestSettingsWriter.XML_TAG_NAME, "amfSettings", "ofSettings", "performanceSettings", "statisticsSettings",
			"wsSecuritySettings", "media-streaming-settings", "plugin-identification-settings", "team-server-settings",
			"restApisSettings", "exportNeoSenseSettings");

	private ProjectSettingsWriter() {
	}

	public static void writeSettingsXML(final String nlProjectFolder, final Optional<ProjectSettings> projectSettings) throws IOException {
		final String template = readTemplate();
		final int bodyStart = template.indexOf("<" + XML_TAG_NAME + ">");
		final String header = template.substring(0, bodyStart);
		final Document document = parse(template.substring(bodyStart));
		final Element settingsElement = document.getDocumentElement();

		// The elements are always written: an absent setting gets the default of the as-code model
		final ProjectSettings settings = projectSettings.orElseGet(() -> ProjectSettings.builder().build());
		GlobalAssertionSettingsWriter.writeXML(document, settingsElement,
				settings.getRuntime().flatMap(RuntimeSettings::getResponses).flatMap(ResponsesSettings::getContentAssertions)
						.orElseGet(() -> ContentAssertionsSettings.builder().build()));
		DynatraceSettingsWriter.writeXML(document, settingsElement, settings.getDynatrace().orElseGet(() -> DynatraceSettings.builder().build()));
		QTestSettingsWriter.writeXML(document, settingsElement, settings.getQtest().orElseGet(() -> QTestSettings.builder().build()));

		write(new File(nlProjectFolder, SETTINGS_XML_FILE), header, document);
	}

	/**
	 * Inserts {@code element} at its place in the settings element, as the DTD orders the children, on its own line.
	 */
	static void insertInOrder(final Document document, final Element settingsElement, final Element element) {
		final int rank = CHILDREN_ORDER.indexOf(element.getTagName());
		for (Node child = settingsElement.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() == Node.ELEMENT_NODE && CHILDREN_ORDER.indexOf(child.getNodeName()) > rank) {
				settingsElement.insertBefore(element, child);
				settingsElement.insertBefore(document.createTextNode(LINE_SEPARATOR), child);
				return;
			}
		}
		settingsElement.appendChild(element);
		settingsElement.appendChild(document.createTextNode(LINE_SEPARATOR));
	}

	private static String readTemplate() throws IOException {
		try (InputStream in = ProjectSettingsWriter.class.getResourceAsStream(SETTINGS_XML_FILE)) {
			if (in == null) {
				throw new IOException("Missing " + SETTINGS_XML_FILE + " template");
			}
			final ByteArrayOutputStream content = new ByteArrayOutputStream();
			in.transferTo(content);
			// The parsed body is written with LF line endings: the header uses the same ones
			return content.toString(StandardCharsets.UTF_8.name()).replace("\r\n", "\n");
		}
	}

	private static Document parse(final String body) throws IOException {
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			return factory.newDocumentBuilder().parse(new InputSource(new StringReader(body)));
		} catch (final ParserConfigurationException | SAXException e) {
			throw new IOException("Cannot read the " + SETTINGS_XML_FILE + " template", e);
		}
	}

	private static void write(final File targetFile, final String header, final Document document) throws IOException {
		if (!targetFile.createNewFile()) {
			throw new IOException("Cannot create " + SETTINGS_XML_FILE + " file");
		}
		try (OutputStream out = Files.newOutputStream(targetFile.toPath())) {
			out.write(header.getBytes(StandardCharsets.UTF_8));
			// No ACCESS_EXTERNAL_* attributes: the older Xalan of the readers' classpath rejects them, and the
			// identity transform of an in-memory document reads no external resource
			final Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
			transformer.transform(new DOMSource(document), new StreamResult(out));
		} catch (final TransformerException e) {
			throw new IOException("Cannot write " + SETTINGS_XML_FILE, e);
		}
	}
}
