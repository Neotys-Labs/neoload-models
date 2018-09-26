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

	@Test
	public void testLrExitGetElement_LR_EXIT_VUSER() {
		Element generatedElement_PASS = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_VUSER", "LR_PASS")
				.build(), METHOD_CALL_CONTEXT).get(0);
		Element generatedElement_FAIL = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_VUSER", "LR_FAIL")
				.build(), METHOD_CALL_CONTEXT).get(0);
		Element generatedElement_AUTO = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_VUSER", "LR_AUTO")
				.build(), METHOD_CALL_CONTEXT).get(0);

		Element expectedElement = ImmutableStop.builder()
				.name("lr_exit")
				.startNewVirtualUser(true)
				.build();
		assertEquals(expectedElement, generatedElement_PASS);
		assertEquals(expectedElement, generatedElement_FAIL);
		assertEquals(expectedElement, generatedElement_AUTO);
	}

	@Test
	public void testLrExitGetElement_LR_EXIT_ITERATION_AND_CONTINUE() {
		Element generatedElement_PASS = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_ITERATION_AND_CONTINUE", "LR_PASS")
				.build(), METHOD_CALL_CONTEXT).get(0);
		Element generatedElement_FAIL = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_ITERATION_AND_CONTINUE", "LR_FAIL")
				.build(), METHOD_CALL_CONTEXT).get(0);
		Element generatedElement_AUTO = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_ITERATION_AND_CONTINUE", "LR_AUTO")
				.build(), METHOD_CALL_CONTEXT).get(0);

		Element expectedElement_PASS_OR_AUTO = ImmutableGoToNextIteration.builder()
				.name("lr_exit")
				.build();
		assertEquals(expectedElement_PASS_OR_AUTO, generatedElement_PASS);
		assertEquals(expectedElement_PASS_OR_AUTO, generatedElement_AUTO);

		Element expectedElement_FAIL = ImmutableJavascript.builder()
				.name("lr_exit")
				.content("RuntimeContext.fail();")
				.build();
		assertEquals(expectedElement_FAIL, generatedElement_FAIL);
	}

	@Test
	public void testLrExitGetElement_LR_EXIT_MAIN_ITERATION_AND_CONTINUE() {
		Element generatedElement_PASS = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_MAIN_ITERATION_AND_CONTINUE", "LR_PASS")
				.build(), METHOD_CALL_CONTEXT).get(0);
		Element generatedElement_FAIL = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_MAIN_ITERATION_AND_CONTINUE", "LR_FAIL")
				.build(), METHOD_CALL_CONTEXT).get(0);
		Element generatedElement_AUTO = (new LrexitMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("lr_exit")
				.addParameters("LR_EXIT_MAIN_ITERATION_AND_CONTINUE", "LR_AUTO")
				.build(), METHOD_CALL_CONTEXT).get(0);

		Element expectedElement_PASS_OR_AUTO = ImmutableGoToNextIteration.builder()
				.name("lr_exit")
				.build();
		assertEquals(expectedElement_PASS_OR_AUTO, generatedElement_PASS);
		assertEquals(expectedElement_PASS_OR_AUTO, generatedElement_AUTO);

		Element expectedElement_FAIL = ImmutableJavascript.builder()
				.name("lr_exit")
				.content("RuntimeContext.fail();")
				.build();
		assertEquals(expectedElement_FAIL, generatedElement_FAIL);
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
