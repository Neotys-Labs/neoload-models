package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.SharedElementRef;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.WebSocketChannel;
import com.neotys.neoload.model.v3.project.userpath.WebSocketRequest;
import com.neotys.neoload.model.v3.validation.constraints.WebSocketSharedChannelReferenceCheck;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidatorContext;

/**
 * The websocket channel references that need the project's {@code shared_elements}, which a
 * {@link UserPath} cannot see: references into a shared element ({@code shared_elements>Name>...}),
 * and every reference made by a request inside a shared element.
 *
 * <p>A request runs in each user path that uses its shared element, directly or through other
 * shared elements, and a virtual user only holds the connections it opened itself. So a
 * {@code shared_elements>Name>...} channel must be opened by every user path running the request,
 * and an {@code init}/{@code actions}/{@code end} reference from inside a shared element must
 * designate the same channel in each of them, since NeoLoad stores a single channel per request.</p>
 *
 * <p>As-code validation sees one file at a time, so nothing is reported about a shared element
 * that the file does not declare: it may be declared in another file or in the {@code .nlp}.</p>
 */
public final class WebSocketSharedChannelReferenceValidator extends AbstractConstraintValidator<WebSocketSharedChannelReferenceCheck, Project> {
	private static final String USER_PATH = "user path";
	private static final String SHARED_ELEMENT = "shared element";

	@Override
	public boolean isValid(final Project project, final ConstraintValidatorContext context) {
		if (project == null) {
			return true;
		}
		final SharedElementUsage usage = new SharedElementUsage(project);
		final List<String> problems = new ArrayList<>();
		for (final UserPath userPath : project.getUserPaths()) {
			for (final WebSocketRequest request : WebSocketChannelPaths.webSocketRequestsWithChannel(userPath)) {
				final String prefix = WebSocketChannelPaths.prefix(request, USER_PATH, userPath.getName());
				request.getChannel()
						.filter(WebSocketChannelPaths::isSharedElementReference)
						.ifPresent(reference -> checkSharedElementReference(request, reference, List.of(userPath), usage, prefix, problems));
			}
		}
		for (final Step sharedElement : project.getSharedElements()) {
			final List<UserPath> users = usage.usersOf(sharedElement.getName());
			for (final WebSocketRequest request : WebSocketChannelPaths.webSocketRequestsWithChannel(sharedElement)) {
				final String prefix = WebSocketChannelPaths.prefix(request, SHARED_ELEMENT, sharedElement.getName());
				request.getChannel().ifPresent(reference -> {
					if (WebSocketChannelPaths.isSharedElementReference(reference)) {
						checkSharedElementReference(request, reference, users, usage, prefix, problems);
					} else {
						checkUserPathReference(request, reference, users, usage, prefix, problems);
					}
				});
			}
		}
		return WebSocketChannelPaths.report(context, problems);
	}

	/** A {@code shared_elements>Name>...} reference, from a request run by each of {@code users}. */
	private static void checkSharedElementReference(final WebSocketRequest request, final String reference, final List<UserPath> users,
			final SharedElementUsage usage, final String prefix, final List<String> problems) {
		final Optional<String> syntaxProblem = WebSocketChannelPaths.syntaxProblem(reference);
		if (syntaxProblem.isPresent()) {
			problems.add(prefix + syntaxProblem.get());
			return;
		}
		final String sharedElementName = WebSocketChannelPaths.sharedElementName(reference);
		final Optional<WebSocketChannelPaths> paths = usage.pathsOf(sharedElementName);
		if (!paths.isPresent()) {
			// Declared in another file or in the .nlp: resolved when the files are merged
			return;
		}
		final WebSocketChannelPaths.Resolution resolution = paths.get().resolve(reference);
		resolution.getProblem().ifPresent(problem -> problems.add(prefix + problem));
		if (!resolution.getChannel().isPresent()) {
			return;
		}
		for (final UserPath userPath : users) {
			if (!usage.uses(userPath, sharedElementName) && !usage.usesUndeclared(userPath)) {
				problems.add(prefix + "shared element '" + WebSocketChannelPaths.escape(sharedElementName) + "' is not used in user path '"
						+ WebSocketChannelPaths.escape(userPath.getName()) + "', so this channel is never opened there.");
			}
		}
		checkMessagesMapping(request, reference, resolution.getChannel().get(), prefix, problems);
	}

