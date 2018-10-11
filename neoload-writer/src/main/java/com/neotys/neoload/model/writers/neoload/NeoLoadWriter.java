package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.ImmutableProject;
import com.neotys.neoload.model.Project;
import com.neotys.neoload.model.repository.FileVariable;
import com.neotys.neoload.model.repository.ImmutableFileVariable;
import com.neotys.neoload.model.repository.Variable;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

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

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class NeoLoadWriter {

	private static Logger logger = LoggerFactory.getLogger(NeoLoadWriter.class);

	private static final String PROJECT_VERSION = "6.5";
	private static final String PRODUCT_VERSION = "6.7.0";

	private static final String CONFIG_ZIP = "config.zip";
	private static final String CONFIG_FOLDER = "config";
	
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
	}

	private Project project;
	private final String nlProjectFolder;
	private Map<String, List<File>> fileToCopy;

	public NeoLoadWriter(final Project project, final String nlProjectFolder, final Map<String, List<File>> map) {
		this.project = project;
		this.nlProjectFolder = nlProjectFolder;
		this.fileToCopy = map;
	}

	public void write(final boolean zipConfig) {
		try {
			final File f = new File(nlProjectFolder);
			if (!f.exists()) {
				logger.info("Output folder does not exist, creating it.");
				Files.createDirectories(Paths.get(nlProjectFolder));
			} else if (f.isFile()) {
				logger.error("The destination is not a directory, migration aborted.");
				return;
			}
			writeXML(zipConfig);
			writeNLP(project.getName().isPresent() ? project.getName().get() : "MyProject", zipConfig);
			logger.info("Project saved.");

		} catch (ParserConfigurationException | TransformerException | IOException e) {
			logger.error("Error writing project.", e);
		}
	}

	private void writeXML(final boolean zipConfig) throws ParserConfigurationException, TransformerException, IOException {

		StreamResult repositoryStream = new StreamResult(new File(nlProjectFolder, ConfigFiles.REPOSITORY.fileName));
		StreamResult scenarioStream = new StreamResult(new File(nlProjectFolder, ConfigFiles.SCENARIO.fileName));


		//copy the file needed to NeoLoad Directory
		copyDataFilesToDestFolder(nlProjectFolder);
		changeBaseNameForCopiedVariables();
		createRecordedFoldersIfNeeded();

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

		// extract settings
		FileUtils.copyInputStreamToFile(NeoLoadWriter.class.getResourceAsStream(ConfigFiles.SETTINGS.fileName),
				new File(nlProjectFolder, "settings.xml"));
		createConfig(zipConfig);
		deleteConfigFiles();
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
			if (var instanceof FileVariable && ((FileVariable) var).getFileName().isPresent()
					&& fileLst.contains(((FileVariable) var).getFileName().get())) {
				ImmutableFileVariable fileVar = (ImmutableFileVariable) var;
				newVariableList.add(fileVar.withFileName(
						FileVariableWriter.VARIABLE_DIRECTORY + File.separator + new File(fileVar.getFileName().get()).getName()));
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

	private void createRecordedFoldersIfNeeded() {
		try {
			final Path recordedRequestsFolderPath = Paths.get(nlProjectFolder, RECORDED_REQUESTS_FOLDER);
			if (!recordedRequestsFolderPath.toFile().exists()) {
				Files.createDirectory(recordedRequestsFolderPath);
			}

			final Path recordedResponsesFolderPath = Paths.get(nlProjectFolder, RECORDED_RESPONSE_FOLDER);
			if (!recordedResponsesFolderPath.toFile().exists()) {
				Files.createDirectory(recordedResponsesFolderPath);
			}
		} catch (IOException e) {
			logger.error("Problem while creating recorded requests and responses folders", e);
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

	private void writeNLP(final String name, final boolean zipConfig) throws IOException {
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
		nlpProperties.addProperty("project.version", PROJECT_VERSION);
		nlpProperties.addProperty("project.original.version", PROJECT_VERSION);
		nlpProperties.addProperty("product.name", "NeoLoad");
		nlpProperties.addProperty("product.original.name", "NeoLoad");
		nlpProperties.addProperty("product.version", PRODUCT_VERSION);
		nlpProperties.addProperty("product.original.version", PRODUCT_VERSION);
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
