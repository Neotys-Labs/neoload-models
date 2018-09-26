package com.neotys.neoload.model.readers.loadrunner.method;

import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.repository.ImmutableGoToNextIteration;
import com.neotys.neoload.model.repository.ImmutableJavascript;
import com.neotys.neoload.model.repository.ImmutableStop;
import org.junit.Test;

import java.util.List;

import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_VISITOR;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.METHOD_CALL_CONTEXT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("squid:S2699")
public class LrexitMethodTest {
	private static Element STOP_ELEMENT = ImmutableStop.builder()
			.name("lr_exit")
			.startNewVirtualUser(true)
			.build();
	private static Element GO_TO_NEXT_ITERATION_ELEMENT = ImmutableGoToNextIteration.builder()
			.name("lr_exit")
			.build();
	private static Element FAIL_JAVA_SCRIPT_ELEMENT = ImmutableJavascript.builder()
			.name("failure-log")
			.content("context.fail();")
			.build();
	private static Element FAIL_JAVA_SCRIPT_WITH_MESSAGE_ELEMENT = ImmutableJavascript.builder()
			.name("failure-log")
			.content("context.fail('Exit with failure status');")
			.build();

	@Test
	public void testLrExitGetElement_LR_EXIT_VUSER() {
		List<Element> generatedElements_PASS = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_VUSER", "LR_PASS")
				.build(), METHOD_CALL_CONTEXT);
		assertEquals(generatedElements_PASS.size(), 1);
		assertEquals(generatedElements_PASS.get(0), STOP_ELEMENT);

		List<Element> generatedElements_FAIL = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_VUSER", "LR_FAIL")
				.build(), METHOD_CALL_CONTEXT);
		assertEquals(generatedElements_FAIL.size(), 2);
		assertEquals(generatedElements_FAIL.get(0), FAIL_JAVA_SCRIPT_WITH_MESSAGE_ELEMENT);
		assertEquals(generatedElements_FAIL.get(1), STOP_ELEMENT);

		List<Element> generatedElements_AUTO = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_VUSER", "LR_AUTO")
				.build(), METHOD_CALL_CONTEXT);
		assertEquals(generatedElements_AUTO.size(), 2);
		assertEquals(generatedElements_AUTO.get(0), FAIL_JAVA_SCRIPT_ELEMENT);
		assertEquals(generatedElements_AUTO.get(1), STOP_ELEMENT);
	}

	@Test
	public void testLrExitGetElement_LR_EXIT_ITERATION_AND_CONTINUE() {
		List<Element> generatedElements_PASS = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_ITERATION_AND_CONTINUE", "LR_PASS")
				.build(), METHOD_CALL_CONTEXT);
		assertEquals(generatedElements_PASS.size(), 1);
		assertEquals(generatedElements_PASS.get(0), GO_TO_NEXT_ITERATION_ELEMENT);

		List<Element> generatedElements_FAIL = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_ITERATION_AND_CONTINUE", "LR_FAIL")
				.build(), METHOD_CALL_CONTEXT);
		assertEquals(generatedElements_FAIL.size(), 2);
		assertEquals(generatedElements_FAIL.get(0), FAIL_JAVA_SCRIPT_WITH_MESSAGE_ELEMENT);
		assertEquals(generatedElements_FAIL.get(1), GO_TO_NEXT_ITERATION_ELEMENT);

		List<Element> generatedElements_AUTO = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_ITERATION_AND_CONTINUE", "LR_AUTO")
				.build(), METHOD_CALL_CONTEXT);
		assertEquals(generatedElements_AUTO.size(), 2);
		assertEquals(generatedElements_AUTO.get(0), FAIL_JAVA_SCRIPT_ELEMENT);
		assertEquals(generatedElements_AUTO.get(1), GO_TO_NEXT_ITERATION_ELEMENT);
	}

	@Test
	public void testLrExitGetElement_LR_EXIT_MAIN_ITERATION_AND_CONTINUE() {
		List<Element> generatedElements_PASS = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_MAIN_ITERATION_AND_CONTINUE", "LR_PASS")
				.build(), METHOD_CALL_CONTEXT);
		assertEquals(generatedElements_PASS.size(), 1);
		assertEquals(generatedElements_PASS.get(0), GO_TO_NEXT_ITERATION_ELEMENT);

		List<Element> generatedElements_FAIL = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_MAIN_ITERATION_AND_CONTINUE", "LR_FAIL")
				.build(), METHOD_CALL_CONTEXT);
		assertEquals(generatedElements_FAIL.size(), 2);
		assertEquals(generatedElements_FAIL.get(0), FAIL_JAVA_SCRIPT_WITH_MESSAGE_ELEMENT);
		assertEquals(generatedElements_FAIL.get(1), GO_TO_NEXT_ITERATION_ELEMENT);

		List<Element> generatedElements_AUTO = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_MAIN_ITERATION_AND_CONTINUE", "LR_AUTO")
				.build(), METHOD_CALL_CONTEXT);
		assertEquals(generatedElements_AUTO.size(), 2);
		assertEquals(generatedElements_AUTO.get(0), FAIL_JAVA_SCRIPT_ELEMENT);
		assertEquals(generatedElements_AUTO.get(1), GO_TO_NEXT_ITERATION_ELEMENT);
	}

	@Test
	public void testLrExitGetElement_DoesNotSupport() {
		List<Element> generatedElement_LR_EXIT_ACTION_AND_CONTINUE = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_ACTION_AND_CONTINUE", "LR_PASS")
				.build(), METHOD_CALL_CONTEXT);
		List<Element> generatedElement_LR_EXIT_VUSER_AFTER_ITERATION = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_VUSER_AFTER_ITERATION", "LR_FAIL")
				.build(), METHOD_CALL_CONTEXT);
		List<Element> generatedElement_LR_EXIT_VUSER_AFTER_ACTION = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_VUSER_AFTER_ACTION", "LR_AUTO")
				.build(), METHOD_CALL_CONTEXT);

		assertTrue(generatedElement_LR_EXIT_ACTION_AND_CONTINUE.isEmpty());
		assertTrue(generatedElement_LR_EXIT_VUSER_AFTER_ITERATION.isEmpty());
		assertTrue(generatedElement_LR_EXIT_VUSER_AFTER_ACTION.isEmpty());
	}
}
