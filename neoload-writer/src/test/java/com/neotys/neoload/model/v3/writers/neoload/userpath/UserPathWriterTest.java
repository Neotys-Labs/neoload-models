package com.neotys.neoload.model.v3.writers.neoload.userpath;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.writers.neoload.WrittingTestUtils;


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
    	final Document document = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(document);
    	
    	UserPathWriter.of(userPath).writeXML(document, root, "");

        /*
		<?xml version="1.0" encoding="UTF-8" ?>
		<test-root>
			<virtual-user uid="my User">
				<init-container element-number="1" execution-type="0" slaProfileEnabled="false" weightsEnabled="false" />
				<actions-container element-number="1" execution-type="0" slaProfileEnabled="false" weightsEnabled="false">
					<weighted-embedded-action uid="3d08d6f5-bd1e-4807-84f2-5ec1bad2b10d" />
				</actions-container>
				<end-container element-number="1" execution-type="0" slaProfileEnabled="false" weightsEnabled="false" />
			</virtual-user>
			<delay-action duration="3000" isThinkTime="false" name="#duration#" uid="3d08d6f5-bd1e-4807-84f2-5ec1bad2b10d" />
		</test-root>
         */
        Assertions.assertThat(root.getChildNodes().getLength()).isEqualTo(2);
        
        Assertions.assertThat(root.getChildNodes().item(0).getAttributes().getNamedItem("uid").getNodeValue()).isEqualTo("my User");
        Assertions.assertThat(root.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(3);
        Assertions.assertThat(root.getChildNodes().item(0).getChildNodes().item(0).getNodeName()).isEqualTo("init-container");
        Assertions.assertThat(root.getChildNodes().item(0).getChildNodes().item(1).getNodeName()).isEqualTo("actions-container");
        Assertions.assertThat(root.getChildNodes().item(0).getChildNodes().item(2).getNodeName()).isEqualTo("end-container");
        
        Assertions.assertThat(root.getChildNodes().item(1).getNodeName()).isEqualTo("delay-action");
    }

    @Test
    public void writeXMLWithAssertionsTest() throws Exception {
        UserPath userPath = UserPath.builder()
                .name("My User")
                .init(Container.builder()
                        .name("init")
                        .addAssertions(ContentAssertion.builder()
                        		.contains("userpath_init_contains_1")
                        		.build())
                        .build())
                .actions(Container.builder()
                        .name("actions")
                        .addSteps(Delay.builder().value("3000").build())
                        .addAssertions(ContentAssertion.builder()
                        		.contains("userpath_actions_contains_1")
                        		.build())
                        .build())
                .end(Container.builder()
                        .name("end")
                        .addAssertions(ContentAssertion.builder()
                        		.contains("userpath_end_contains_1")
                        		.build())
                        .build())
                .addAssertions(ContentAssertion.builder()
                		.contains("userpath_contains_1")
                		.build())
                .build();

        // write the repository
    	final Document document = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(document);
    	
    	UserPathWriter.of(userPath).writeXML(document, root, "");

        /*
		<?xml version="1.0" encoding="UTF-8" ?>
		<test-root>
			<virtual-user uid="My User">
				<init-container element-number="1" execution-type="0" slaProfileEnabled="false" weightsEnabled="false">
					<assertions>
						<assertion-content name="assertion_1" notType="false" pattern="userpath_init_contains_1" />
					</assertions>
				</init-container>
				<actions-container element-number="1" execution-type="0" slaProfileEnabled="false" weightsEnabled="false">
					<weighted-embedded-action uid="f370617b-942e-42a7-a42d-a69c2a539186" />
					<assertions>
						<assertion-content name="assertion_1" notType="false" pattern="userpath_actions_contains_1" />
					</assertions>
				</actions-container>
				<end-container element-number="1" execution-type="0" slaProfileEnabled="false" weightsEnabled="false">
					<assertions>
						<assertion-content name="assertion_1" notType="false" pattern="userpath_end_contains_1" />
					</assertions>
				</end-container>
				<assertions>
					<assertion-content name="assertion_1" notType="false" pattern="userpath_contains_1" />
				</assertions>
			</virtual-user>
			<delay-action duration="3000" isThinkTime="false" name="#duration#" uid="f370617b-942e-42a7-a42d-a69c2a539186" />
		</test-root>
         */
        Assertions.assertThat(root.getChildNodes().getLength()).isEqualTo(2);
        Assertions.assertThat(root.getChildNodes().item(0).getNodeName()).isEqualTo("virtual-user");
        Assertions.assertThat(root.getChildNodes().item(1).getNodeName()).isEqualTo("delay-action");
        
        final Node xmlUserPath = root.getChildNodes().item(0);
        Assertions.assertThat(xmlUserPath.getAttributes().getNamedItem("uid").getNodeValue()).isEqualTo("My User");
        
        Assertions.assertThat(xmlUserPath.getChildNodes().getLength()).isEqualTo(4);
        Assertions.assertThat(xmlUserPath.getChildNodes().item(0).getNodeName()).isEqualTo("init-container");
        Assertions.assertThat(xmlUserPath.getChildNodes().item(1).getNodeName()).isEqualTo("actions-container");
        Assertions.assertThat(xmlUserPath.getChildNodes().item(2).getNodeName()).isEqualTo("end-container");
        Assertions.assertThat(xmlUserPath.getChildNodes().item(3).getNodeName()).isEqualTo("assertions");
        
        final Node xmlInitContainer = xmlUserPath.getChildNodes().item(0);      
        Assertions.assertThat(xmlInitContainer.getChildNodes().item(0).getNodeName()).isEqualTo("assertions");
        Assertions.assertThat(xmlInitContainer.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(1);
        Node xmlAssertion = xmlInitContainer.getChildNodes().item(0);
        Assertions.assertThat(xmlAssertion.getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_1");
        Assertions.assertThat(xmlAssertion.getChildNodes().item(0).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertion.getChildNodes().item(0).getAttributes().getNamedItem("pattern").getNodeValue()).isEqualTo("userpath_init_contains_1");

        final Node xmlActionsContainer = xmlUserPath.getChildNodes().item(1);      
        Assertions.assertThat(xmlActionsContainer.getChildNodes().item(1).getNodeName()).isEqualTo("assertions");
        Assertions.assertThat(xmlActionsContainer.getChildNodes().item(1).getChildNodes().getLength()).isEqualTo(1);
        xmlAssertion = xmlActionsContainer.getChildNodes().item(1);
        Assertions.assertThat(xmlAssertion.getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_1");
        Assertions.assertThat(xmlAssertion.getChildNodes().item(0).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertion.getChildNodes().item(0).getAttributes().getNamedItem("pattern").getNodeValue()).isEqualTo("userpath_actions_contains_1");

        final Node xmlEndContainer = xmlUserPath.getChildNodes().item(2);      
        Assertions.assertThat(xmlEndContainer.getChildNodes().item(0).getNodeName()).isEqualTo("assertions");
        Assertions.assertThat(xmlEndContainer.getChildNodes().item(0).getChildNodes().getLength()).isEqualTo(1);
        xmlAssertion = xmlEndContainer.getChildNodes().item(0);
        Assertions.assertThat(xmlAssertion.getChildNodes().item(0).getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_1");
        Assertions.assertThat(xmlAssertion.getChildNodes().item(0).getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertion.getChildNodes().item(0).getAttributes().getNamedItem("pattern").getNodeValue()).isEqualTo("userpath_end_contains_1");

        final Node xmlAssertions = xmlUserPath.getChildNodes().item(3);      
        Assertions.assertThat(xmlAssertions.getNodeName()).isEqualTo("assertions");
        Assertions.assertThat(xmlAssertions.getChildNodes().getLength()).isEqualTo(1);
        xmlAssertion = xmlAssertions.getChildNodes().item(0);
        Assertions.assertThat(xmlAssertion.getAttributes().getNamedItem("name").getNodeValue()).isEqualTo("assertion_1");
        Assertions.assertThat(xmlAssertion.getAttributes().getNamedItem("notType").getNodeValue()).isEqualTo("false");
        Assertions.assertThat(xmlAssertion.getAttributes().getNamedItem("pattern").getNodeValue()).isEqualTo("userpath_contains_1");    
    }
}
