package com.neotys.neoload.model.readers.loadrunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.neotys.neoload.model.listener.TestEventListener;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.common.io.Files;
import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.repository.Delay;
import com.neotys.neoload.model.repository.ImmutableServer;
import com.neotys.neoload.model.repository.Page;
import com.neotys.neoload.model.Project;
import com.neotys.neoload.model.repository.Server;

public class LoadRunnerReaderTest {
	
    @Test
    public void methodReaderTest() {
    	final LoadRunnerReader reader = new LoadRunnerReader(new TestEventListener(), "", "");
        try(InputStream targetStream = this.getClass().getResourceAsStream("Action.c")) {
            Container container = reader.parseCppFile("{", "}", targetStream, "MyContainer");
            assertThat(container).isNotNull();
            assertThat(container.getChilds().size()).isEqualTo(5);
            assertThat(container.getChilds().get(0).getName()).isEqualTo("page1");
            assertThat(container.getChilds().get(1).getName()).isEqualTo("delay");
            assertThat(((Delay)container.getChilds().get(1)).getDelay()).isEqualTo("1000");
            assertThat(container.getChilds().get(2).getName()).isEqualTo("page2");
            assertThat(container.getChilds().get(3).getName()).isEqualTo("web_cache_cleanup");
            assertThat(container.getChilds().get(4).getName()).isEqualTo("web_cleanup_cookies");
        }catch(IOException e) {
            fail("Error reading test stream", e);
        }
    }

    @Test
    public void methodReaderTestFullRequest() {
    	final LoadRunnerReader reader = new LoadRunnerReader(new TestEventListener(), "", "");
        try(InputStream targetStream = this.getClass().getResourceAsStream("ActionRequest.c")) {
            Container container = reader.parseCppFile("{", "}", targetStream, "MyContainer");
            assertThat(container).isNotNull();
            assertThat(container.getChilds().size()).isEqualTo(1);
            assertThat(container.getChilds().get(0).getName()).isEqualTo("page1");
            final Page page = (Page)container.getChilds().get(0);
            assertThat(page.getChilds().size()).isEqualTo(12);
            // be sure all the requests names are unique
            ((Page)container.getChilds().get(0)).getChilds().stream().forEach(pageElement -> {
                if(page.getChilds().stream().filter(pageElement1 -> pageElement1.getName().equals(pageElement.getName())).count()!=1) {
                    fail("request names under a page are not unique");
                }
            });
        }catch(IOException e) {
            fail("Error reading test stream", e);
        }
    }

    @Test
    public void transactionsReaderTest() {
    	final LoadRunnerReader reader = new LoadRunnerReader(new TestEventListener(), "", "");
        try(InputStream targetStream = this.getClass().getResourceAsStream("ActionTransaction.c")) {
            Container container = reader.parseCppFile("{", "}", targetStream, "MyContainer");
            assertThat(container).isNotNull();
            assertThat(container.getChilds().size()).isEqualTo(2);
            assertThat(container.getChilds().get(0).getName()).isEqualTo("level#1");
            assertThat(container.getChilds().get(1).getName()).isEqualTo("page#3");
            assertThat(((Container)container.getChilds().get(0)).getChilds().size()).isEqualTo(2);
            assertThat(((Container)container.getChilds().get(0)).getChilds().get(0).getName()).isEqualTo("level#2");
            assertThat(((Container)container.getChilds().get(0)).getChilds().get(1).getName()).isEqualTo("page#2");
            assertThat(((Container)((Container)container.getChilds().get(0)).getChilds().get(0)).getChilds().get(0).getName()).isEqualTo("page#1");
        }catch(IOException e) {
            fail("Error reading test stream", e);
        }
    }

