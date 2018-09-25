package com.neotys.neoload;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import com.google.common.base.Charsets;
import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.repository.Container;

public class LrReaderUtil {
	
	private static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "", "");
	
	public static Container read(final String content) {
		try {
			return LOAD_RUNNER_READER.parseCppFile("{", "}", getInputStream(content), "MyContainer", Charsets.UTF_8);
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
