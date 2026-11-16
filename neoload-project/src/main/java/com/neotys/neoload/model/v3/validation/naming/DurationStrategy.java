package com.neotys.neoload.model.v3.validation.naming;

import com.neotys.neoload.model.v3.project.userpath.Delay;
import javax.validation.Path.Node;
import org.hibernate.validator.internal.engine.path.NodeImpl;

/**
 * Names the duration of a {@link Delay} after its step key ({@code delay} or {@code think_time}) instead of {@code duration}.
 */
public final class DurationStrategy implements PropertyNamingStrategy {
	private static final PropertyNamingStrategy DEFAULT_STRATEGY = new SnakeCaseStrategy();

	@Override
	public String apply(final Node node) {
		final NodeImpl parent = ((NodeImpl) node).getParent();
		final Object parentValue = parent == null ? null : parent.getValue();
		if (parentValue instanceof Delay) {
			return ((Delay) parentValue).isThinkTime() ? Delay.DEFAULT_THINK_TIME_NAME : Delay.DEFAULT_NAME;
		}
		return DEFAULT_STRATEGY.apply(node);
	}
}
