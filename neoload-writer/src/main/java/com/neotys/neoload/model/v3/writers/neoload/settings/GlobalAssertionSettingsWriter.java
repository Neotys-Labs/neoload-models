package com.neotys.neoload.model.v3.writers.neoload.settings;

import com.neotys.neoload.model.v3.project.settings.ContentAssertionsSettings;
import java.util.Arrays;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Writes the {@code globalAssertionSettings} element of settings.xml from the {@code runtime: responses:
 * content_assertions} settings of the project, with the defaults of {@link ContentAssertionsSettings} for the absent
 * settings: one {@code contentType} element per content type.
 */
public final class GlobalAssertionSettingsWriter {

	public static final String XML_TAG_NAME = "globalAssertionSettings";
	public static final String XML_ATTR_APPLY_ON_CUSTOM_ACTIONS = "applyGlobalAssertionsOnCA";
	public static final String XML_CONTENT_TYPE_TAG_NAME = "contentType";
	public static final String XML_CONTENT_TYPE_ATTR_VALUE = "value";
	private static final String CONTENT_TYPES_SEPARATOR = ",";

	private GlobalAssertionSettingsWriter() {
	}

	public static void writeXML(final Document document, final Element settingsElement, final ContentAssertionsSettings contentAssertions) {
		final Element element = document.createElement(XML_TAG_NAME);
		element.setAttribute(XML_ATTR_APPLY_ON_CUSTOM_ACTIONS,
				String.valueOf(contentAssertions.getApplyOnCustomActions().orElse(ContentAssertionsSettings.DEFAULT_APPLY_ON_CUSTOM_ACTIONS)));
		element.appendChild(document.createTextNode(ProjectSettingsWriter.LINE_SEPARATOR));
		// An empty value applies the global assertions to no response: no contentType element
		Arrays.stream(contentAssertions.getContentTypes().orElse(ContentAssertionsSettings.DEFAULT_CONTENT_TYPES).split(CONTENT_TYPES_SEPARATOR))
				.map(String::trim)
				.filter(contentType -> !contentType.isEmpty())
				.forEach(contentType -> {
					final Element contentTypeElement = document.createElement(XML_CONTENT_TYPE_TAG_NAME);
					contentTypeElement.setAttribute(XML_CONTENT_TYPE_ATTR_VALUE, contentType);
					element.appendChild(contentTypeElement);
					element.appendChild(document.createTextNode(ProjectSettingsWriter.LINE_SEPARATOR));
				});
		ProjectSettingsWriter.insertInOrder(document, settingsElement, element);
	}
}
