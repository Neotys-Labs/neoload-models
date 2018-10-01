package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.Project;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.UUID;

public class ProjectWriter {

    public static final String XML_REPOSITORY_TAG_NAME = "repository";
    public static final String XML_SCENARIOS_TAG_NAME = "scenarios";
    public static final String XML_ZONES_TAG_NAME = "zones";
    public static final String XML_ZONES_ATTR_DEFAULTZONEUID_NAME = "defaultZoneUid";
    public static final String XML_ZONES_ATTR_DNSOVERRIDE_NAME = "dnsOverrideEnabled";
    public static final String XML_ZONES_ATTR_DNSTTL_NAME = "dnsTtl";
    public static final String XML_ATTR_UID_NAME = "uid";
    public static final String XML_ZONE_TAG_NAME = "zone";
    public static final String XML_LGHOST_TAG_NAME = "lg-host";

    public static final String DEFAULT_ZONE_NAME = "Default zone";
    public static final String DEFAULT_LG_NAME = "localhost:7100";
    
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
        project.getSharedElements().forEach(container -> SharedContainerWriter.of(container).writeXML(repositoryDocument, repositoryElement, outputFolder));
        project.getUserPaths().forEach(userPath -> UserPathWriter.of(userPath).writeXML(repositoryDocument, repositoryElement, outputFolder));
        project.getServers().forEach(serv -> ServerWriter.of(serv).writeXML(repositoryDocument, repositoryElement));
        project.getVariables().forEach(var -> WriterUtils.<VariableWriter>getWriterFor(var).writeXML(repositoryDocument, repositoryElement, outputFolder));
        project.getPopulations().forEach(pop -> PopulationWriter.of(pop).writeXML(repositoryDocument, repositoryElement));
        writeZoneAndLG(repositoryDocument, repositoryElement);

        final Element scenariosElement = scenarioDocument.createElement(XML_SCENARIOS_TAG_NAME);
        scenarioDocument.appendChild(scenariosElement);
        project.getScenarios().forEach(scenario -> ScenarioWriter.of(scenario).writeXML(scenarioDocument, scenariosElement));

    }

    private void writeZoneAndLG(final Document repositoryDocument, final Element repositoryElement) {
        // <zones defaultZoneUid="Default zone" dnsOverrideEnabled="false" dnsTtl="30"/>
        Element zonesElement = repositoryDocument.createElement(XML_ZONES_TAG_NAME);
        zonesElement.setAttribute(XML_ZONES_ATTR_DEFAULTZONEUID_NAME, DEFAULT_ZONE_NAME);
        zonesElement.setAttribute(XML_ZONES_ATTR_DNSOVERRIDE_NAME, Boolean.FALSE.toString());
        zonesElement.setAttribute(XML_ZONES_ATTR_DNSTTL_NAME, "30");
        repositoryElement.appendChild(zonesElement);

        //<zone dnsOverrideEnabled="false" proxyType="COMMON" proxyTypeLg="NO_PROXY" uid="Default zone">
        //  <embedded-action>ffb8381d-0598-491f-be33-9b2f072251d9</embedded-action>
        //</zone>
        String lgUuid = UUID.randomUUID().toString();
        Element zoneElement = repositoryDocument.createElement(XML_ZONE_TAG_NAME);
        zoneElement.setAttribute(XML_ZONES_ATTR_DNSOVERRIDE_NAME, Boolean.FALSE.toString());
        zoneElement.setAttribute(XML_ATTR_UID_NAME, DEFAULT_ZONE_NAME);
        Element embeddedElement = repositoryDocument.createElement(WriterUtils.EMBEDDED_ACTION_XML_TAG_NAME);
        embeddedElement.setTextContent(lgUuid);
        zoneElement.appendChild(embeddedElement);
        repositoryElement.appendChild(zoneElement);

        //<lg-host hostname="localhost:7100" loadWeight="1" standalone="false" uid="ffb8381d-0598-491f-be33-9b2f072251d9"/>
        Element lgHostElement = repositoryDocument.createElement(XML_LGHOST_TAG_NAME);
        lgHostElement.setAttribute("hostname", DEFAULT_LG_NAME);
        lgHostElement.setAttribute("loadWeight", "1");
        lgHostElement.setAttribute("standalone", Boolean.FALSE.toString());
        lgHostElement.setAttribute(XML_ATTR_UID_NAME, lgUuid);
        repositoryElement.appendChild(lgHostElement);

    }
}
