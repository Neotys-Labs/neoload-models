package com.neotys.neoload.model.readers.loadrunner;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_READER;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Iterables;
import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.loadrunner.filereader.ProjectFileReader;
@SuppressWarnings("squid:S2699")
public class ProjectFileReaderTest {
	
	@Test
	public void loadTest() throws URISyntaxException {
		URL url = this.getClass().getResource("projectTest");
		File projectFolder = new File(url.toURI());
		
		ProjectFileReader pfr = new ProjectFileReader(LOAD_RUNNER_READER, new TestEventListener(), projectFolder);
		
		assertEquals(pfr.getParameterFile(), "test_param_file.prm");
		assertEquals(pfr.getLeftBrace(), "(");
		assertEquals(pfr.getRightBrace(), "}");
		Map<String,String> actionsMap = pfr.getAllActionsMap();
		assertEquals(actionsMap.size(), 4);
		assertEquals(Iterables.get(actionsMap.values(),0), "vuser_init.c");
		assertEquals(Iterables.get(actionsMap.values(),2), "ActionTransaction.c");

		assertEquals(pfr.getInits().size(), 2);
		assertEquals(Iterables.get(pfr.getInits(),0), "Login");
		assertEquals(Iterables.get(pfr.getInits(),1), "vuser_init");

		assertEquals(pfr.getActions().size(), 2);
		assertEquals(Iterables.get(pfr.getActions(),0), "Login");
		assertEquals(Iterables.get(pfr.getActions(),1), "Action");

		assertEquals(pfr.getEnds().size(), 1);
		assertEquals(Iterables.get(pfr.getEnds(),0), "vuser_end");
	}

}

