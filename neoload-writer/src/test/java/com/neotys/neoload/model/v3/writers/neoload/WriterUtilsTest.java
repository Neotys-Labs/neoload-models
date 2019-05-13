package com.neotys.neoload.model.v3.writers.neoload;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.server.ImmutableServer;
import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.variable.*;
import com.neotys.neoload.model.v3.writers.neoload.server.ServerWriter;
import com.neotys.neoload.model.v3.writers.neoload.variable.VariableWriter;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class WriterUtilsTest {

    /*@Test
    public void getWriterForPageTest() {
        Page page = ImmutablePage.builder()
                .name("TEST")
                .thinkTime(0)
                .isDynamic(false)
                .build();
        assertThat(WriterUtils.<ElementWriter>getWriterFor(page).getClass().getSimpleName()).isEqualTo("PageWriter");
    }*/

    public void getWriterForServerTest() {
        Server server = new ImmutableServer.Builder()
                .host("myhost")
                .name("myServer")
                .port("443")
                .scheme(Server.Scheme.HTTPS)
                .build();
        assertThat(WriterUtils.<ServerWriter>getWriterFor(server).getClass().getSimpleName()).isEqualTo("ServerWriter");

    }


    @Test
    public void getWriterForVariableTest() {
        FileVariable var = new FileVariable.Builder()
                .name("TEST")
                .path("myfile")
                .changePolicy(Variable.ChangePolicy.EACH_USE)
                .order(Variable.Order.SEQUENTIAL)
                .isFirstLineColumnNames(false)
                .columnNames(ImmutableList.of("col1"))
                .delimiter(",")
                .scope(Variable.Scope.LOCAL)
                .startFromLine(0)
                .build();
        assertThat(WriterUtils.<VariableWriter>getWriterFor(var).getClass().getSimpleName()).isEqualTo("FileVariableWriter");

        ConstantVariable constantVariable = new ConstantVariable.Builder()
                .name("TEST")
                .value("3")
                .changePolicy(Variable.ChangePolicy.EACH_USER)
                .order(Variable.Order.SEQUENTIAL)
                .scope(Variable.Scope.LOCAL)
                .build();
        assertThat(WriterUtils.getWriterFor(constantVariable).getClass().getSimpleName()).isEqualTo("ConstantVariableWriter");
    }

    /*@Test
    public void generateEmbeddedActionTest() throws ParserConfigurationException, TransformerException {
    	final Document doc = WrittingTestUtils.generateEmptyDocument();
    	final Element root = WrittingTestUtils.generateTestRootElement(doc);

    	final Page page = ImmutablePage.builder()
                .name("TEST")
                .thinkTime(0)
                .isDynamic(false)
                .build();
    	WriterUtils.generateEmbeddedAction(doc, root, page, Optional.of("action-test"));
    	final String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><test-root><action-test>" + WriterUtils.getElementUid(page)+ "</action-test></test-root>";
    	final String generatedResult = WrittingTestUtils.getXmlString(doc);
    	assertEquals(expectedResult, generatedResult);
    }*/

    @Test
    public void isNLVariableTest() {
    	assertEquals(false, WriterUtils.isNLVariable(null));
    	assertEquals(false, WriterUtils.isNLVariable(""));
    	assertEquals(false, WriterUtils.isNLVariable("toto"));
    	assertEquals(false, WriterUtils.isNLVariable("${}"));
    	assertEquals(false, WriterUtils.isNLVariable("$"));
    	assertEquals(false, WriterUtils.isNLVariable("{}"));
    	assertEquals(false, WriterUtils.isNLVariable("$}"));
    	assertEquals(false, WriterUtils.isNLVariable("${"));
    	assertEquals(false, WriterUtils.isNLVariable("${toto"));
    	assertEquals(false, WriterUtils.isNLVariable("{toto}"));
    	assertEquals(true, WriterUtils.isNLVariable("${toto}"));
    }
    
    @Test
    public void extractVariableNameTest() {    	
    	assertEquals("toto", WriterUtils.extractVariableName("${toto}"));
    }
}
