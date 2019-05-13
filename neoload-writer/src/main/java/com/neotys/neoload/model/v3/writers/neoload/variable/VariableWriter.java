package com.neotys.neoload.model.v3.writers.neoload.variable;

import com.neotys.neoload.model.v3.project.variable.Variable;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
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

	protected int getPolicyCode(Variable.ChangePolicy pol) {
		switch (pol) {
			case EACH_USE : return 1;
			case EACH_REQUEST : return 2;
			case EACH_PAGE : return 3;
			case EACH_USER : return 4;
			case EACH_ITERATION : return 5;
			default : return 1;
		}
	}
	
	protected int getScopeCode(Variable.Scope scope) {
		switch (scope) {
			case UNIQUE : return 4;
			case GLOBAL : return 1;
			case LOCAL : return 2;
			default : return 1;
		}
	}
	
	protected String getWhenOutOfValuesCode(Variable.OutOfValue outOfValue) {
		switch (outOfValue) {
			case CYCLE : return "CYCLE_VALUES";
			case STOP : return "STOP_TEST";
			case NO_VALUE : return "DEFAULT_VALUE";
			default : return "CYCLE_VALUES";
		}
	}

    public abstract void writeXML(final Document document, final org.w3c.dom.Element currentElement, final String outputFolder);

	public void writeXML(final org.w3c.dom.Element currentElement) {
    	currentElement.setAttribute(XML_ATTR_NAME, variable.getName());
		currentElement.setAttribute(XML_ATTR_ORDER, Integer.toString(variable.getOrder() == Variable.Order.SEQUENTIAL ? 1 : 2));
		currentElement.setAttribute(XML_ATTR_POLICY, Integer.toString(getPolicyCode(variable.getChangePolicy())));
		currentElement.setAttribute(XML_ATTR_RANGE, Integer.toString(getScopeCode(variable.getScope())));
		currentElement.setAttribute(XML_ATTR_WHEN_OUT_OF_VALUE, getWhenOutOfValuesCode(variable.getOutOfValue()));
	}

	// needs a refactor to be put in an upper class or in an util class (duplication from ElementWritter). The original function might have a bug!!!
	public void writeDescription(final Document document, final org.w3c.dom.Element currentElement) {
		this.variable.getDescription().ifPresent(s -> {
			org.w3c.dom.Element descElement = document.createElement(ElementWriter.XML_DESCRIPTION_TAG);
			descElement.setTextContent(s);
			currentElement.appendChild(descElement);
		});
	}
}
