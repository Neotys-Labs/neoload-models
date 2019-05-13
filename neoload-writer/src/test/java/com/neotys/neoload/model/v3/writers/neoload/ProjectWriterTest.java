package com.neotys.neoload.model.v3.writers.neoload;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.Writer;

public class ProjectWriterTest {

    @Test
    public void writeProjectTest() throws Exception {

        UserPath userPath = UserPath.builder()
                .name("my User")
                .actions(Container.builder()
                        .addSteps(Delay.builder().value("3000").build())
                        .build())
                .build();


        Project project = Project.builder()
                .name("my Project")
                .addUserPaths(userPath)
                .build();


        // write the repository
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document repositoryDocument = docBuilder.newDocument();
        Document scenarioDocument = docBuilder.newDocument();
        ProjectWriter.of(project).writeXML(repositoryDocument, scenarioDocument, "");

        /*
        <repository>
          <virtual-user uid="my User">
            <actions-container element-number="1" execution-type="0" weightsEnabled="false">
              <weighted-embedded-action uid="a3416926-93a6-4af6-969d-89a20e44a453"/>
            </actions-container>
          </virtual-user>
          <delay-action duration="3000" isThinkTime="false" name="#duration#" uid="a3416926-93a6-4af6-969d-89a20e44a453"/>
          <zones defaultZoneUid="Default zone" dnsOverrideEnabled="false" dnsTtl="30"/>
          <zone dnsOverrideEnabled="false" uid="Default zone">
            <embedded-action>859077c6-e412-4094-9796-d612ecade975</embedded-action>
          </zone>
          <lg-host hostname="localhost:7100" loadWeight="1" standalone="false" uid="859077c6-e412-4094-9796-d612ecade975"/>
        </repository>
         */
        Assertions.assertThat(repositoryDocument.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(5);
        Assertions.assertThat(repositoryDocument.getChildNodes().item(0).getChildNodes().item(0).getAttributes().getNamedItem("uid").getNodeValue()).isEqualTo("my User");
        Assertions.assertThat(repositoryDocument.getChildNodes().item(0).getChildNodes().item(2).getNodeName()).isEqualTo("zones");
        Assertions.assertThat(repositoryDocument.getChildNodes().item(0).getChildNodes().item(3).getNodeName()).isEqualTo("zone");
        Assertions.assertThat(repositoryDocument.getChildNodes().item(0).getChildNodes().item(4).getNodeName()).isEqualTo("lg-host");
    }

    public static final void prettyPrint(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        System.out.println(out.toString());
    }
}
