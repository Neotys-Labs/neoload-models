package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.If;
import com.neotys.neoload.model.v3.writers.neoload.ConditionsWriter;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class IfWriter extends ElementWriter {

	private static final String XML_TAG_NAME = "if-action";	
    
	public IfWriter(final If ifThenElse) {
		super(ifThenElse);	
	}

	public static IfWriter of(final If ifThenElse) {
		return new IfWriter(ifThenElse);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		final Element ifElement = document.createElement(XML_TAG_NAME);
		super.writeXML(document, ifElement, outputFolder);
		currentElement.appendChild(ifElement);		
		final If ifThenElse = ((If) this.element);
		ContainerWriter.of(ifThenElse.getThen(), "then-container").writeXML(document, ifElement, outputFolder);
		ifThenElse.getElse().ifPresent(elseContainer -> ContainerWriter.of(elseContainer, "else-container").writeXML(document, ifElement, outputFolder));
		ConditionsWriter.writeXML(document, ifElement, ifThenElse.getConditions(), ifThenElse.getMatch());
	}
}
