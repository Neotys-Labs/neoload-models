package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.WebSocketRequest;
import com.neotys.neoload.model.v3.validation.constraints.WebSocketChannelReferenceCheck;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidatorContext;

/**
 * Each {@code websocket_request.channel} of a user path must be the complete path of exactly one
 * of its channels. A request can only use a channel of its own user path: a virtual user only
 * holds the connections it opened itself.
 */
public final class WebSocketChannelReferenceValidator extends AbstractConstraintValidator<WebSocketChannelReferenceCheck, UserPath> {
	@Override
	public boolean isValid(final UserPath userPath, final ConstraintValidatorContext context) {
		if (userPath == null) {
			return true;
		}
		final List<WebSocketRequest> webSocketRequests = WebSocketChannelPaths.webSocketRequestsWithChannel(userPath);
		if (webSocketRequests.isEmpty()) {
			return true;
		}
		final WebSocketChannelPaths paths = WebSocketChannelPaths.of(userPath);
		final List<String> problems = new ArrayList<>();
		for (final WebSocketRequest webSocketRequest : webSocketRequests) {
			webSocketRequest.getChannel()
					.map(paths::resolve)
					.flatMap(WebSocketChannelPaths.Resolution::getProblem)
					.ifPresent(problem -> problems.add(WebSocketChannelPaths.prefix(webSocketRequest) + problem));
		}
		return WebSocketChannelPaths.report(context, problems);
	}
}
