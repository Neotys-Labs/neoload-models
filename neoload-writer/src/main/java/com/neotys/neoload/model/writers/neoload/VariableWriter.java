package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.repository.FileVariable;
import com.neotys.neoload.model.repository.Variable;
import org.w3c.dom.Document;


public abstract class VariableWriter {

	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_ORDER = "order";
    public static final String XML_ATTR_POLICY = "policy";
    public static final String XML_ATTR_RANGE = "range";
    public static final String XML_ATTR_WHEN_OUT_OF_VALUE = "whenOutOfValues";

    protected Variable variable;

    protected VariableWriter(Variable variable) {
    	this.variable = variable;
	}

	protected int getPolicyCode(Variable.VariablePolicy pol) {
		switch (pol) {
			case EACH_USE : return 1;
			case EACH_REQUEST : return 2;
			case EACH_PAGE : return 3;
			case EACH_VUSER : return 4;
			case EACH_ITERATION : return 5;
			default : return 1;
		}
	}
	
	protected int getScopeCode(Variable.VariableScope scope) {
		switch (scope) {
			case UNIQUE : return 4;
			case GLOBAL : return 1;
			case LOCAL : return 2;
			default : return 1;
		}
	}
	
	protected String getWhenOutOfValuesCode(Variable.VariableNoValuesLeftBehavior noValBehalf) {
		switch (noValBehalf) {
			case CYCLE : return "CYCLE_VALUES";
			case STOP : return "STOP_TEST";
			case NO_VALUE : return "DEFAULT_VALUE";
			default : return "CYCLE_VALUES";
		}
	}

    public abstract void writeXML(final Document document, final org.w3c.dom.Element currentElement, final String outputFolder);

	public void writeXML(final org.w3c.dom.Element currentElement) {
    	currentElement.setAttribute(XML_ATTR_NAME, variable.getName());
		variable.getOrder().ifPresent(variableOrder -> currentElement.setAttribute(XML_ATTR_ORDER, Integer.toString(variableOrder == FileVariable.VariableOrder.SEQUENTIAL ? 1 : 2)));
		currentElement.setAttribute(XML_ATTR_POLICY, Integer.toString(getPolicyCode(variable.getPolicy())));
		currentElement.setAttribute(XML_ATTR_RANGE, Integer.toString(getScopeCode(variable.getScope())));
		variable.getNoValuesLeftBehavior().ifPresent(variableNoValuesLeftBehavior ->currentElement.setAttribute(XML_ATTR_WHEN_OUT_OF_VALUE, getWhenOutOfValuesCode(variableNoValuesLeftBehavior)));

	}
}
