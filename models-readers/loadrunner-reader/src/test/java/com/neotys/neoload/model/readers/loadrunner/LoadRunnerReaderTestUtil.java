package com.neotys.neoload.model.readers.loadrunner;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;

public class LoadRunnerReaderTestUtil {

	public static final LoadRunnerReader LOAD_RUNNER_READER = new LoadRunnerReader(new TestEventListener(), "", "", "");
	public static final LoadRunnerVUVisitor LOAD_RUNNER_VISITOR = new LoadRunnerVUVisitor(LOAD_RUNNER_READER, "{", "}", "");
	public static final MethodcallContext METHOD_CALL_CONTEXT = new MethodcallContext(null, 0);

}
