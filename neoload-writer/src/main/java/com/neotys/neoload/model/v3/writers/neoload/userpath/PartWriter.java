package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.Part;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PartWriter extends ElementWriter {

    private static final String XML_TAG_MULTIPART_STRING = "multipart-string";
    private static final String XML_TAG_MULTIPART_FILE = "multipart-file";

    public PartWriter(Part part) {
        super(part);
    }

    public static PartWriter of(Part part) {
        return new PartWriter(part);
    }

    @Override
    public void writeXML(final Document document, final org.w3c.dom.Element currentElement, final String outputFolder) {

        if(((Part)this.element).getSourceFilename().isPresent()) {
            final Element filePartElement = document.createElement(XML_TAG_MULTIPART_FILE);
            writeFilePartElement(filePartElement, ((Part)this.element));
            currentElement.appendChild(filePartElement);
        } else {
            final Element stringPartElement = document.createElement(XML_TAG_MULTIPART_STRING);
            writeStringPartElement(stringPartElement, ((Part)this.element));
            currentElement.appendChild(stringPartElement);
        }
    }

    private static void writeStringPartElement(final Element stringPartElement, final Part part) {
        writeCommonPartElement(stringPartElement, part);
        stringPartElement.setAttribute("value", part.getValue().get());
        stringPartElement.setAttribute("valueMode", "USE_VALUE");
    }

    private static void writeFilePartElement(final Element filePartElement, final Part part) {
        writeCommonPartElement(filePartElement, part);
        filePartElement.setAttribute("attachedFilename", part.getSourceFilename().orElse(""));
        filePartElement.setAttribute("filename", part.getFilename().orElse(part.getSourceFilename().orElse("")));
    }

    private static void writeCommonPartElement(final Element partElement, final Part part) {
        partElement.setAttribute("name",part.getName());
        part.getCharSet().ifPresent(s -> partElement.setAttribute("charSet", s));
        part.getContentType().ifPresent(s -> partElement.setAttribute("contentType", s));
        part.getTransferEncoding().ifPresent(s -> partElement.setAttribute("transferEncoding", s));
    }
}
