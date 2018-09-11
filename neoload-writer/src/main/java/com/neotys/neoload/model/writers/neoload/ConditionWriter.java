package com.neotys.neoload.model.writers.neoload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.repository.Condition;

public class ConditionWriter {
	
	private static final String XML_CONDITION_TAG_NAME = "condition";
	private static final String XML_ATTR_OPERAND1 = "operand1";
	private static final String XML_ATTR_OPERATOR = "operator";
	private static final String XML_ATTR_OPERAND2 = "operand2";
	
	private ConditionWriter() {}
	
	public static void writeXML(final Document document, final Element currentElement, final Condition condition) {
		final Element conditionElement = document.createElement(XML_CONDITION_TAG_NAME);		
		conditionElement.setAttribute(XML_ATTR_OPERAND1, condition.getOperand1());
		conditionElement.setAttribute(XML_ATTR_OPERATOR, condition.getOperator().toString());
		conditionElement.setAttribute(XML_ATTR_OPERAND2, condition.getOperand2());
		currentElement.appendChild(conditionElement);
	}
}
