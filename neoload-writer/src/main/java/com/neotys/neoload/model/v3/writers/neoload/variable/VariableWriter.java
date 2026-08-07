package com.neotys.neoload.model.v3.writers.neoload.variable;

import com.neotys.neoload.model.v3.project.variable.Variable;
import com.neotys.neoload.model.v3.project.variable.ChangePolicyElement;
import com.neotys.neoload.model.v3.project.variable.ScopeElement;
import com.neotys.neoload.model.v3.project.variable.OrderElement;
import com.neotys.neoload.model.v3.project.variable.OutOfValueElement;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;


public abstract class VariableWriter extends ElementWriter {

	public static final String XML_ATTR_NAME = "name";
	public static final String XML_ATTR_ORDER = "order";
    public static final String XML_ATTR_POLICY = "policy";
    public static final String XML_ATTR_RANGE = "range";
    public static final String XML_ATTR_WHEN_OUT_OF_VALUE = "whenOutOfValues";

    protected VariableWriter(Variable variable) {
    	super(variable);
	}

	protected int getPolicyCode(ChangePolicyElement.ChangePolicy pol) {
		switch (pol) {
			case EACH_USE : return 1;
			case EACH_REQUEST : return 2;
			case EACH_PAGE : return 3;
			case EACH_USER : return 4;
			case EACH_ITERATION : return 5;
			default : return 1;
		}
	}

	protected int getScopeCode(ScopeElement.Scope scope) {
		switch (scope) {
			case UNIQUE : return 4;
			case GLOBAL : return 1;
			case LOCAL : return 2;
			default : return 1;
		}
	}

	protected String getWhenOutOfValuesCode(OutOfValueElement.OutOfValue outOfValue) {
		switch (outOfValue) {
			case CYCLE : return "CYCLE_VALUES";
			case STOP : return "STOP_TEST";
			case NO_VALUE : return "DEFAULT_VALUE";
			default : return "CYCLE_VALUES";
		}
	}

	public void writeXML(final org.w3c.dom.Element currentElement) {
		currentElement.setAttribute(XML_ATTR_NAME, element.getName());
		if (element instanceof ChangePolicyElement) {
			currentElement.setAttribute(XML_ATTR_POLICY, Integer.toString(getPolicyCode(((ChangePolicyElement) element).getChangePolicy())));
		}
		if (element instanceof ScopeElement) {
			currentElement.setAttribute(XML_ATTR_RANGE, Integer.toString(getScopeCode(((ScopeElement) element).getScope())));
		}
		if (element instanceof OrderElement) {
			currentElement.setAttribute(XML_ATTR_ORDER, Integer.toString(((OrderElement) element).getOrder() == OrderElement.Order.SEQUENTIAL ? 1 : 2));
		}
		if (element instanceof OutOfValueElement) {
			currentElement.setAttribute(XML_ATTR_WHEN_OUT_OF_VALUE, getWhenOutOfValuesCode(((OutOfValueElement) element).getOutOfValue()));
		}
	}
}
