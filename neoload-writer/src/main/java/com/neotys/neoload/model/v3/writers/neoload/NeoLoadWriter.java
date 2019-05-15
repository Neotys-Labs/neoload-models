package com.neotys.neoload.model.v3.writers.neoload;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.ImmutableProject;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.sla.SlaProfile;
import com.neotys.neoload.model.v3.project.variable.FileVariable;
import com.neotys.neoload.model.v3.project.variable.ImmutableFileVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import com.neotys.neoload.model.v3.writers.neoload.settings.ProjectSettingsWriter;
import com.neotys.neoload.model.v3.writers.neoload.sla.SlaProfileWriter;
import com.neotys.neoload.model.writers.neoload.FileVariableWriter;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Character.isDigit;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Optional.ofNullable;

public class NeoLoadWriter {

	private static Logger logger = LoggerFactory.getLogger(NeoLoadWriter.class);

	public static final String DEFAULT_PROJECT_VERSION = "6.5";
	public static final String DEFAULT_PRODUCT_VERSION = "6.7.0";

	private static final String CONFIG_ZIP = "config.zip";
	private static final String CONFIG_FOLDER = "config";

	private static final String SLA_FOLDER = "sla_profiles";
	
	public static final String RECORDED_REQUESTS_FOLDER = "recorded-requests";
	public static final String RECORDED_RESPONSE_FOLDER = "recorded-responses";

	public enum ConfigFiles {
		REPOSITORY("repository.xml"),
		SCENARIO("scenario.xml"),
		SETTINGS("settings.xml");

		private String fileName;

		ConfigFiles(String fileName) {
			this.fileName = fileName;
		}
		
		public String getFileName() {
			return fileName;
		}
	}

	private Project project;
	private final String nlProjectFolder;
	private Map<String, List<File>> fileToCopy;

	public NeoLoadWriter(final Project project, final String nlProjectFolder, final Map<String, List<File>> fileToCopy) {
		this.project = project;
		this.nlProjectFolder = nlProjectFolder;
		this.fileToCopy = fileToCopy;
	}

	/**
	 * write the project with default project version and default product version.
	 */
	public void write(final boolean zipConfig) {
		write(zipConfig, DEFAULT_PROJECT_VERSION, DEFAULT_PRODUCT_VERSION);
	}

	public void write(final boolean zipConfig, @Nullable final String projectVersion, @Nullable final String productVersion) {
		try {
			final File f = new File(nlProjectFolder);
			if (!f.exists()) {
				logger.info("Output folder does not exist, creating it.");
				Files.createDirectories(Paths.get(nlProjectFolder));
			} else if (f.isFile()) {
				logger.error("The destination is not a directory, migration aborted.");
				throw new IllegalArgumentException("Cannot write the project, The destination is not a directory.");
			}
			writeXML(zipConfig);

			final String safeProjectVersion = ofNullable(validateVersion(projectVersion, 2)).orElse(DEFAULT_PROJECT_VERSION);
			final String safeProductVersion = ofNullable(validateVersion(productVersion, 3)).orElse(DEFAULT_PRODUCT_VERSION);
			if(!project.getName().isPresent()) {
				logger.error("Cannot write a project without a name.");
				logger.info("Project not saved (this project have no name.");
			} else {
				writeNLP(project.getName().get(), zipConfig, safeProjectVersion, safeProductVersion);
				logger.info("Project saved.");
			}

		} catch (ParserConfigurationException | TransformerException | IOException e) {
			logger.error("Error writing project.", e);
		}
	}

	/**
	 *
	 * @return null if version is not valid, the version otherwise.
	 */
	@Nullable
	static String validateVersion(@Nullable final String version, final int expectedSize) {
		if (Strings.isNullOrEmpty(version)) {
			return null;
		}

		final Iterable<String> iterable = () -> Splitter.on(".").split(version).iterator();
		if (newStreamFromIterable(iterable).count() != expectedSize
				|| newStreamFromIterable(iterable).anyMatch(s -> s.length() != 1 || !isDigit(s.charAt(0)))) {
			logger.warn("Invalid version: " + version);
			return null;
		}
		return version;
	}

