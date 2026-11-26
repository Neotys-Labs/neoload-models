package com.neotys.neoload.model.v3.project.userpath;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class HttpBasedRequestTest {
	private static final String MOVE_URL_QUERY_TO_PARAMETERS = "moveUrlQueryToParameters";

	/**
	 * The url query is moved to the parameters in a {@code @Value.Check} of each immutable request: Immutables cannot
	 * generate it from a method inherited from {@link HttpBasedRequest}, as the copy to return is specific to the immutable type.
	 * So every immutable type of this package that is an {@link HttpBasedRequest} must declare it.
	 */
	@Test
	public void everyImmutableHttpBasedRequestMovesTheUrlQuery() throws Exception {
		final List<Class<?>> immutableRequests = immutableHttpBasedRequests();
		assertThat(immutableRequests).contains(Request.class, SoapRequest.class, WebSocketChannel.class);

		final List<String> withoutCheck = new ArrayList<>();
		for (final Class<?> immutableRequest : immutableRequests) {
			if (Arrays.stream(immutableRequest.getDeclaredMethods()).noneMatch(method -> MOVE_URL_QUERY_TO_PARAMETERS.equals(method.getName()))) {
				withoutCheck.add(immutableRequest.getSimpleName());
			}
		}
		assertThat(withoutCheck).as("HttpBasedRequest types without a %s value check", MOVE_URL_QUERY_TO_PARAMETERS).isEmpty();
	}

	/** The abstract types of this package that have a generated {@code Immutable<Name>} class and extend {@link HttpBasedRequest}. */
	private static List<Class<?>> immutableHttpBasedRequests() throws ClassNotFoundException, URISyntaxException {
		final String prefix = "Immutable";
		final File packageDirectory = new File(HttpBasedRequest.class.getProtectionDomain().getCodeSource().getLocation().toURI())
				.toPath().resolve(HttpBasedRequest.class.getPackage().getName().replace('.', File.separatorChar)).toFile();
		final List<Class<?>> result = new ArrayList<>();
		for (final String fileName : packageDirectory.list((directory, name) -> name.startsWith(prefix) && name.endsWith(".class") && name.indexOf('$') < 0)) {
			final String abstractName = fileName.substring(prefix.length(), fileName.length() - ".class".length());
			final Class<?> abstractType = Class.forName(HttpBasedRequest.class.getPackage().getName() + '.' + abstractName);
			if (HttpBasedRequest.class.isAssignableFrom(abstractType)) {
				result.add(abstractType);
			}
		}
		return result;
	}
}
