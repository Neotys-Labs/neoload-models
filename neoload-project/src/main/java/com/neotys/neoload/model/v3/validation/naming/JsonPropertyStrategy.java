package com.neotys.neoload.model.v3.validation.naming;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import javax.validation.Path.Node;
import org.hibernate.validator.internal.engine.path.NodeImpl;

/**
 * Names a property after the key of the {@link JsonProperty} getter of its model interface, as written in the YAML,
 * instead of the snake case of its Java name. Falls back to snake case when the getter has no key. The Immutables
 * generated classes are skipped, their keys being the Java names.
 */
public final class JsonPropertyStrategy implements PropertyNamingStrategy {
	private static final PropertyNamingStrategy DEFAULT_STRATEGY = new SnakeCaseStrategy();

	@Override
	public String apply(final Node node) {
		return jsonKey(node)
				.map(key -> node.toString().replace(node.getName(), key))
				.orElseGet(() -> DEFAULT_STRATEGY.apply(node));
	}

	private static Optional<String> jsonKey(final Node node) {
		final NodeImpl parent = ((NodeImpl) node).getParent();
		final Object owner = parent == null ? null : parent.getValue();
		if (owner == null) {
			return Optional.empty();
		}
		final Set<String> getters = getterNames(node.getName());
		return TypeToken.of(owner.getClass()).getTypes().interfaces().rawTypes().stream()
				.flatMap(type -> Arrays.stream(type.getDeclaredMethods()))
				.filter(method -> method.getParameterCount() == 0 && getters.contains(method.getName()))
				.map(JsonPropertyStrategy::jsonKey)
				.flatMap(Optional::stream)
				.findFirst();
	}

	private static Optional<String> jsonKey(final Method getter) {
		final JsonProperty property = getter.getAnnotation(JsonProperty.class);
		return property == null || Strings.isNullOrEmpty(property.value()) ? Optional.empty() : Optional.of(property.value());
	}

	private static Set<String> getterNames(final String property) {
		final String capitalized = Character.toUpperCase(property.charAt(0)) + property.substring(1);
		return Set.of("get" + capitalized, "is" + capitalized);
	}
}
