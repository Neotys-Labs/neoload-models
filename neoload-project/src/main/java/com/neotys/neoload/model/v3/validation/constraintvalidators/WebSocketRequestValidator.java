package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.WebSocketRequest;
import com.neotys.neoload.model.v3.project.userpath.WebSocketRequest.MessageType;
import com.neotys.neoload.model.v3.validation.constraints.WebSocketRequestCheck;
import java.util.ArrayList;
import java.util.List;
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
		final String prefix = WebSocketChannelPaths.prefix(request);
		final List<String> problems = new ArrayList<>();
		final boolean close = request.getMessageType() == MessageType.CLOSE;
		if (request.isSynchronous()) {
			if (close) {
				problems.add(prefix + "a 'close' request cannot be synchronous.");
			}
			if (!request.getMappingId().isPresent()) {
				problems.add(prefix + "a synchronous request requires 'mapping_id'.");
			}
		} else {
			if (request.getMappingId().isPresent()) {
				problems.add(prefix + "'mapping_id' is only used by a synchronous request.");
			}
			if (!request.getExtractors().isEmpty()) {
				problems.add(prefix + "'extractors' are only applied on a synchronous request: an asynchronous one has no response.");
			}
			if (!request.getAssertions().isEmpty()) {
				problems.add(prefix + "'assertions' are only checked on a synchronous request: an asynchronous one has no response.");
			}
		}
		if (!close && !WebSocketRequest.DEFAULT_STATUS_CODE.equals(request.getStatusCode())) {
			problems.add(prefix + "'status_code' is only used by a 'close' request.");
		}
		if (request.getBody().isPresent() && request.getBodyBinary().isPresent()) {
			problems.add(prefix + "'body' and 'bodybinary' cannot be used simultaneously.");
		}
		return WebSocketChannelPaths.report(context, problems);
	}
}
