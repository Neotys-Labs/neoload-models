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
			return null;
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
					return ImmutableList.of(ImmutableStop.builder()
							.name(MethodUtils.unquote(method.getName()))
							.startNewVirtualUser(true)
							.build());
				case LR_EXIT_ITERATION_AND_CONTINUE:
				case LR_EXIT_MAIN_ITERATION_AND_CONTINUE:
					if (ExitStatus.LR_PASS == exitStatus || ExitStatus.LR_AUTO == exitStatus) {
						return ImmutableList.of(ImmutableGoToNextIteration.builder()
								.name(MethodUtils.unquote(method.getName()))
								.build());
					} else if (ExitStatus.LR_FAIL == exitStatus) {
						return ImmutableList.of(ImmutableJavascript.builder()
								.name(MethodUtils.unquote(method.getName()))
								.content("RuntimeContext.fail();")
								.build());
					}
					break;
			}
		} catch (final IllegalArgumentException e) {
			//Can not parse ContinuationOption or ExitStatus
		}
		visitor.readSupportedFunctionWithWarn(method.getName(), ctx,
				method.getName() + " has 2 not supported parameters, continuationOption: " + continuationOptionString
						+ ", exitStatus: " + exitStatusString);
		return null;
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
