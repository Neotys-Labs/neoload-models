package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.Project;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ProjectWriter {

    public static final String XML_TAG_NAME = "repository";
    
    private final Project project;

    public ProjectWriter(Project project) {
        this.project = project;
    }

    public static ProjectWriter of(Project project) {
        return new ProjectWriter(project);
    }

    public void writeXML(final Document document, final String outputFolder) {
        final Element repositoryElement = document.createElement(XML_TAG_NAME);
        document.appendChild(repositoryElement);
        project.getUserPaths().forEach(userPath -> UserPathWriter.of(userPath).writeXML(document, repositoryElement, userPath.getName(), outputFolder));
        project.getServers().forEach(serv -> ServerWriter.of(serv).writeXML(document, repositoryElement));
        project.getVariables().forEach(var -> WriterUtils.getWriterFor(var).writeXML(document, repositoryElement, outputFolder));
    }
}
