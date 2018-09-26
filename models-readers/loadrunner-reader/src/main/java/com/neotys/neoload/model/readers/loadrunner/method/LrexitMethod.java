package com.neotys.neoload.model.readers.loadrunner.method;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.parsers.CPP14Parser.MethodcallContext;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerVUVisitor;
import com.neotys.neoload.model.readers.loadrunner.MethodCall;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.ImmutableGoToNextIteration;
import com.neotys.neoload.model.repository.ImmutableJavascript;
import com.neotys.neoload.model.repository.ImmutableStop;

import java.util.List;
import java.util.Optional;

import static com.neotys.neoload.model.readers.loadrunner.MethodUtils.normalizeString;

public class LrexitMethod implements LoadRunnerMethod {

	public LrexitMethod() {
		super();
	}

	@Override
	public List<Element> getElement(final LoadRunnerVUVisitor visitor, final MethodCall method, final MethodcallContext ctx) {
		Preconditions.checkNotNull(method);
		if (method.getParameters() == null || method.getParameters().size() < 2) {
			visitor.readSupportedFunctionWithWarn(method.getName(), ctx, method.getName() + " method must have at least 2 parameters");
			return ImmutableList.of();
		}
		visitor.readSupportedFunction(method.getName(), ctx);
		final String continuationOptionString = normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(),
				method.getParameters().get(0));
		final String exitStatusString = normalizeString(visitor.getLeftBrace(), visitor.getRightBrace(),
				method.getParameters().get(1));

		try {
			final ContinuationOption continuationOption = ContinuationOption.valueOf(continuationOptionString);
			final ExitStatus exitStatus = ExitStatus.valueOf(exitStatusString);

			switch (continuationOption) {
				case LR_EXIT_VUSER:
					return buildStopElements(method, exitStatus);
				case LR_EXIT_ITERATION_AND_CONTINUE:
				case LR_EXIT_MAIN_ITERATION_AND_CONTINUE:
					return buildGoToNextIterations(method, exitStatus);
				default:
					//do not support these continuation options
					break;
			}
		} catch (final IllegalArgumentException e) {
			//Can not parse ContinuationOption or ExitStatus
		}
		visitor.readSupportedFunctionWithWarn(method.getName(), ctx,
				method.getName() + " has 2 not supported parameters, continuationOption: " + continuationOptionString
						+ ", exitStatus: " + exitStatusString);
		return ImmutableList.of();
	}

	private ImmutableList<Element> buildStopElements(final MethodCall method, final ExitStatus exitStatus) {
		ImmutableList.Builder<Element> builder = ImmutableList.builder();
		buildJavaScriptIfNeeded(exitStatus).ifPresent(builder::add);

		builder.add(ImmutableStop.builder()
				.name(MethodUtils.unquote(method.getName()))
				.startNewVirtualUser(true)
				.build());

		return builder.build();
	}

	private List<Element> buildGoToNextIterations(final MethodCall method, final ExitStatus exitStatus) {
		ImmutableList.Builder<Element> builder = ImmutableList.builder();
		buildJavaScriptIfNeeded(exitStatus).ifPresent(builder::add);

		builder.add(ImmutableGoToNextIteration.builder()
				.name(MethodUtils.unquote(method.getName()))
				.build());

		return builder.build();
	}

	private Optional<Element> buildJavaScriptIfNeeded(final ExitStatus exitStatus) {
		switch (exitStatus) {
			case LR_PASS:
				return Optional.empty();
			case LR_AUTO:
				return Optional.of(buildJavaScript("RuntimeContext.fail();"));
			case LR_FAIL:
				return Optional.of(buildJavaScript("RuntimeContext.fail('Exit with failure status');"));
			default:
				throw new IllegalArgumentException("Do not support the status exit: " + exitStatus);
		}
	}

	private Element buildJavaScript(final String javaScriptContent) {
		return ImmutableJavascript.builder()
				.name("failure-log")
				.content(javaScriptContent)
				.build();
	}

	private enum ContinuationOption {
		LR_EXIT_VUSER,
		LR_EXIT_ACTION_AND_CONTINUE,
		LR_EXIT_ITERATION_AND_CONTINUE,
		LR_EXIT_VUSER_AFTER_ITERATION,
		LR_EXIT_VUSER_AFTER_ACTION,
		LR_EXIT_MAIN_ITERATION_AND_CONTINUE
	}

	private enum ExitStatus {
		LR_PASS,
		LR_FAIL,
		LR_AUTO
	}
}
