package com.neotys.neoload.model.readers.loadrunner;

import com.neotys.neoload.model.Project;
import com.neotys.neoload.model.listener.TestEventListener;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("squid:S2699")
public class ContainerInFileMethodTest {

	@Test
	public void containersInFileReaderTest() {

		final URL url = this.getClass().getResource("functionsfiles");
		final File projectFolder = new File(url.getFile());

		final LoadRunnerReader lrReader = new LoadRunnerReader(new TestEventListener(), projectFolder.getPath(), "project",  "");

		final Project project = lrReader.read();
		assertThat(project).isNotNull();
		assertThat(project.getSharedElements()).isNotEmpty();
		// TODO verify project
	}
}
