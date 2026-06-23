package com.neotys.neoload.model.v3.writers.neoload.variable;

import com.neotys.neoload.model.v3.project.variable.SecretVaultVariable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SecretVaultVariableWriter extends VariableWriter {

	public static final String XML_TAG_NAME = "variable-keyvault";
	public static final String XML_ATTR_PROVIDER_ID = "providerId";
	public static final String XML_ATTR_SECRET_PATH = "secretPath";

	public SecretVaultVariableWriter(SecretVaultVariable variable) {
		super(variable);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		org.w3c.dom.Element xmlVariable = document.createElement(XML_TAG_NAME);
		super.writeXML(xmlVariable);
		SecretVaultVariable theVariable = (SecretVaultVariable) element;
		xmlVariable.setAttribute(XML_ATTR_PROVIDER_ID, theVariable.getProviderId());
		xmlVariable.setAttribute(XML_ATTR_SECRET_PATH, theVariable.getSecretIdentifier());
		writeDescription(document, xmlVariable);
		currentElement.appendChild(xmlVariable);
	}
}
