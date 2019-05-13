package com.neotys.neoload.model.v3.writers.neoload.userpath;

import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class UserPathWriterTest {

    @Test
    public void writeXMLTest() throws Exception {
        UserPath userPath = UserPath.builder()
                .name("my User")
                .actions(Container.builder()
                        .name("delay")
                        .addSteps(Delay.builder().value("3000").build())
                        .build())
                .build();

        // write the repository
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        Element xmlUserpath = document.createElement("userpath-test");
        document.appendChild(xmlUserpath);
        UserPathWriter.of(userPath).writeXML(document, xmlUserpath, "");

        /*
        <userpath-test>
            <virtual-user uid="my User">
                <init-container/>
                <actions-container element-number="1" execution-type="0" uid="f80575c1-32ad-433a-8f19-d25d54b627fe" weightsEnabled="false">
                    <weighted-embedded-action uid="40d5093a-ef7d-4d6c-9530-7e205175e9af"/>
                </actions-container>
                <end-container/>
            </virtual-user>
            <delay-action duration="3000" isThinkTime="false" name="delay" uid="40d5093a-ef7d-4d6c-9530-7e205175e9af"/>
         </userpath-test>
         */
        Assertions.assertThat(xmlUserpath.getChildNodes().getLength()).isEqualTo(2);
        Assertions.assertThat(xmlUserpath.getChildNodes().item(0).getAttributes().getNamedItem("uid").getNodeValue()).isEqualTo("my User");
        Assertions.assertThat(xmlUserpath.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(3);
        Assertions.assertThat(xmlUserpath.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("init-container");
        Assertions.assertThat(xmlUserpath.getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("actions-container");
        Assertions.assertThat(xmlUserpath.getChildNodes().item(0).getChildNodes().item(2).getNodeName()).isEqualTo("end-container");
        Assertions.assertThat(xmlUserpath.getChildNodes().item(1).getNodeName()).isEqualTo("delay-action");
    }

}
