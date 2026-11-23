package com.neotys.neoload.model.v3.writers.neoload.settings;

import com.neotys.neoload.model.v3.project.settings.QTestSettings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Writes the qTest settings of the project into the {@code qTestSettings} element of settings.xml.
 * The attributes that are not set in the model keep the default value of the template.
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
		final Element element = ProjectSettingsWriter.getOrAppendChild(document, settingsElement, XML_TAG_NAME);
		qtest.getEnabled().ifPresent(enabled -> element.setAttribute(XML_ATTR_ENABLED, enabled.toString()));
		qtest.getUrl().ifPresent(url -> element.setAttribute(XML_ATTR_URL, url));
		qtest.getToken().ifPresent(token -> element.setAttribute(XML_ATTR_TOKEN, token));
		qtest.getUsername().ifPresent(username -> element.setAttribute(XML_ATTR_USERNAME, username));
		qtest.getPassword().ifPresent(password -> element.setAttribute(XML_ATTR_PASSWORD, password));
		qtest.getAuthMethod().ifPresent(authMethod -> element.setAttribute(XML_ATTR_AUTH_METHOD, authMethod.name()));
		qtest.getProjectId().ifPresent(projectId -> element.setAttribute(XML_ATTR_PROJECT_ID, projectId.toString()));
		qtest.getCycleId().ifPresent(cycleId -> element.setAttribute(XML_ATTR_CYCLE_ID, cycleId.toString()));
	}
}
