package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.WebSocketPushMessage;
import com.neotys.neoload.model.v3.validation.constraints.WebSocketPushMessageCheck;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidatorContext;

/**
 * A push message is conditional when it declares {@code conditions}, and then needs at least
 * one, and a fallback when it declares none. {@code match} only combines conditions, so a
 * fallback cannot have one.
 */
public final class WebSocketPushMessageValidator extends AbstractConstraintValidator<WebSocketPushMessageCheck, WebSocketPushMessage> {
	@Override
	public boolean isValid(final WebSocketPushMessage pushMessage, final ConstraintValidatorContext context) {
		if (pushMessage == null) {
			return true;
		}
		final List<String> problems = new ArrayList<>();
		if (pushMessage.getConditions().map(List::isEmpty).orElse(false)) {
			problems.add("'conditions' must hold at least one condition; a push message without 'conditions' is the fallback.");
		}
		if (pushMessage.isFallback() && pushMessage.getMatch().isPresent()) {
			problems.add("'match' is only used with 'conditions': a push message without 'conditions' is the fallback.");
		}
		final String prefix = "push message '" + WebSocketChannelPaths.escape(pushMessage.getName()) + "': ";
		return WebSocketChannelPaths.report(context, problems.stream().map(problem -> prefix + problem).collect(Collectors.toList()));
	}
}
