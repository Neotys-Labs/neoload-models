package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Case;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Fork;
import com.neotys.neoload.model.v3.project.userpath.If;
import com.neotys.neoload.model.v3.project.userpath.Loop;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.Switch;
import com.neotys.neoload.model.v3.project.userpath.TryCatch;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.WebPage;
import com.neotys.neoload.model.v3.project.userpath.WebSocketChannel;
import com.neotys.neoload.model.v3.project.userpath.WebSocketRequest;
import com.neotys.neoload.model.v3.project.userpath.While;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidatorContext;

/**
 * The websocket channels of a {@link UserPath} or of a shared element, indexed by their complete
 * path: the root, then each step name down to the channel's own name, joined with {@code >}. The
 * root is {@code init}, {@code actions} or {@code end} for a channel declared in a user path, and
 * {@code shared_elements>Name} for one declared inside the shared element {@code Name}. The
 * children of an {@code if}, a {@code try_catch} and the default branch of a {@code switch} are
 * designated by their YAML key ({@code then}, {@code else}, {@code try}, {@code catch},
 * {@code default}), a case by its name. A {@code shared_element} step is not a segment: a channel
 * inside a shared element is always designated from the shared element's own root.
 */
final class WebSocketChannelPaths {
	static final String SEPARATOR = ">";
	private static final List<String> USER_PATH_ROOTS = List.of(UserPath.INIT, UserPath.ACTIONS, UserPath.END);
	private static final String SHARED_ELEMENTS_ROOT = Project.SHARED_ELEMENTS;

	private final Map<List<String>, List<WebSocketChannel>> channels = new LinkedHashMap<>();

	private WebSocketChannelPaths() {
	}

	static WebSocketChannelPaths of(final UserPath userPath) {
		final WebSocketChannelPaths paths = new WebSocketChannelPaths();
		userPath.getInit().ifPresent(init -> paths.walkContainer(init, List.of(UserPath.INIT)));
		paths.walkContainer(userPath.getActions(), List.of(UserPath.ACTIONS));
		userPath.getEnd().ifPresent(end -> paths.walkContainer(end, List.of(UserPath.END)));
		return paths;
	}

	/** The channels declared inside one {@code shared_elements} entry, rooted at {@code shared_elements>Name}. */
	static WebSocketChannelPaths ofSharedElement(final Step sharedElement) {
		final WebSocketChannelPaths paths = new WebSocketChannelPaths();
		paths.walkStep(sharedElement, List.of(SHARED_ELEMENTS_ROOT, String.valueOf(sharedElement.getName())));
		return paths;
	}

	/** The WebSocketRequests of a user path or of a shared element where the channel attribute is present. */
	static List<WebSocketRequest> webSocketRequestsWithChannel(final Element element) {
		return element.flattened()
				.filter(WebSocketRequest.class::isInstance)
				.map(WebSocketRequest.class::cast)
				.filter(request -> request.getChannel().isPresent())
				.collect(Collectors.toList());
	}

	/** Whether the reference designates a channel declared inside a shared element. */
	static boolean isSharedElementReference(final String reference) {
		return reference.startsWith(SHARED_ELEMENTS_ROOT + SEPARATOR);
	}

	/** The name of the shared element a {@link #isSharedElementReference shared element reference} points into. */
	static String sharedElementName(final String reference) {
		return reference.split(SEPARATOR, -1)[1];
	}

	/**
	 * @return why the reference is not a channel path, whatever the channels it could designate
	 */
	static Optional<String> syntaxProblem(final String reference) {
		final List<String> segments = Arrays.asList(reference.split(SEPARATOR, -1));
		final boolean userPathRoot = USER_PATH_ROOTS.contains(segments.get(0)) && segments.size() >= 2;
		final boolean sharedElementRoot = SHARED_ELEMENTS_ROOT.equals(segments.get(0)) && segments.size() >= 3;
		if ((userPathRoot || sharedElementRoot) && !segments.contains("")) {
			return Optional.empty();
		}
		return Optional.of("'" + escape(reference) + "' is not a channel path: it must start with "
				+ String.join(", ", USER_PATH_ROOTS) + " or " + SHARED_ELEMENTS_ROOT + ">SharedElementName"
				+ " and name each step down to the channel, e.g. 'actions>my_transaction>my_channel'.");
	}

	/**
	 * @return the WebSocketChannel the reference designates, or why it designates none
	 */
	Resolution resolve(final String reference) {
		final Optional<String> syntaxProblem = syntaxProblem(reference);
		if (syntaxProblem.isPresent()) {
			return Resolution.problem(syntaxProblem.get());
		}
		final List<String> segments = Arrays.asList(reference.split(SEPARATOR, -1));
		final List<WebSocketChannel> found = channels.getOrDefault(segments, List.of());
		if (found.size() == 1) {
			return Resolution.channel(found.get(0));
		}
		if (found.size() > 1) {
			return Resolution.problem("'" + escape(reference) + "' designates " + found.size()
					+ " channels: two sibling steps on this path share a name.");
		}
		final String name = segments.get(segments.size() - 1);
		final List<String> candidates = channels.keySet().stream()
				.filter(path -> name.equals(path.get(path.size() - 1)))
				.map(path -> "'" + escape(String.join(SEPARATOR, path)) + "'")
				.distinct()
				.collect(Collectors.toList());
		return Resolution.problem("no websocket_channel at '" + escape(reference) + "'"
				+ (candidates.isEmpty() ? "." : "; use " + String.join(" or ", candidates) + "."));
	}

