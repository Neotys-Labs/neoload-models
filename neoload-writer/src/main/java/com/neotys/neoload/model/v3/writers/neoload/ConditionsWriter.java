package com.neotys.neoload.model.v3.writers.neoload;

import com.neotys.neoload.model.v3.project.userpath.Condition;
import com.neotys.neoload.model.v3.project.userpath.Match;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

public class ConditionsWriter {

	private static final String XML_CONDITIONS_TAG_NAME = "conditions";
	private static final String XML_ATTR_MATCH_TYPE = "match-type";
	
	private ConditionsWriter() {}
	
	public static void writeXML(final Document document, final Element currentElement, final List<Condition> conditions, final Match match) {
		final Element conditionsElement = document.createElement(XML_CONDITIONS_TAG_NAME);
		conditionsElement.setAttribute(XML_ATTR_MATCH_TYPE, matchValue(match));
		for(final Condition condition: conditions){
			ConditionWriter.writeXML(document, conditionsElement, condition);
		}
		currentElement.appendChild(conditionsElement);
	}

	private static String matchValue(final Match match) {
		if(match == Match.ANY) return "1";
		return "0";
	}
}
