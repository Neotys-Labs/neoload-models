package com.neotys.neoload.model.readers.loadrunner;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.any23.encoding.TikaEncodingDetector;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Iterables;
import com.neotys.neoload.model.ImmutableProject;
import com.neotys.neoload.model.Project;
import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.parsers.CPP14Lexer;
import com.neotys.neoload.model.parsers.CPP14Parser;
import com.neotys.neoload.model.readers.Reader;
import com.neotys.neoload.model.readers.loadrunner.filereader.ParameterFileReader;
import com.neotys.neoload.model.readers.loadrunner.filereader.ProjectFileReader;
import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.repository.ImmutableContainer;
import com.neotys.neoload.model.repository.ImmutableServer;
import com.neotys.neoload.model.repository.ImmutableUserPath;
import com.neotys.neoload.model.repository.Server;

public class LoadRunnerReader extends Reader {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoadRunnerReader.class);

	private static final String PATTERN = "\"\\s*\\n\\s*\"";
	private static final String LOAD_PATTERN = "Load \"%s\"";
	private static final Container DEFAULT_INIT_CONTAINER = ImmutableContainer.builder().name("Init").build();
	private static final Container DEFAULT_END_CONTAINER = ImmutableContainer.builder().name("End").build();

	private final EventListener eventListener;
	private final String projectName;
	private File currentScriptFolder = null;

	@VisibleForTesting
	protected Map<String, Server> currentProjectServers = new HashMap<>();
	private Map<String, Integer> nameIndexes = new HashMap<>();

	private List<File> dataFilesToCopy = new ArrayList<>();

	public LoadRunnerReader(final EventListener eventListener, final String folder, final String projectName) {
		super(folder);
		this.eventListener = eventListener;
		this.projectName = projectName;
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
			final Map<String, String> actionsMap = projectFileReader.getActions();
			if (actionsMap.isEmpty()) {
				LOGGER.error("No action in the map. Ignore the script.");
				return;
			}
			final ParameterFileReader parameterFileReader = new ParameterFileReader(this, projectFileReader, projectFolder);

			final ImmutableUserPath.Builder userPathBuilder = ImmutableUserPath.builder();

			final String vuserInitFile = actionsMap.remove(Iterables.getFirst(actionsMap.keySet(), "vuser_init"));
			final boolean hasInit = manageInit(projectFolder, projectFileReader, userPathBuilder, vuserInitFile);
			final String vuserEndFile = actionsMap.remove(Iterables.getLast(actionsMap.keySet(), "vuser_end"));
			final ImmutableContainer.Builder actionsContainerBuilder = ImmutableContainer.builder().name("Actions");
			final boolean hasAction = manageAction(projectFolder, projectFileReader, actionsMap, userPathBuilder, actionsContainerBuilder);
			final boolean hasEnd = manageEnd(projectFolder, projectFileReader, userPathBuilder, vuserEndFile);

			if (!hasInit && !hasAction && !hasEnd) {
				LOGGER.error("No Init / Actions / End. Ignore the script.");
				return;
			}
			projectBuilder.addAllVariables(parameterFileReader.getAllVariables()).addUserPaths(
					userPathBuilder.name(projectFileReader.getVirtualUserName()).build());
		} finally {
			eventListener.endScript();
		}
	}

	private boolean manageEnd(final File projectFolder, final ProjectFileReader projectFileReader, final ImmutableUserPath.Builder userPathBuilder,
			final String vuserEndFile) {
		if (vuserEndFile != null) {
			if (vuserEndFile.endsWith(".c")) {
				Path pathUserEnd = Paths.get(projectFolder.getAbsolutePath(), vuserEndFile);
				final Charset charset = guessCharset(pathUserEnd.toFile());
				try (FileInputStream targetStream = new FileInputStream(pathUserEnd.toFile())) {
					userPathBuilder.endContainer(Optional.ofNullable(parseCppFile(projectFileReader.getLeftBrace(),
							projectFileReader.getRightBrace(), targetStream, "End", charset)).orElse(DEFAULT_END_CONTAINER));
					LOGGER.info(String.format(LOAD_PATTERN, pathUserEnd));
					eventListener.readSupportedAction(vuserEndFile);
					return true;
				} catch (IOException | RecognitionException e) {
					eventListener.readUnsupportedAction(vuserEndFile);
					LOGGER.error("Error reading end file", e);
				}
			} else {
				eventListener.readUnsupportedAction(vuserEndFile);
			}
		}
		return false;
	}

	private boolean manageAction(final File projectFolder, final ProjectFileReader projectFileReader, final Map<String, String> actionsMap,
			final ImmutableUserPath.Builder userPathBuilder, final ImmutableContainer.Builder actionsContainerBuilder) {
		final AtomicBoolean asAtLeastOneAction = new AtomicBoolean(false);
		actionsMap.forEach(
				(actionName, actionFile) -> {
					if (actionFile.endsWith(".c")) {
						final Path pathAction = Paths.get(projectFolder.getAbsolutePath(), actionFile);
						final Charset charset = guessCharset(pathAction.toFile());
						try (FileInputStream targetStream = new FileInputStream(pathAction.toFile())) {
							final Container container = parseCppFile(projectFileReader.getLeftBrace(), projectFileReader.getRightBrace(),
									targetStream, actionName, charset);
							actionsContainerBuilder.addChilds(container);
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

		userPathBuilder.actionsContainer(actionsContainerBuilder.build());
		return asAtLeastOneAction.get();
	}

	private boolean manageInit(final File projectFolder, final ProjectFileReader projectFileReader, final ImmutableUserPath.Builder userPathBuilder,
			final String vuserInitFile) {
		if (vuserInitFile != null) {
			if (vuserInitFile.endsWith(".c")) {
				final Path pathUserInit = Paths.get(projectFolder.getAbsolutePath(), vuserInitFile);
				final Charset charset = guessCharset(pathUserInit.toFile());
				try (FileInputStream targetStream = new FileInputStream(pathUserInit.toFile())) {
					userPathBuilder.initContainer(Optional.ofNullable(parseCppFile(projectFileReader.getLeftBrace(),
							projectFileReader.getRightBrace(), targetStream, "Init", charset)).orElse(DEFAULT_INIT_CONTAINER));
					LOGGER.info(String.format(LOAD_PATTERN, pathUserInit));
					eventListener.readSupportedAction(vuserInitFile);
					return true;
				} catch (IOException | RecognitionException e) {
					eventListener.readUnsupportedAction(vuserInitFile);
					LOGGER.error("Error reading init file", e);
				}
			} else {
				eventListener.readUnsupportedAction(vuserInitFile);
			}
		}
		return false;
	}
	
	public static Charset guessCharset(final File file) {
		try (FileInputStream targetStream = new FileInputStream(file)) {
		  return Charset.forName(new TikaEncodingDetector().guessEncoding(targetStream));    
		} catch(final Exception e){
			return Charset.defaultCharset();
		}
	}

	/**
	 * Check if a identical server already exist, if exist the function return it
	 * If not but there exist a server with the same "uid", we create a new server with a different uid.
	 * @param newServer to test
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
	protected Container parseCppFile(final String leftBrace, final String rightBrace, final InputStream stream,
			final String name, final Charset charset) throws IOException {

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
		LoadRunnerVUVisitor visitor = new LoadRunnerVUVisitor(this, leftBrace, rightBrace, name);
		Container container = (Container) visitor.visit(tree);
		// end unended container
		while (visitor.getCurrentContainers().size() > 1) {
			container = visitor.getCurrentContainers().remove(visitor.getCurrentContainers().size() - 1).build();
			final Container parent = visitor.getCurrentContainers().get(visitor.getCurrentContainers().size() - 1).build();
			container = (Container) LoadRunnerVUVisitor.setUniqueNameInContainer(container, parent);
			container = visitor.getCurrentContainers().get(visitor.getCurrentContainers().size() - 1).addChilds(container).build();
		}
		return container;

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
}