	private void walkContainer(final Container container, final List<String> path) {
		if (container != null) {
			walkSteps(container.getSteps(), path);
		}
	}

	private void walkSteps(final List<Step> steps, final List<String> parent) {
		if (steps == null) {
			return;
		}
		for (final Step step : steps) {
			walkStep(step, child(parent, step.getName()));
		}
	}

	/** Indexes the step, whose own path is {@code path}, and the channels it contains. */
	private void walkStep(final Step step, final List<String> path) {
		if (step instanceof WebSocketChannel) {
			channels.computeIfAbsent(path, key -> new ArrayList<>()).add((WebSocketChannel) step);
		} else if (step instanceof Container) {
			walkSteps(((Container) step).getSteps(), path);
		} else if (step instanceof Loop) {
			walkSteps(((Loop) step).getSteps(), path);
		} else if (step instanceof While) {
			walkSteps(((While) step).getSteps(), path);
		} else if (step instanceof Fork) {
			walkSteps(((Fork) step).getSteps(), path);
		} else if (step instanceof WebPage) {
			walkSteps(((WebPage) step).getSteps(), path);
		} else if (step instanceof If) {
			final If ifStep = (If) step;
			walkContainer(ifStep.getThen(), child(path, If.THEN));
			ifStep.getElse().ifPresent(elseContainer -> walkContainer(elseContainer, child(path, If.ELSE)));
		} else if (step instanceof TryCatch) {
			final TryCatch tryCatch = (TryCatch) step;
			tryCatch.getTry().ifPresent(tryContainer -> walkContainer(tryContainer, child(path, TryCatch.TRY)));
			tryCatch.getCatch().ifPresent(catchContainer -> walkContainer(catchContainer, child(path, TryCatch.CATCH)));
		} else if (step instanceof Switch) {
			final Switch switchStep = (Switch) step;
			for (final Case caseStep : switchStep.getCases()) {
				walkSteps(caseStep.getSteps(), child(path, caseStep.getName()));
			}
			walkContainer(switchStep.getDefault(), child(path, Switch.DEFAULT));
		}
	}

	private static List<String> child(final List<String> parent, final String segment) {
		return ImmutableList.<String>builder().addAll(parent).add(String.valueOf(segment)).build();
	}

	/** The request's name, as the prefix of each message about it. */
	static String prefix(final WebSocketRequest request) {
		return "websocket_request '" + escape(request.getName()) + "': ";
	}

	/**
	 * The request's name and where it is, for messages reported on the whole project, which carry
	 * no property path telling the user path or the shared element apart.
	 */
	static String prefix(final WebSocketRequest request, final String kind, final String ownerName) {
		return "websocket_request '" + escape(request.getName()) + "' in " + kind + " '" + escape(ownerName) + "': ";
	}

	/** The problem of a synchronous request whose channel cannot extract the correlation id of a response. */
	static String missingMessagesMapping(final String reference) {
		return "a synchronous request needs its channel '" + escape(reference) + "' to declare 'messages_mapping'.";
	}

	/**
	 * Messages are interpolated by the validation provider, which evaluates {@code ${...}} and
	 * {@code {...}}; user text therefore has to be escaped before it becomes part of one.
	 */
	static String escape(final String text) {
		return String.valueOf(text)
				.replace("\\", "\\\\")
				.replace("{", "\\{")
				.replace("}", "\\}")
				.replace("$", "\\$");
	}

	/** Reports each message as its own violation; returns whether there was none. */
	static boolean report(final ConstraintValidatorContext context, final List<String> messages) {
		if (messages.isEmpty()) {
			return true;
		}
		if (context != null) {
			context.disableDefaultConstraintViolation();
			messages.forEach(message -> context.buildConstraintViolationWithTemplate(message).addConstraintViolation());
		}
		return false;
	}

	static final class Resolution {
		private final WebSocketChannel channel;
		private final String problem;

		private Resolution(final WebSocketChannel channel, final String problem) {
			this.channel = channel;
			this.problem = problem;
		}

		static Resolution channel(final WebSocketChannel channel) {
			return new Resolution(channel, null);
		}

		static Resolution problem(final String problem) {
			return new Resolution(null, problem);
		}

		Optional<WebSocketChannel> getChannel() {
			return Optional.ofNullable(channel);
		}

		/** Already escaped, ready to be reported. */
		Optional<String> getProblem() {
			return Optional.ofNullable(problem);
		}
	}
}
