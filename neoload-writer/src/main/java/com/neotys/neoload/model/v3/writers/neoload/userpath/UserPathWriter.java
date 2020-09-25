package com.neotys.neoload.model.v3.writers.neoload.userpath;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.assertion.Assertion;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import com.neotys.neoload.model.v3.writers.neoload.userpath.assertion.AssertionsWriter;

public class UserPathWriter extends ElementWriter {

    public static final String XML_TAG_NAME = "virtual-user";
    public static final String INIT_CONTAINER_TAG_NAME = "init-container";
    public static final String ACTIONS_CONTAINER_TAG_NAME = "actions-container";
    public static final String END_CONTAINER_TAG_NAME = "end-container";
    public static final String XML_USERPATH_NAME_ATTR = "uid";

    private UserPath userPath;

    public UserPathWriter(UserPath userPath) {
        super(userPath);
        this.userPath = userPath;
    }

    public static UserPathWriter of(UserPath userPath) {
        return new UserPathWriter(userPath);
    }

    @Override
    public void writeXML(final Document document, final Element repositoryElement, final String outputFolder) {
        Element element = document.createElement(XML_TAG_NAME);
        element.setAttribute(XML_USERPATH_NAME_ATTR, this.userPath.getName());
        super.writeDescription(document, element);
        repositoryElement.appendChild(element);
        // write init-container
        ContainerWriter.of(this.userPath.getInit().orElse(Container.builder().build()), INIT_CONTAINER_TAG_NAME).writeXML(document, element, outputFolder);

        // write actions-container
        ContainerWriter.of(this.userPath.getActions(), ACTIONS_CONTAINER_TAG_NAME).writeXML(document, element, outputFolder);

        // write end-container
        ContainerWriter.of(this.userPath.getEnd().orElse(Container.builder().build()), END_CONTAINER_TAG_NAME).writeXML(document, element, outputFolder);
        
        // write assertions
        final List<Assertion> assertions = this.userPath.getAssertions();
        if ((assertions != null && (!assertions.isEmpty()))) {
        	AssertionsWriter.of(assertions).writeXML(document, element);	
        }        
    }
    
}