    @Test
    public void readTest() throws IOException {
        File myTempDir = Files.createTempDir();
        FileUtils.copyInputStreamToFile(LoadRunnerReaderTest.class.getResourceAsStream("sample/vuser_init.c"), new File(myTempDir.getPath(),"vuser_init.c"));
        FileUtils.copyInputStreamToFile(LoadRunnerReaderTest.class.getResourceAsStream("sample/Action.c"), new File(myTempDir.getPath(),"Action.c"));
        FileUtils.copyInputStreamToFile(LoadRunnerReaderTest.class.getResourceAsStream("sample/ActionTransaction.c"), new File(myTempDir.getPath(),"ActionTransaction.c"));
        FileUtils.copyInputStreamToFile(LoadRunnerReaderTest.class.getResourceAsStream("sample/vuser_end.c"), new File(myTempDir.getPath(),"vuser_end.c"));
        FileUtils.copyInputStreamToFile(LoadRunnerReaderTest.class.getResourceAsStream("projectTest/projectTest.usr"), new File(myTempDir.getPath(),"projectTest.usr"));
        FileUtils.copyInputStreamToFile(LoadRunnerReaderTest.class.getResourceAsStream("projectTest/parameterTest.prm"), new File(myTempDir.getPath(),"parameterTest.prm"));

        LoadRunnerReader reader = new LoadRunnerReader(new TestEventListener(), myTempDir.getPath(), "myProject");
        Project project = reader.read();
        assertThat(project.getUserPaths().size()).isEqualTo(1);
        assertThat(project.getUserPaths().get(0).getInitContainer().getChilds().size()).isEqualTo(1);
        assertThat(project.getUserPaths().get(0).getActionsContainer().getChilds().size()).isEqualTo(2);
        assertThat(project.getUserPaths().get(0).getActionsContainer().getChilds().get(0).getName()).isEqualTo("Action");
        assertThat(project.getUserPaths().get(0).getActionsContainer().getChilds().get(1).getName()).isEqualTo("ActionTransaction");
        assertThat(((Container)project.getUserPaths().get(0).getActionsContainer().getChilds().get(0)).getChilds().get(0).getName()).isEqualTo("My transaction");
        assertThat(project.getUserPaths().get(0).getEndContainer().getChilds().size()).isEqualTo(1);
    }

    @Test
    public void getOrAddServerTest() {
        final LoadRunnerReader reader = new LoadRunnerReader(new TestEventListener(), "",  "");
        Server server = reader.getOrAddServerIfNotExist(ImmutableServer.builder().host("myhost").port("80").name("myhost").scheme("http").build());
        assertThat(reader.currentProjectServers.size()).isEqualTo(1);
        reader.getOrAddServerIfNotExist(server);
        assertThat(reader.currentProjectServers.size()).isEqualTo(1);
        reader.getOrAddServerIfNotExist(ImmutableServer.builder().host("myhost").port("80").name("myhost").scheme("http").build());
        assertThat(reader.currentProjectServers.size()).isEqualTo(1);
        reader.getOrAddServerIfNotExist(ImmutableServer.builder().host("myhost2").port("80").name("myhost").scheme("http").build());
        assertThat(reader.currentProjectServers.size()).isEqualTo(2);
    }
    
    @Test
    public void subTransactionsReaderTest() {
    	final LoadRunnerReader reader = new LoadRunnerReader(new TestEventListener(), "", "");
        try(InputStream targetStream = this.getClass().getResourceAsStream("ActionSubTransaction.c")) {
            Container container = reader.parseCppFile("{", "}", targetStream, "MyContainer");
            assertThat(container).isNotNull();
            assertThat(container.getChilds().size()).isEqualTo(3);
            assertThat(container.getChilds().get(0).getName()).isEqualTo("level#2a");
            assertThat(container.getChilds().get(1).getName()).isEqualTo("page#2");
            assertThat(container.getChilds().get(2).getName()).isEqualTo("level#2b");
            assertThat(((Container)container.getChilds().get(0)).getChilds().size()).isEqualTo(1);
            assertThat(((Container)container.getChilds().get(0)).getChilds().get(0).getName()).isEqualTo("page#1");
            assertThat(((Container)container.getChilds().get(2)).getChilds().size()).isEqualTo(1);
            assertThat(((Container)container.getChilds().get(2)).getChilds().get(0).getName()).isEqualTo("page#3");
            
        }catch(IOException e) {
            fail("Error reading test stream", e);
        }
    }
}
