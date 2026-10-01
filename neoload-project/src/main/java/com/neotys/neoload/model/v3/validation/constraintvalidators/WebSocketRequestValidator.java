package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.WebSocketRequest;
import com.neotys.neoload.model.v3.project.userpath.WebSocketRequest.MessageType;
import com.neotys.neoload.model.v3.validation.constraints.WebSocketRequestCheck;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidatorContext;

/**
 * Rejects the settings NeoLoad would ignore, following the WebSocket request panel of the GUI
 * ({@code WebSocketModePanel}, {@code ActionAdvancedEditor}): a close cannot be synchronous, the
 * status code is only shown for a close, and the mapping id, the variable extractors and the
 * assertions only for a synchronous request, which is the only kind that waits for a response.
 */
public final class WebSocketRequestValidator extends AbstractConstraintValidator<WebSocketRequestCheck, WebSocketRequest> {
	@Override
	public boolean isValid(final WebSocketRequest request, final ConstraintValidatorContext context) {
		if (request == null) {
			return true;
		}
		final List<String> problems = new ArrayList<>();
		if (request.isSynchronous()) {
			checkSynchronous(request, problems);
		} else {
			checkAsynchronous(request, problems);
		}
		checkContent(request, problems);
		final String prefix = WebSocketChannelPaths.prefix(request);
		return WebSocketChannelPaths.report(context, problems.stream().map(problem -> prefix + problem).collect(Collectors.toList()));
	}

	private static void checkSynchronous(final WebSocketRequest request, final List<String> problems) {
		if (isClose(request)) {
			problems.add("a 'close' request cannot be synchronous.");
		}
		if (!request.getMappingId().isPresent()) {
			problems.add("a synchronous request requires 'mapping_id'.");
		}
	}

	/** An asynchronous request does not wait for a response, so nothing that works on one applies to it. */
	private static void checkAsynchronous(final WebSocketRequest request, final List<String> problems) {
		if (request.getMappingId().isPresent()) {
			problems.add("'mapping_id' is only used by a synchronous request.");
		}
		if (!request.getExtractors().isEmpty()) {
			problems.add("'extractors' are only applied on a synchronous request: an asynchronous one has no response.");
		}
		if (!request.getAssertions().isEmpty()) {
			problems.add("'assertions' are only checked on a synchronous request: an asynchronous one has no response.");
		}
	}

	private static void checkContent(final WebSocketRequest request, final List<String> problems) {
		if (!isClose(request) && !WebSocketRequest.DEFAULT_STATUS_CODE.equals(request.getStatusCode())) {
			problems.add("'status_code' is only used by a 'close' request.");
		}
		if (request.getBody().isPresent() && request.getBodyBinary().isPresent()) {
			problems.add("'body' and 'bodybinary' cannot be used simultaneously.");
		}
	}

	private static boolean isClose(final WebSocketRequest request) {
		return request.getMessageType() == MessageType.CLOSE;
	}
}
