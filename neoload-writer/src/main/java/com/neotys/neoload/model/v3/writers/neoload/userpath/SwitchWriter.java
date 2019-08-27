package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.Case;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Switch;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SwitchWriter extends ElementWriter {

    private static final String XML_TAG_NAME = "switch-container";
    private static final String XML_ATTR_CONDITION = "condition";


    public SwitchWriter(final Switch aSwitch) {
        super(aSwitch);
    }

    public static SwitchWriter of(final Switch aSwitch) {
        return new SwitchWriter(aSwitch);
    }

    @Override
    public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
        final Element switchElement = document.createElement(XML_TAG_NAME);
        switchElement.setAttribute(XML_ATTR_CONDITION, ((Switch) element).getValue());
        super.writeXML(document, switchElement, outputFolder);
        currentElement.appendChild(switchElement);
        final Switch aSwitch = ((Switch) this.element);

        for(Case aCase : aSwitch.getCases()){
            CaseWriter.of(aCase).writeXML(document, switchElement, outputFolder);
            aCase.getSteps().forEach(
                    elt -> WriterUtils.<ElementWriter>getWriterFor(elt).writeXML(document, currentElement, outputFolder)
            );
        }
        if (aSwitch.getDefault() != null) {
            ContainerWriter.of(aSwitch.getDefault(), "default-statement").writeXML(document, switchElement, outputFolder);
        } else {
            ContainerWriter.of((new Container.Builder()).name("Default").build(), "default-statement").writeXML(document, switchElement, outputFolder);
        }
    }
}
