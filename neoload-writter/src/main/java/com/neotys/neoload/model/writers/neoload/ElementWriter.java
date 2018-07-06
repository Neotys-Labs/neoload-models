package com.neotys.neoload.model.writers.neoload;

import com.google.common.hash.Hashing;
import com.neotys.neoload.model.core.Element;

import org.w3c.dom.Document;

import java.nio.charset.StandardCharsets;

public class ElementWriter {

    public static final String XML_NAME_ATTR = "name";
    public static final String XML_DESCRIPTION_TAG = "description";
    public static final String XML_UID_TAG = "uid";
    
    protected final Element element;
    
    public ElementWriter(Element element) {
        this.element = element;
    }

    public void writeDescription(final Document document, final org.w3c.dom.Element currentElement) {
        this.element.getDescription().ifPresent(s -> {
            org.w3c.dom.Element descElement = document.createElement(ElementWriter.XML_DESCRIPTION_TAG);
            descElement.setNodeValue(s);
            currentElement.appendChild(descElement);
        });
    }

   /**
    * 
    * @param document
    * @param currentElement
    * @param parentPath
    * @param outputFolder NeoLoad output folder may be use for further computation in overriding classes.
    */
    protected void writeXML(final Document document, final org.w3c.dom.Element currentElement, final String parentPath, final String outputFolder) {
        currentElement.setAttribute(XML_NAME_ATTR, element.getName());
        currentElement.setAttribute(XML_UID_TAG, Hashing.sha256().hashString(parentPath+"/"+element.getName(), StandardCharsets.UTF_8).toString());
        writeDescription(document, currentElement);
    }

}
