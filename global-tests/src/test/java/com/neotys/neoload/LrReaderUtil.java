package com.neotys.neoload;

import com.google.common.base.Charsets;
import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.readers.loadrunner.MutableContainer;
import com.neotys.neoload.model.repository.Container;

import java.io.*;

public class LrReaderUtil {
	
	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "", "");
	
	public static Container read(final String content) {
		try {
			final MutableContainer container = new MutableContainer("container");
			LOAD_RUNNER_READER.parseCppFile(container, "{", "}", getInputStream(content), Charsets.UTF_8);
			return container;
		} catch (final Exception e) {
		}
		return null;
	}
	
	private static InputStream getInputStream(final String content) throws Exception {
		final File temp = File.createTempFile(LrToNlTest.class.toString(), "");
		temp.deleteOnExit();
		final BufferedWriter out = new BufferedWriter(new FileWriter(temp));
		out.write("Action(){" + content + "}");
		out.close();
		return new FileInputStream(temp);
	}
}
