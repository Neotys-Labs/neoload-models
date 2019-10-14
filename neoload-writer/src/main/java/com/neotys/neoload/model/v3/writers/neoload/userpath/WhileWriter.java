package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.While;
import com.neotys.neoload.model.v3.writers.neoload.ConditionsWriter;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Optional;

public class WhileWriter extends ElementWriter {

	private static final String XML_TAG_NAME = "while-action";

	public WhileWriter(final While whileDo) {
		super(whileDo);
	}

	public static WhileWriter of(final While whileDo) {
		return new WhileWriter(whileDo);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		final Element whileElement = document.createElement(XML_TAG_NAME);
		super.writeXML(document, whileElement, outputFolder);
		currentElement.appendChild(whileElement);
		final While whileDo = ((While) this.element);

		((While) element).getSteps().forEach(
				elt -> {
					WriterUtils.generateEmbeddedAction(document, whileElement, elt, Optional.of(WriterUtils.EMBEDDED_ACTION_XML_TAG_NAME), false);
					WriterUtils.<ElementWriter>getWriterFor(elt).writeXML(document, currentElement, outputFolder);
				}
		);

		ConditionsWriter.writeXML(document, whileElement, whileDo.getConditions(), whileDo.getMatch());
	}
}
