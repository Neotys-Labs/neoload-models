package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.TryCatch;
import com.neotys.neoload.model.v3.project.userpath.TryCatch.Policy;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TryCatchWriter extends ElementWriter {
	public final static String XML_TAG_NAME = "try-action";

	public final static String XML_PARAM_ERROR = "catch-errors";
	public final static String XML_PARAM_ASSERTION = "catch-assertions";
	public static final String TRY_CONTAINER = "try-container";
	public static final String CATCH_CONTAINER = "catch-container";

	public TryCatchWriter(final TryCatch tryCatch) {
		super(tryCatch);
	}

	public static TryCatchWriter of(final TryCatch tryCatch) {
		return new TryCatchWriter(tryCatch);
	}

	@Override
	public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
		final Element tryCatchElement = document.createElement(XML_TAG_NAME);
		super.writeXML(document, tryCatchElement, outputFolder);
		currentElement.appendChild(tryCatchElement);
		final Policy policy = ((TryCatch) this.element).getPolicy().orElse(Policy.CATCH_ALL);
		currentElement.setAttribute(XML_PARAM_ERROR,String.valueOf(policy == Policy.CATCH_ALL || policy == Policy.CATCH_ERRORS));
		currentElement.setAttribute(XML_PARAM_ASSERTION,String.valueOf(policy == Policy.CATCH_ALL || policy == Policy.CATCH_ASSERTIONS));
		final TryCatch aTryCatchCatch = ((TryCatch) this.element);
		ContainerWriter.of(aTryCatchCatch.getTry(), TRY_CONTAINER).writeXML(document, tryCatchElement, outputFolder);
		ContainerWriter.of(aTryCatchCatch.getCatch(), CATCH_CONTAINER).writeXML(document, tryCatchElement, outputFolder);



	}

}
