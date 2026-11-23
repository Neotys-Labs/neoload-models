package com.neotys.neoload.model.v3.writers.neoload.settings;

import com.neotys.neoload.model.v3.project.settings.QTestSettings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Writes the {@code qTestSettings} element of settings.xml from the qTest settings of the project, with the defaults
 * of {@link QTestSettings} for the absent settings.
 */
public final class QTestSettingsWriter {

	public static final String XML_TAG_NAME = "qTestSettings";
	public static final String XML_ATTR_ENABLED = "enable";
	public static final String XML_ATTR_URL = "url";
	public static final String XML_ATTR_TOKEN = "token";
	public static final String XML_ATTR_USERNAME = "username";
	public static final String XML_ATTR_PASSWORD = "password";
	public static final String XML_ATTR_AUTH_METHOD = "authMethod";
	public static final String XML_ATTR_PROJECT_ID = "projectId";
	public static final String XML_ATTR_CYCLE_ID = "contextTreePathIds";

	private QTestSettingsWriter() {
	}

	public static void writeXML(final Document document, final Element settingsElement, final QTestSettings qtest) {
		final Element element = document.createElement(XML_TAG_NAME);
		element.setAttribute(XML_ATTR_AUTH_METHOD, qtest.getAuthMethod().orElse(QTestSettings.DEFAULT_AUTH_METHOD).name());
		element.setAttribute(XML_ATTR_CYCLE_ID, qtest.getCycleId().map(String::valueOf).orElse(""));
		element.setAttribute(XML_ATTR_ENABLED, String.valueOf(qtest.getEnabled().orElse(QTestSettings.DEFAULT_ENABLED)));
		element.setAttribute(XML_ATTR_PASSWORD, qtest.getPassword().orElse(""));
		element.setAttribute(XML_ATTR_PROJECT_ID, qtest.getProjectId().map(String::valueOf).orElse(""));
		element.setAttribute(XML_ATTR_TOKEN, qtest.getToken().orElse(""));
		element.setAttribute(XML_ATTR_URL, qtest.getUrl().orElse(""));
		element.setAttribute(XML_ATTR_USERNAME, qtest.getUsername().orElse(""));
		ProjectSettingsWriter.insertInOrder(document, settingsElement, element);
	}
}
