package com.neotys.neoload.model.v3.writers.neoload;

import com.neotys.neoload.model.v3.project.Dependency;
import com.neotys.neoload.model.v3.project.DependencyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * The generic dependency writer. It is able to write dependency files such as java or js libraries.
 * In the case of js libraries, the writer also registers them in the project repository.
 */
public class DependencyWriter extends ElementWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyWriter.class);

    private static final String XML_JSLIB_TAG_NAME = "js-library";
    private static final String XML_ATTRIBUTE_FILENAME = "filename";
    private static final String XML_ATTRIBUTE_TS = "ts";

    public DependencyWriter(final Dependency dependency) {
        super(dependency);
    }

    public static DependencyWriter of(final Dependency dependency) {
        return new DependencyWriter(dependency);
    }

    @Override
    public void writeXML(final Document document, final Element currentElement, final String outputFolder) {
        final Dependency dependency = (Dependency) this.element;

        final File dir = new File(outputFolder + File.separator + dependency.getType().getLocationFolderName());
        if (!dir.exists()) {
            dir.mkdir();
        }

        final Path destination = Paths.get(dir.getAbsolutePath() + File.separator + dependency.getFilename());

        try {
            writeFile(dependency, destination);
        } catch (IOException e) {
            LOGGER.error("An error occurred while writing the JS Library file \"" + dependency.getFilename() + " in " + destination + "\":\n" + e);
            return;
        }

        registerLib(dependency, document, currentElement, destination);

    }

    private void registerLib(final Dependency dependency, final Document document, final Element currentElement, final Path destination) {
        // register if nec
        if (dependency.getType().equals(DependencyType.JS_LIBRARY)) {
            final Element xmlJSLib = document.createElement(XML_JSLIB_TAG_NAME);
            super.writeXML(document, xmlJSLib, null);

            xmlJSLib.setAttribute(XML_ATTRIBUTE_FILENAME, destination.toString());
            xmlJSLib.setAttribute(XML_NAME_ATTR, dependency.getName());
            xmlJSLib.setAttribute(XML_ATTRIBUTE_TS, Long.toString(System.currentTimeMillis()));
            currentElement.appendChild(xmlJSLib);
        }
    }

    private void writeFile(final Dependency dependency, final Path destination) throws IOException {
        if (dependency.getInputStream() != null) {
            Files.copy(dependency.getInputStream(), destination, REPLACE_EXISTING);
        } else {
            final Path source = Paths.get(dependency.getFilename());
            Files.copy(source, destination);
        }
    }
}
