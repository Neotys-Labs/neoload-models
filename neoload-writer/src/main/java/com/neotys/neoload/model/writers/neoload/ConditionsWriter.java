package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.Condition;
import com.neotys.neoload.model.repository.Conditions;

public class ConditionsWriter {

	private static final String XML_CONDITIONS_TAG_NAME = "conditions";
	private static final String XML_ATTR_MATCH_TYPE = "match-type";
	
	private ConditionsWriter() {}
	
	public static void writeXML(final Document document, final Element currentElement, final Conditions conditions) {
		final Element conditionsElement = document.createElement(XML_CONDITIONS_TAG_NAME);
		conditionsElement.setAttribute(XML_ATTR_MATCH_TYPE, String.valueOf(conditions.getMatchType().ordinal()));
		for(final Condition condition: conditions.getConditions()){
			ConditionWriter.writeXML(document, conditionsElement, condition);
		}
		currentElement.appendChild(conditionsElement);
	}
}