	private static Stream<String> newStreamFromIterable(final Iterable<String> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false);
	}

	private void writeXML(final boolean zipConfig) throws ParserConfigurationException, TransformerException, IOException {

		StreamResult repositoryStream = new StreamResult(new File(nlProjectFolder, ConfigFiles.REPOSITORY.fileName));
		StreamResult scenarioStream = new StreamResult(new File(nlProjectFolder, ConfigFiles.SCENARIO.fileName));


		//copy the file needed to NeoLoad Directory
		copyDataFilesToDestFolder(nlProjectFolder);
		changeBaseNameForCopiedVariables();
		createProjectFoldersIfNeeded();

		// write the repository
		DocumentBuilderFactory repositoryDocFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder repositoryDocBuilder = repositoryDocFactory.newDocumentBuilder();
		Document repositoryDoc = repositoryDocBuilder.newDocument();

		DocumentBuilderFactory scenarioDocFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder scenarioDocBuilder = scenarioDocFactory.newDocumentBuilder();
		Document scenarioDoc = scenarioDocBuilder.newDocument();

		ProjectWriter.of(project).writeXML(repositoryDoc, scenarioDoc, nlProjectFolder);

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.VERSION, "1.1");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		DOMSource repositorySource = new DOMSource(repositoryDoc);
		transformer.transform(repositorySource, repositoryStream);

		DOMSource scenarioSource = new DOMSource(scenarioDoc);
		transformer.transform(scenarioSource, scenarioStream);

		// Write SLA Profiles
		writeSlaProfiles(project.getSlaProfiles());

		// extract settings
		ProjectSettingsWriter.writeSettingsXML(nlProjectFolder, project.getProjectSettings());
		
		createConfig(zipConfig);
		deleteConfigFiles();
	}

	private void writeSlaProfiles(List<SlaProfile> slaProfiles) {
		slaProfiles.forEach(slaProfile -> {
			try {
				StreamResult slaStream = new StreamResult(new File(nlProjectFolder + File.separator + SLA_FOLDER, normalizeCollaborativeFileName(slaProfile.getName()) + ".xml"));
				DocumentBuilderFactory slaProfileDocFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder slaProfileDocBuilder = slaProfileDocFactory.newDocumentBuilder();
				Document slaProfileDoc = slaProfileDocBuilder.newDocument();

				SlaProfileWriter.of(slaProfile).writeXML(slaProfileDoc, null, nlProjectFolder);

				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				transformer.setOutputProperty(OutputKeys.VERSION, "1.1");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");

				DOMSource slaProfileSource = new DOMSource(slaProfileDoc);
				transformer.transform(slaProfileSource, slaStream);
			}catch(Exception e) {
				logger.error("Error while writing SLA profile \""+slaProfile.getName()+"\"on disk. ", e);
			}
		});
	}

	private void changeBaseNameForCopiedVariables() {
		ImmutableProject tmpProject = (ImmutableProject) project;
		List<Variable> newVariableList = new ArrayList<>();
		List<String> fileLst = new ArrayList<>();
		if (fileToCopy == null || fileToCopy.size() == 0) {
			return;
		}
		fileToCopy.get("variables").forEach(var -> fileLst.add(var.getAbsolutePath()));
		tmpProject.getVariables().forEach(var -> {
			if (var instanceof FileVariable && fileLst.contains(((FileVariable) var).getPath())) {
				ImmutableFileVariable fileVar = (ImmutableFileVariable) var;
				newVariableList.add(fileVar.withPath(
						FileVariableWriter.VARIABLE_DIRECTORY + File.separator + new File(fileVar.getPath()).getName()));
			} else {
				newVariableList.add(var);
			}
		});
		project = tmpProject.withVariables(newVariableList);
	}

	/**
	 * Copy parameters data files from LR project to NL project
	 */
	private void copyDataFilesToDestFolder(String destFolderFullPath) {
		if (fileToCopy == null)
			return;

		fileToCopy.forEach((repertory, dataFileList) -> {
			File repFile = new File(destFolderFullPath + File.separator + repertory);
			if (!repFile.exists()) {
				repFile.mkdir();
			}
			dataFileList.forEach(dataFile -> {
				Path source = Paths.get(dataFile.getAbsolutePath());
				Path destination = Paths.get(repFile.getAbsolutePath() + File.separator + dataFile.getName());
				try {
					Files.copy(source, destination);
				} catch (IOException e) {
					logger.error("Problem while copying parameter data file \"" + source.toString() + "\" into folder \"" + destination.toString()
							+ "\" :\n" + e);
				}
			});
		});
	}

	private void createProjectFoldersIfNeeded() {
		try {
			final Path recordedRequestsFolderPath = Paths.get(nlProjectFolder, RECORDED_REQUESTS_FOLDER);
			if (!recordedRequestsFolderPath.toFile().exists()) {
				Files.createDirectory(recordedRequestsFolderPath);
			}

			final Path recordedResponsesFolderPath = Paths.get(nlProjectFolder, RECORDED_RESPONSE_FOLDER);
			if (!recordedResponsesFolderPath.toFile().exists()) {
				Files.createDirectory(recordedResponsesFolderPath);
			}

			final Path slaProfielsFolderPath = Paths.get(nlProjectFolder, SLA_FOLDER);
			if (!slaProfielsFolderPath.toFile().exists()) {
				Files.createDirectory(slaProfielsFolderPath);
			}
		} catch (IOException e) {
			logger.error("Problem while creating project folders", e);
		}
	}

	private void createConfig(final boolean zipConfig) throws IOException {
		if(zipConfig){
			Map<String, String> env = new HashMap<>();
			env.put("create", "true");
	
			Path zipfile = Paths.get(nlProjectFolder, CONFIG_ZIP);
			URI uri = URI.create("jar:" + zipfile.toUri());
	
			try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {
				Stream.of(ConfigFiles.values()).forEach(configFile -> {
					Path filePath = Paths.get(nlProjectFolder, configFile.fileName);
					Path filePathInZipfile = zipfs.getPath("/" + configFile.fileName);
					// copy file into the zip file
					try {
						Files.copy(filePath, filePathInZipfile, REPLACE_EXISTING);
					} catch (IOException e) {
						logger.error("Error writing file in " + CONFIG_ZIP + " file", e);
					}
				});
			}
		} else {
			final Path confFolder = Paths.get(nlProjectFolder + File.separator + CONFIG_FOLDER);
			try {
				Files.createDirectories(confFolder);
			} catch (IOException e) {
				logger.error("Error creating folder " + CONFIG_FOLDER, e);
			}
			Stream.of(ConfigFiles.values()).forEach(configFile -> {
				Path filePath = Paths.get(nlProjectFolder, configFile.fileName);
				Path filePathInConfFolder = Paths.get(nlProjectFolder + File.separator + CONFIG_FOLDER, configFile.fileName);
				// copy a file into the config folder
				try {
					Files.copy(filePath, filePathInConfFolder, REPLACE_EXISTING);
				} catch (IOException e) {
					logger.error("Error copying file to " + CONFIG_FOLDER + " folder", e);
				}
			});
		}
	}


	protected static String normalizeCollaborativeFileName(String fileName) {
		final StringBuilder normalized = new StringBuilder();
		fileName.codePoints().forEach(value -> {
			if(Character.isUpperCase(value)) normalized.append('@');
			normalized.append((char)Character.toLowerCase(value));
		});
		return normalized.toString();
	}

	private void deleteConfigFiles() {
		// remove the files on disk
		Stream.of(ConfigFiles.values()).forEach(configFile -> {
			try {
				Files.delete(Paths.get(nlProjectFolder, configFile.fileName));
			} catch (IOException e) {
				logger.warn("Warning some config files were not cleanup", e);
			}
		});
	}

	private void writeNLP(final String name, final boolean zipConfig,
						  final String projectVersion, final String productVersion)
			throws IOException {
		File nlp = new File(nlProjectFolder, name + ".nlp");
		if (!nlp.exists() && !nlp.createNewFile()) {
			logger.error("Error, cannot create the NLP file");
		}
		if (!nlp.isFile()) {
			throw new IllegalArgumentException("NLP file must be a file");
		}
		PropertiesConfiguration nlpProperties = new PropertiesConfiguration();
		nlpProperties.getLayout().setGlobalSeparator("=");
		nlpProperties.addProperty("project.name", name);
		nlpProperties.addProperty("project.version", projectVersion);
		nlpProperties.addProperty("project.original.version", projectVersion);
		nlpProperties.addProperty("product.name", "NeoLoad");
		nlpProperties.addProperty("product.original.name", "NeoLoad");
		nlpProperties.addProperty("product.version", productVersion);
		nlpProperties.addProperty("product.original.version", productVersion);
		nlpProperties.addProperty("project.id", UUID.randomUUID().toString());
		nlpProperties.addProperty("project.config.path", zipConfig ? CONFIG_ZIP : CONFIG_FOLDER);
		nlpProperties.addProperty("project.config.storage", zipConfig ? "ZIP" : "FOLDER");
		nlpProperties.addProperty("team.server.enabled", "false");

		try (final Writer out = new FileWriter(nlp)) {
			nlpProperties.setHeader("Project description file");
			nlpProperties.write(out);
		} catch (ConfigurationException e) {
			logger.error("Error while saving NLP file", e);
		}
	}

	public File getNlProjectFolder() {
		return new File(nlProjectFolder);
	}
}
