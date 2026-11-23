package com.neotys.neoload.model.v3.writers.neoload.settings;

import com.neotys.neoload.model.v3.project.settings.DynatraceSettings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Writes the {@code dynaTraceSettings} element of settings.xml from the Dynatrace settings of the project, with the
 * defaults of {@link DynatraceSettings} for the absent settings.
 */
public final class DynatraceSettingsWriter {

	public static final String XML_TAG_NAME = "dynaTraceSettings";
	public static final String XML_ATTR_ENABLED = "dynaTraceEnabled";
	public static final String XML_ATTR_LOGICAL_NAMES_ENABLED = "logicalNamesEnabled";
	public static final String XML_ATTR_URL = "url";
	public static final String XML_ATTR_TOKEN = "token";

	private DynatraceSettingsWriter() {
	}

	public static void writeXML(final Document document, final Element settingsElement, final DynatraceSettings dynatrace) {
		final Element element = document.createElement(XML_TAG_NAME);
		element.setAttribute(XML_ATTR_ENABLED, String.valueOf(dynatrace.getEnabled().orElse(DynatraceSettings.DEFAULT_ENABLED)));
		// Not an as-code setting
		element.setAttribute(XML_ATTR_LOGICAL_NAMES_ENABLED, Boolean.FALSE.toString());
		element.setAttribute(XML_ATTR_URL, dynatrace.getUrl().orElse(""));
		element.setAttribute(XML_ATTR_TOKEN, dynatrace.getToken().orElse(""));
		ProjectSettingsWriter.insertInOrder(document, settingsElement, element);
	}
}
