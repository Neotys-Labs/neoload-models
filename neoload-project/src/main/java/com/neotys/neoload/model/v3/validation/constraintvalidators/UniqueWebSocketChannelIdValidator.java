package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.WebSocketChannel;
import com.neotys.neoload.model.v3.validation.constraints.UniqueWebSocketChannelIdCheck;

import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@code websocket_request} addresses its channel by id, so two channels in the same
 * {@link UserPath} may not share one. Blank ids are left to {@code RequiredCheck}.
 */
public final class UniqueWebSocketChannelIdValidator extends AbstractConstraintValidator<UniqueWebSocketChannelIdCheck, UserPath> {
	@Override
	public boolean isValid(final UserPath userPath, final ConstraintValidatorContext context) {
		if (userPath == null) {
			return true;
		}
		final List<String> ids = userPath.flattened()
				.filter(WebSocketChannel.class::isInstance)
				.map(element -> ((WebSocketChannel) element).getId())
				.filter(id -> id != null && !id.trim().isEmpty())
				.collect(Collectors.toList());

		return ids.size() == ids.stream().distinct().count();
	}
}
