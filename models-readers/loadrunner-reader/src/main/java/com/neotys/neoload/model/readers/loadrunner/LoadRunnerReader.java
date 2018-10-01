package com.neotys.neoload.model.readers.loadrunner;

import com.google.common.annotations.VisibleForTesting;
import com.neotys.neoload.model.ImmutableProject;
import com.neotys.neoload.model.Project;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.parsers.CPP14Lexer;
import com.neotys.neoload.model.parsers.CPP14Parser;
import com.neotys.neoload.model.readers.Reader;
import com.neotys.neoload.model.readers.loadrunner.customaction.ImmutableMappingMethod;
import com.neotys.neoload.model.readers.loadrunner.filereader.ParameterFileReader;
import com.neotys.neoload.model.readers.loadrunner.filereader.ProjectFileReader;
import com.neotys.neoload.model.readers.loadrunner.method.ContainerInFileMethod;
import com.neotys.neoload.model.readers.loadrunner.method.LoadRunnerMethod;
import com.neotys.neoload.model.readers.loadrunner.method.LoadRunnerSupportedMethods;
import com.neotys.neoload.model.repository.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.IOUtils;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoadRunnerReader extends Reader {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoadRunnerReader.class);

	private static final String PATTERN = "\"\\s*\\n\\s*\"";
	private static final String LOAD_PATTERN = "Load \"%s\"";

	private final EventListener eventListener;
	private final String projectName;
	private File currentScriptFolder = null;
	private final LoadRunnerSupportedMethods lrSupportedMethods;

	@VisibleForTesting
	protected Map<String, Server> currentProjectServers = new HashMap<>();
	private Map<String, Integer> nameIndexes = new HashMap<>();

	private List<File> dataFilesToCopy = new ArrayList<>();

	public LoadRunnerReader(final EventListener eventListener, final String folder, final String projectName,
							final String additionalCustomActionMappingContent) {
		super(folder);
		this.eventListener = eventListener;
		this.projectName = projectName;
		this.lrSupportedMethods = new LoadRunnerSupportedMethods(additionalCustomActionMappingContent);		
	}

	/**
	 * Create an HashMap with keys as the NeoLoad destination folder of the Future NeoLoad Project.
	 * The set returned by each key is a list of the file to copy into the folder reference by the key
	 * @return the hashMap
	 */
	public Map<String, List<File>> getFileToCopy() {
		Map<String, List<File>> returnedMap = new HashMap<>();
		returnedMap.put("variables", dataFilesToCopy);
		return returnedMap;
	}

	private List<File> getProjectFolders() {
		final List<File> projectFolders = new ArrayList<>();
		final File rootFolder = new File(folder);
		if (!rootFolder.isDirectory()) {
			LOGGER.error("Source parameter is not a folder. Please review your parameters and try again.");
			throw new IllegalArgumentException("Source path is not a folder");
		}
		if (ProjectFileReader.containsUSRFile(rootFolder)) {
			projectFolders.add(rootFolder);
		} else {
			projectFolders.addAll(
					Arrays.stream(rootFolder.listFiles()).filter(ProjectFileReader::containsUSRFile).collect(Collectors.toList()));
		}
		return projectFolders;
	}

	@Override
	public synchronized Project read() {
		try {

			final ImmutableProject.Builder projectBuilder = ImmutableProject.builder().name(this.projectName);
			final List<File> projectFolders = getProjectFolders();

			final int totalScriptNumber = projectFolders.size();
			eventListener.startReadingScripts(totalScriptNumber);

			if (projectFolders.isEmpty()) {
				throw new IllegalStateException("No Load Runner project found.");
			}

			for (final File projectFolder : projectFolders) {
				readScript(projectBuilder, projectFolder);
			}
			projectBuilder.servers(currentProjectServers.values());
			return projectBuilder.build();
		} finally {
			eventListener.endReadingScripts();
		}
	}

	private void readScript(final ImmutableProject.Builder projectBuilder, final File projectFolder) {
		try {
			currentScriptFolder = projectFolder;
			eventListener.startScript(currentScriptFolder.getName());
			final ProjectFileReader projectFileReader = new ProjectFileReader(this, eventListener, projectFolder);
			final Map<String, String> actionsMap = projectFileReader.getAllActionsMap();
			if (actionsMap.isEmpty()) {
				LOGGER.error("No action in the map. Ignore the script.");
				return;
			}
			final ParameterFileReader parameterFileReader = new ParameterFileReader(this, projectFileReader, projectFolder);

			final ImmutableUserPath.Builder userPathBuilder = ImmutableUserPath.builder();

			final boolean hasAction = manageActions(projectFolder, projectFileReader, actionsMap, projectBuilder, userPathBuilder);

			final UserPath userPath = userPathBuilder.name(projectFileReader.getVirtualUserName()).build();

			if (!hasAction) {
				LOGGER.error("No Init / Actions / End. Ignore the script.");
				return;
			}
			projectBuilder.addAllVariables(parameterFileReader.getAllVariables()).addUserPaths(userPath);
		} finally {
			eventListener.endScript();
		}
	}

	private boolean manageActions(final File projectFolder,
								  final ProjectFileReader projectFileReader,
								  final Map<String, String> actionsMap,
								  final ImmutableProject.Builder projectBuilder,
								  final ImmutableUserPath.Builder userPathBuilder) {

		final Map<String, MutableContainer> containersByName = actionsMap.keySet().stream()
				.collect(Collectors.toMap(Function.identity(), MutableContainer::new));

		lrSupportedMethods.setContainerInFileMethod(new ContainerInFileMethod(containersByName));

		final AtomicBoolean asAtLeastOneAction = new AtomicBoolean(false);
		actionsMap.forEach(
				(actionName, actionFile) -> {
					if (actionFile.endsWith(".c")) {
						final Path pathAction = Paths.get(projectFolder.getAbsolutePath(), actionFile);
						final Charset charset = guessCharset(pathAction.toFile());

						final MutableContainer containerBuilder = containersByName.get(actionName);
						try (FileInputStream targetStream = new FileInputStream(pathAction.toFile())) {
							parseCppFile(containerBuilder, projectFileReader.getLeftBrace(), projectFileReader.getRightBrace(),
									targetStream, charset);
							LOGGER.info(String.format(LOAD_PATTERN, pathAction));
							eventListener.readSupportedAction(actionFile);
							asAtLeastOneAction.set(true);
						} catch (IOException | RecognitionException e) {
							eventListener.readUnsupportedAction(actionFile);
							LOGGER.error("Error reading action file", e);
						}
					} else {
						eventListener.readUnsupportedAction(actionFile);
					}
				});

		lrSupportedMethods.setContainerInFileMethod(null);

		manageInitActionsEnd(projectFileReader, containersByName, projectBuilder, userPathBuilder);

		return asAtLeastOneAction.get();
	}

	private void manageInitActionsEnd(final ProjectFileReader projectFileReader,
									  final Map<String, MutableContainer> containersByName,
									  final ImmutableProject.Builder projectBuilder,
									  final ImmutableUserPath.Builder userPathBuilder) {
		final ImmutableContainer.Builder initContainerBuilder = ImmutableContainer.builder().name("Init");
		projectFileReader.getInits().stream().map(containersByName::get).filter(Objects::nonNull).forEach(initContainerBuilder::addChilds);

		final ImmutableContainer.Builder actionsContainerBuilder = ImmutableContainer.builder().name("Actions");
		if (projectFileReader.getActions().isEmpty()) {
			containersByName.values().stream()
					.filter(c -> !projectFileReader.getInits().contains(c.getName()) && !projectFileReader.getEnds().contains(c.getName()))
					.forEach(actionsContainerBuilder::addChilds);
		} else {
			projectFileReader.getActions().stream().map(containersByName::get).filter(Objects::nonNull).forEach(actionsContainerBuilder::addChilds);
		}

		final ImmutableContainer.Builder endContainerBuilder = ImmutableContainer.builder().name("End");
		projectFileReader.getEnds().stream().map(containersByName::get).filter(Objects::nonNull).forEach(endContainerBuilder::addChilds);

		final ImmutableContainer init = initContainerBuilder.build();
		final ImmutableContainer actions = actionsContainerBuilder.build();
		final ImmutableContainer end = endContainerBuilder.build();

		// when a container appears more than once in User Path, we add it in shared containers
		containersByName.values().forEach(container ->
		{
			if (Stream.concat(Stream.concat(init.flattened(), actions.flattened()), end.flattened()).filter(container::equals).count() > 1) {
				container.setShared(true);
				projectBuilder.addSharedElements(container);
			}
		});

		// if init, actions or end has only child container not shared then we remove it.
		if(hasOneNotSharedChild(init)){
			final Container childContainer = (Container) init.getChilds().get(0);
			userPathBuilder.initContainer(ImmutableContainer.builder().name("Init").addAllChilds(childContainer.getChilds()).build());
		} else{
			userPathBuilder.initContainer(init);
		}
		if(hasOneNotSharedChild(actions)){
			final Container childContainer = (Container) actions.getChilds().get(0);
			userPathBuilder.actionsContainer(ImmutableContainer.builder().name("Actions").addAllChilds(childContainer.getChilds()).build());
		} else{
			userPathBuilder.actionsContainer(actions);
		}
		if(hasOneNotSharedChild(end)){
			final Container childContainer = (Container) end.getChilds().get(0);
			userPathBuilder.endContainer(ImmutableContainer.builder().name("End").addAllChilds(childContainer.getChilds()).build());
		} else{
			userPathBuilder.endContainer(end);
		}
	}

	private static boolean hasOneNotSharedChild(final Container container) {
		if (container.getChilds().size() != 1) {
			return false;
		}
		final Element child = container.getChilds().get(0);
		return child instanceof Container && !((Container) child).isShared();
	}

	private static Charset guessCharset(final File file) {
		try (FileInputStream targetStream = new FileInputStream(file)) {
		  return Charset.forName(guessEncoding(targetStream));
		} catch(final Exception e){
			return Charset.defaultCharset();
		}
	}

	private static String guessEncoding(InputStream is) throws IOException {
		UniversalDetector detector = new UniversalDetector(null);

		int numberRead;
		final byte[] buffer = new byte[1024];
		while ((numberRead = is.read(buffer)) > 0 && !detector.isDone()) {
			detector.handleData(buffer, 0, numberRead);
		}
		detector.dataEnd();

		String encoding = detector.getDetectedCharset();
		detector.reset();
		if (encoding != null) {
			return encoding;
		} 
		throw new IOException("Not detected");		
	}

	/**
	 * Check if a identical server already exist, if exist the function return it
	 * If not but there exist a server with the same "uid", we create a new server with a different uid.
	 * @return the unique server to use
	 */
	public Server getOrAddServerIfNotExist(final String name, final String host, final String port, final Optional<String> scheme) {
		// Search if exact same server exists
		for (final Server server : currentProjectServers.values()) {
			if (server.getHost().equals(host)
					&& server.getPort().equals(port)
					&& server.getScheme().equals(scheme)
					&& server.getName().equals(name)) {
				return server;
			}
		}
		final Server server = ImmutableServer.builder().name(
				currentProjectServers.get(name) == null ? name : findUniqueName(name, currentProjectServers.keySet())).host(host).port(port).scheme(
						scheme).build();
		currentProjectServers.put(server.getName(), server);
		return server;
	}

	private static String findUniqueName(final String name, final Set<String> keySet) {
		int i = 0;
		String uniqueName = name;
		while (keySet.contains(uniqueName)) {
			uniqueName = name + "_" + (++i);
		}
		return uniqueName;
	}

	@VisibleForTesting
	public void parseCppFile(final MutableContainer mutableContainer, final String leftBrace, final String rightBrace,
								  final InputStream stream, final Charset charset) throws IOException {

		CPP14Lexer lexer = new CPP14Lexer(loadAndCorrectGrammarFromLR(stream, charset));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CPP14Parser parser = new CPP14Parser(tokens);
		ParseTree tree = parser.declaration();
		if (tree instanceof CPP14Parser.DeclarationContext) {
			final RecognitionException exception = ((CPP14Parser.DeclarationContext) tree).exception;
			if (exception != null) {
				throw exception;
			}
		}
		LoadRunnerVUVisitor visitor = new LoadRunnerVUVisitor(this, leftBrace, rightBrace, mutableContainer);
		visitor.visit(tree);
		// end unended container
		while (visitor.getCurrentContainers().size() > 1) {
			final Container container = LoadRunnerVUVisitor.toContainer(visitor.getCurrentContainers().remove(visitor.getCurrentContainers().size() - 1));
			visitor.addInContainers(container);
		}
	}

	@VisibleForTesting
	protected static CharStream loadAndCorrectGrammarFromLR(final InputStream stream, final Charset charset) throws IOException {		
		StringWriter writer = new StringWriter();		
		IOUtils.copy(stream, writer, charset);
		String loadedFileAsString = writer.toString();
		return CharStreams.fromString(loadedFileAsString.replaceAll(PATTERN, ""));
	}

	public void addDataFilesToCopy(final File file) {
		dataFilesToCopy.add(file);
	}

	public void removeDataFilesToCopyIf(final Predicate<File> filter) {
		dataFilesToCopy.removeIf(filter);
	}

	public void clear() {
		currentProjectServers.clear();
		nameIndexes.clear();
	}

	public EventListener getEventListener() {
		return eventListener;
	}

	public String getCurrentScriptName() {
		return currentScriptFolder != null ? currentScriptFolder.getName() : null;
	}

	public Path getCurrentScriptFolder() {
		return currentScriptFolder != null ? currentScriptFolder.toPath() : Paths.get(folder);
	}

	public Path getCurrentScriptDataFolder() {
		return getCurrentScriptFolder().resolve("data");
	}

	/**
	 * Build or get the server from the list of server already build from the passed url
	 * @param url extracting the server from this url
	 * @return the corresponding server
	 * @throws MalformedURLException
	 */
	public Server getServer(final String url) throws MalformedURLException {
		return getServer(new URL(url));
	}

	/**
	* Build or get the server from the list of server already build from the passed url
	* @param url extracting the server from this url
	* @return the corresponding server
	*/
	public Server getServer(final URL url) {
		return getOrAddServerIfNotExist(
				MethodUtils.normalizeName(url.getHost()),
				url.getHost(),
				String.valueOf(url.getPort() != -1 ? url.getPort() : url.getDefaultPort()),
				Optional.of(url.getProtocol()));
	}

	public LoadRunnerMethod getLrSupportedMethod(final String methodName) {
		return lrSupportedMethods.get(methodName);
	}

	public ImmutableMappingMethod getCustomActionMappingMethod(String methodName) {
		return lrSupportedMethods.getCustomActionMappingMethod(methodName);
	}
}
