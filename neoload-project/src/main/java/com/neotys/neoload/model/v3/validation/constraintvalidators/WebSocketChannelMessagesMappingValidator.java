package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.WebSocketRequest;
import com.neotys.neoload.model.v3.validation.constraints.WebSocketChannelMessagesMappingCheck;
import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidatorContext;

/**
 * A synchronous request waits for the inbound frame whose correlation id equals its
 * {@code mapping_id}, and only its channel's {@code messages_mapping} says how to extract that id.
 * A reference that resolves to no channel is left to {@link WebSocketChannelReferenceValidator}.
 */
public final class WebSocketChannelMessagesMappingValidator extends AbstractConstraintValidator<WebSocketChannelMessagesMappingCheck, UserPath> {
	@Override
	public boolean isValid(final UserPath userPath, final ConstraintValidatorContext context) {
		if (userPath == null) {
			return true;
		}
		final List<WebSocketRequest> requests = WebSocketChannelPaths.requestsWithChannel(userPath);
		if (requests.stream().noneMatch(WebSocketRequest::isSynchronous)) {
			return true;
		}
		final WebSocketChannelPaths paths = WebSocketChannelPaths.of(userPath);
		final List<String> problems = new ArrayList<>();
		for (final WebSocketRequest request : requests) {
			if (!request.isSynchronous()) {
				continue;
			}
			request.getChannel().ifPresent(reference -> paths.resolve(reference).getChannel()
					.filter(channel -> !channel.getMessagesMapping().isPresent())
					.ifPresent(channel -> problems.add(WebSocketChannelPaths.prefix(request) + "a synchronous request needs its channel '"
							+ WebSocketChannelPaths.escape(reference) + "' to declare 'messages_mapping'.")));
		}
		return WebSocketChannelPaths.report(context, problems);
	}
}
