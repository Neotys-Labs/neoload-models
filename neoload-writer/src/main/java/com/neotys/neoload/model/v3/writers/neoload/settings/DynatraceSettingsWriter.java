package com.neotys.neoload.model.v3.writers.neoload.settings;

import com.neotys.neoload.model.v3.project.settings.DynatraceSettings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Writes the Dynatrace settings of the project into the {@code dynaTraceSettings} element of settings.xml.
 * The attributes that are not set in the model keep the default value of the template.
 */
public final class DynatraceSettingsWriter {

	public static final String XML_TAG_NAME = "dynaTraceSettings";
	public static final String XML_ATTR_ENABLED = "dynaTraceEnabled";
	public static final String XML_ATTR_URL = "url";
	public static final String XML_ATTR_TOKEN = "token";

	private DynatraceSettingsWriter() {
	}

	public static void writeXML(final Document document, final Element settingsElement, final DynatraceSettings dynatrace) {
		final Element element = ProjectSettingsWriter.getOrAppendChild(document, settingsElement, XML_TAG_NAME);
		dynatrace.getEnabled().ifPresent(enabled -> element.setAttribute(XML_ATTR_ENABLED, enabled.toString()));
		dynatrace.getUrl().ifPresent(url -> element.setAttribute(XML_ATTR_URL, url));
		dynatrace.getToken().ifPresent(token -> element.setAttribute(XML_ATTR_TOKEN, token));
	}
}
