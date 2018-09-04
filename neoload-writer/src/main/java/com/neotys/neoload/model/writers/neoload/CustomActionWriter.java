package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.CustomAction;

public class CustomActionWriter extends ElementWriter {

	public static final String XML_TAG_NAME = "custom-action";
	public static final String XML_ACTION_TYPE_ATT = "actionType";
	public static final String XML_IS_HIT_ATT = "isHit";

	public CustomActionWriter(final CustomAction customAction) {
		super(customAction);
	}

	public static CustomActionWriter of(final CustomAction customAction) {
		return new CustomActionWriter(customAction);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		final Element customAction = document.createElement(XML_TAG_NAME);
		super.writeXML(document, customAction, outputFolder);
		customAction.setAttribute(XML_ACTION_TYPE_ATT, ((CustomAction) element).getType());
		customAction.setAttribute(XML_IS_HIT_ATT, String.valueOf(((CustomAction) element).isHit()));
		((CustomAction) element).getParameters().forEach(p -> CustomActionParameterWriter.writeXML(document, customAction, p));		
		currentElement.appendChild(customAction);
	}
}
