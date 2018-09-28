package com.neotys.neoload.model.readers.loadrunner;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.neotys.neoload.model.Project;
import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.repository.Delay;
import com.neotys.neoload.model.repository.Page;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_READER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
@SuppressWarnings("squid:S2699")
public class LoadRunnerReaderTest {
	
    @Test
    public void methodReaderTest() {
        final String folder = new File(this.getClass().getResource("Action.c").getFile()).getParent();
        final LoadRunnerReader reader = new LoadRunnerReader(new TestEventListener(), folder, "", "");
        try(InputStream targetStream = this.getClass().getResourceAsStream("Action.c")) {
            final MutableContainer container = new MutableContainer("MyContainer");
            reader.parseCppFile(container, "{", "}", targetStream, Charsets.UTF_8);
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
        final String folder = new File(this.getClass().getResource("ActionRequest.c").getFile()).getParent();
    	final LoadRunnerReader reader = new LoadRunnerReader(new TestEventListener(), folder, "", "");
        try(InputStream targetStream = this.getClass().getResourceAsStream("ActionRequest.c")) {
            final MutableContainer container = new MutableContainer("MyContainer");
            reader.parseCppFile(container, "{", "}", targetStream, Charsets.UTF_8);
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
    public void readTest() throws IOException {
        File myTempDir = Files.createTempDir();
        FileUtils.copyInputStreamToFile(LoadRunnerReaderTest.class.getResourceAsStream("sample/vuser_init.c"), new File(myTempDir.getPath(),"vuser_init.c"));
        FileUtils.copyInputStreamToFile(LoadRunnerReaderTest.class.getResourceAsStream("sample/Action.c"), new File(myTempDir.getPath(),"Action.c"));
        FileUtils.copyInputStreamToFile(LoadRunnerReaderTest.class.getResourceAsStream("sample/ActionTransaction.c"), new File(myTempDir.getPath(),"ActionTransaction.c"));
        FileUtils.copyInputStreamToFile(LoadRunnerReaderTest.class.getResourceAsStream("sample/vuser_end.c"), new File(myTempDir.getPath(),"vuser_end.c"));
        FileUtils.copyInputStreamToFile(LoadRunnerReaderTest.class.getResourceAsStream("projectTest/projectTest.usr"), new File(myTempDir.getPath(),"projectTest.usr"));
        FileUtils.copyInputStreamToFile(LoadRunnerReaderTest.class.getResourceAsStream("projectTest/parameterTest.prm"), new File(myTempDir.getPath(),"parameterTest.prm"));

        LoadRunnerReader reader = new LoadRunnerReader(new TestEventListener(), myTempDir.getPath(), "myProject", "");
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
    	LOAD_RUNNER_READER.clear();
        assertThat(LOAD_RUNNER_READER.currentProjectServers.size()).isEqualTo(0);
        assertThat(LOAD_RUNNER_READER.getOrAddServerIfNotExist("myhost","myhost", "80",Optional.of("http")).getName()).isEqualTo("myhost");
        assertThat(LOAD_RUNNER_READER.currentProjectServers.size()).isEqualTo(1);
        assertThat(LOAD_RUNNER_READER.getOrAddServerIfNotExist("myhost","myhost", "80",Optional.of("http")).getName()).isEqualTo("myhost");
        assertThat(LOAD_RUNNER_READER.currentProjectServers.size()).isEqualTo(1);        
        assertThat(LOAD_RUNNER_READER.getOrAddServerIfNotExist("myhost2","myhost2", "80",Optional.of("http")).getName()).isEqualTo("myhost2");
        assertThat(LOAD_RUNNER_READER.currentProjectServers.size()).isEqualTo(2);
        assertThat(LOAD_RUNNER_READER.getOrAddServerIfNotExist("myhost2","myhost", "8080",Optional.of("http")).getName()).isEqualTo("myhost2_1");
        assertThat(LOAD_RUNNER_READER.currentProjectServers.size()).isEqualTo(3);
        assertThat(LOAD_RUNNER_READER.getOrAddServerIfNotExist("myhost2","myhost", "80",Optional.of("https")).getName()).isEqualTo("myhost2_2");
        assertThat(LOAD_RUNNER_READER.currentProjectServers.size()).isEqualTo(4);
    }
   
}
