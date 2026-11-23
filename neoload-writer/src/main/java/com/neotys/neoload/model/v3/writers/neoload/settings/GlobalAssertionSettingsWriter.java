package com.neotys.neoload.model.v3.writers.neoload.settings;

import com.neotys.neoload.model.v3.project.settings.ContentAssertionsSettings;
import java.util.Arrays;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Writes the {@code runtime: responses: content_assertions} settings of the project into the
 * {@code globalAssertionSettings} element of settings.xml: one {@code contentType} element per content type.
 * The settings that are not set in the model keep the default value of the template.
 */
public final class GlobalAssertionSettingsWriter {

	public static final String XML_TAG_NAME = "globalAssertionSettings";
	public static final String XML_ATTR_APPLY_ON_CUSTOM_ACTIONS = "applyGlobalAssertionsOnCA";
	public static final String XML_CONTENT_TYPE_TAG_NAME = "contentType";
	public static final String XML_CONTENT_TYPE_ATTR_VALUE = "value";
	private static final String CONTENT_TYPES_SEPARATOR = ",";
	private static final String LINE_SEPARATOR = "\n";

	private GlobalAssertionSettingsWriter() {
	}

	public static void writeXML(final Document document, final Element settingsElement, final ContentAssertionsSettings contentAssertions) {
		final Element element = ProjectSettingsWriter.getOrAppendChild(document, settingsElement, XML_TAG_NAME);
		contentAssertions.getApplyOnCustomActions()
				.ifPresent(apply -> element.setAttribute(XML_ATTR_APPLY_ON_CUSTOM_ACTIONS, apply.toString()));
		contentAssertions.getContentTypes().ifPresent(contentTypes -> writeContentTypes(document, element, contentTypes));
	}

	private static void writeContentTypes(final Document document, final Element element, final String contentTypes) {
		// The content types replace the default ones of the template; an empty value clears the list
		while (element.hasChildNodes()) {
			element.removeChild(element.getFirstChild());
		}
		element.appendChild(document.createTextNode(LINE_SEPARATOR));
		Arrays.stream(contentTypes.split(CONTENT_TYPES_SEPARATOR))
				.map(String::trim)
				.filter(contentType -> !contentType.isEmpty())
				.forEach(contentType -> {
					final Element contentTypeElement = document.createElement(XML_CONTENT_TYPE_TAG_NAME);
					contentTypeElement.setAttribute(XML_CONTENT_TYPE_ATTR_VALUE, contentType);
					element.appendChild(contentTypeElement);
					element.appendChild(document.createTextNode(LINE_SEPARATOR));
				});
	}
}