	/**
	 * An {@code init}/{@code actions}/{@code end} reference from a request inside a shared element:
	 * it is resolved in each user path running the request, and must designate the same channel.
	 */
	private static void checkUserPathReference(final WebSocketRequest request, final String reference, final List<UserPath> users,
			final SharedElementUsage usage, final String prefix, final List<String> problems) {
		final Optional<String> syntaxProblem = WebSocketChannelPaths.syntaxProblem(reference);
		if (syntaxProblem.isPresent()) {
			problems.add(prefix + syntaxProblem.get());
			return;
		}
		// The distinct channels the user paths resolve the reference to, compared by identity (two
		// channels of different user paths may be equal in content) and kept in document order
		final List<WebSocketChannel> channels = new ArrayList<>();
		final List<String> resolvingUserPaths = new ArrayList<>();
		for (final UserPath userPath : users) {
			final WebSocketChannelPaths.Resolution resolution = usage.pathsOf(userPath).resolve(reference);
			resolution.getProblem().ifPresent(problem -> problems.add(prefix + "in user path '"
					+ WebSocketChannelPaths.escape(userPath.getName()) + "', " + problem));
			resolution.getChannel().ifPresent(channel -> {
				resolvingUserPaths.add(WebSocketChannelPaths.escape(userPath.getName()));
				if (channels.stream().noneMatch(known -> known == channel)) {
					channels.add(channel);
				}
			});
		}
		if (channels.size() > 1) {
			problems.add(prefix + "'" + WebSocketChannelPaths.escape(reference) + "' designates a different channel in each user path using "
					+ "this shared element (" + String.join(", ", resolvingUserPaths) + "), but a request can only have one channel.");
		} else if (channels.size() == 1) {
			checkMessagesMapping(request, reference, channels.get(0), prefix, problems);
		}
	}

	private static void checkMessagesMapping(final WebSocketRequest request, final String reference, final WebSocketChannel channel,
			final String prefix, final List<String> problems) {
		if (request.isSynchronous() && channel.getMessagesMapping().isEmpty()) {
			problems.add(prefix + WebSocketChannelPaths.missingMessagesMapping(reference));
		}
	}

	/** Which shared elements each user path uses, directly or through other shared elements. */
	private static final class SharedElementUsage {
		private final List<UserPath> userPaths;
		private final Map<String, Step> sharedElements = new LinkedHashMap<>();
		private final Map<String, WebSocketChannelPaths> sharedElementPaths = new LinkedHashMap<>();
		private final Map<UserPath, Set<String>> usedByUserPath = new IdentityHashMap<>();
		private final Map<UserPath, WebSocketChannelPaths> userPathPaths = new IdentityHashMap<>();

		SharedElementUsage(final Project project) {
			this.userPaths = project.getUserPaths();
			for (final Step sharedElement : project.getSharedElements()) {
				if (sharedElement.getName() != null) {
					// Duplicate names are reported by UniqueElementNameCheck; the first one is kept here
					sharedElements.putIfAbsent(sharedElement.getName(), sharedElement);
				}
			}
		}

		/** The channels of a shared element declared in this file. */
		Optional<WebSocketChannelPaths> pathsOf(final String sharedElementName) {
			final Step sharedElement = sharedElements.get(sharedElementName);
			if (sharedElement == null) {
				return Optional.empty();
			}
			return Optional.of(sharedElementPaths.computeIfAbsent(sharedElementName, name -> WebSocketChannelPaths.ofSharedElement(sharedElement)));
		}

		WebSocketChannelPaths pathsOf(final UserPath userPath) {
			return userPathPaths.computeIfAbsent(userPath, WebSocketChannelPaths::of);
		}

		boolean uses(final UserPath userPath, final String sharedElementName) {
			return used(userPath).contains(sharedElementName);
		}

		/** Whether the user path uses a shared element this file does not declare, which may itself use any other one. */
		boolean usesUndeclared(final UserPath userPath) {
			return used(userPath).stream().anyMatch(name -> !sharedElements.containsKey(name));
		}

		/** The user paths of this file that run the shared element. */
		List<UserPath> usersOf(final String sharedElementName) {
			return userPaths.stream().filter(userPath -> uses(userPath, sharedElementName)).collect(Collectors.toList());
		}

		private Set<String> used(final UserPath userPath) {
			return usedByUserPath.computeIfAbsent(userPath, this::transitivelyUsed);
		}

		private Set<String> transitivelyUsed(final Element element) {
			final Set<String> used = new LinkedHashSet<>();
			final Deque<String> toVisit = new ArrayDeque<>(directlyUsed(element));
			while (!toVisit.isEmpty()) {
				final String name = toVisit.pop();
				// A reference cycle is reported by SharedElementCycleCheck; the visited set keeps this walk finite
				if (used.add(name) && sharedElements.containsKey(name)) {
					toVisit.addAll(directlyUsed(sharedElements.get(name)));
				}
			}
			return Collections.unmodifiableSet(used);
		}

		private static List<String> directlyUsed(final Element element) {
			return element.flattened()
					.filter(SharedElementRef.class::isInstance)
					.map(Element::getName)
					.collect(Collectors.toList());
		}
	}
}
