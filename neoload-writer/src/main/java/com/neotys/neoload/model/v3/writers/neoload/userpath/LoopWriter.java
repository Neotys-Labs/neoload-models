package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.Loop;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Optional;

public class LoopWriter extends ElementWriter {

    private static final String XML_TAG_NAME = "loop-action";
    private static final String XML_ATTR_LOOP = "loop";

    public LoopWriter(final Loop loop) {
        super(loop);
    }

    public static LoopWriter of(final Loop loop) {
        return new LoopWriter(loop);
    }

    @Override
    public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
        final Element loopElement = document.createElement(XML_TAG_NAME);
        loopElement.setAttribute(XML_ATTR_LOOP, ((Loop) element).getTimes());
        super.writeXML(document, loopElement, outputFolder);
        currentElement.appendChild(loopElement);

        ((Loop) element).getSteps().forEach(
                elt -> {
					WriterUtils.generateEmbeddedAction(document, loopElement, elt, Optional.of(WriterUtils.EMBEDDED_ACTION_XML_TAG_NAME), false);
					WriterUtils.<ElementWriter>getWriterFor(elt).writeXML(document, currentElement, outputFolder);
				}
        );
    }
}
