package com.neotys.neoload.model.v3.writers.neoload.variable;

import com.neotys.neoload.model.v3.project.variable.SecretVaultVariable;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SecretVaultVariableWriter extends ElementWriter {

	public static final String XML_TAG_NAME = "variable-keyvault";
	public static final String XML_ATTR_PROVIDER_ID = "providerId";
	public static final String XML_ATTR_SECRET_PATH = "secretPath";

	public SecretVaultVariableWriter(SecretVaultVariable variable) {
		super(variable);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		final Element xmlVariable = document.createElement(XML_TAG_NAME);
		final SecretVaultVariable theVariable = (SecretVaultVariable) element;
		xmlVariable.setAttribute(VariableWriterUtils.XML_ATTR_NAME, element.getName());
		xmlVariable.setAttribute(XML_ATTR_PROVIDER_ID, theVariable.getProviderId());
		xmlVariable.setAttribute(XML_ATTR_SECRET_PATH, theVariable.getSecretId());
		writeDescription(document, xmlVariable);
		currentElement.appendChild(xmlVariable);
	}
}
