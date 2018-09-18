package com.neotys.neoload.model.readers.loadrunner;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_READER;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Iterables;
import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.loadrunner.filereader.ProjectFileReader;
@SuppressWarnings("squid:S2699")
public class ProjectFileReaderTest {
	
	@Test
	public void loadTest() {
		URL url = this.getClass().getResource("projectTest");
		File projectFolder = new File(url.getFile());		
		
		ProjectFileReader pfr = new ProjectFileReader(LOAD_RUNNER_READER, new TestEventListener(), projectFolder);
		
		assertEquals(pfr.getParameterFile(), "test_param_file.prm");
		assertEquals(pfr.getLeftBrace(), "(");
		assertEquals(pfr.getRightBrace(), "}");
		Map<String,String> actionsMap = pfr.getActions();
		assertEquals(actionsMap.size(), 4);
		assertEquals(Iterables.get(actionsMap.values(),0), "vuser_init.c");
		assertEquals(Iterables.get(actionsMap.values(),2), "ActionTransaction.c");
	}

}

