package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.Project;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ProjectWriter {

    public static final String XML_REPOSITORY_TAG_NAME = "repository";
    public static final String XML_SCENARIOS_TAG_NAME = "scenarios";
    
    private final Project project;

    public ProjectWriter(Project project) {
        this.project = project;
    }

    public static ProjectWriter of(Project project) {
        return new ProjectWriter(project);
    }

    public void writeXML(final Document repositoryDocument, final Document scenarioDocument, final String outputFolder) {
        final Element repositoryElement = repositoryDocument.createElement(XML_REPOSITORY_TAG_NAME);
        repositoryDocument.appendChild(repositoryElement);
        project.getUserPaths().forEach(userPath -> UserPathWriter.of(userPath).writeXML(repositoryDocument, repositoryElement, userPath.getName()));
        project.getServers().forEach(serv -> ServerWriter.of(serv).writeXML(repositoryDocument, repositoryElement));
        project.getVariables().forEach(var -> WriterUtils.<VariableWriter>getWriterFor(var).writeXML(repositoryDocument, repositoryElement, outputFolder));
        project.getPopulations().forEach(pop -> PopulationWriter.of(pop).writeXML(repositoryDocument, repositoryElement));

        final Element scenariosElement = scenarioDocument.createElement(XML_SCENARIOS_TAG_NAME);
        scenarioDocument.appendChild(scenariosElement);
        project.getScenarios().forEach(scenario -> ScenarioWriter.of(scenario).writeXML(scenarioDocument, scenariosElement));

    }
}
