package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.Stop;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StopWriter extends ElementWriter {

	public static final String XML_TAG_NAME = "stopvu-action";
	public static final String XML_START_NEW_VU_ATT = "startNewVu";

	public StopWriter(Stop stop) {
		super(stop);
	}

	public static StopWriter of(final Stop stop) {
		return new StopWriter(stop);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		Element xmlStop = document.createElement(XML_TAG_NAME);
		super.writeXML(document, xmlStop, outputFolder);
		xmlStop.setAttribute(XML_START_NEW_VU_ATT, String.valueOf(((Stop) element).startNewVirtualUser()));
		currentElement.appendChild(xmlStop);
	}
}
