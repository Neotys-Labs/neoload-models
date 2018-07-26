package com.neotys.neoload.model.writers.neoload;

import com.neotys.neoload.model.ImmutableProject;
import com.neotys.neoload.model.Project;
import com.neotys.neoload.model.repository.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
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
import java.io.*;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class NeoLoadWriter {

	private static Logger logger = LoggerFactory.getLogger(NeoLoadWriter.class);

	private static final String PROJECT_VERSION = "6.4";
	private static final String PRODUCT_VERSION = "6.6.0";

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

	public void write() {
		try {
			final File f = new File(nlProjectFolder);
			if (!f.exists()) {
				logger.info("Output folder does not exist, creating it.");
				Files.createDirectories(Paths.get(nlProjectFolder));
			} else if (f.isFile()) {
				logger.error("The destination is not a directory, migration aborted.");
				return;
			}
			writeXML();
			writeNLP(project.getName());
			logger.info("Project saved.");

		} catch (ParserConfigurationException | TransformerException | IOException e) {
			logger.error("Error writing project.", e);
		}
	}

	private void writeXML() throws ParserConfigurationException, TransformerException, IOException {

		StreamResult result = new StreamResult(new File(nlProjectFolder, ConfigFiles.REPOSITORY.fileName));

		//copy the file needed to NeoLoad Directory
		copyDataFilesToDestFolder(nlProjectFolder);
		changeBaseNameForCopiedVariables();

		//files need to be copied to neoload project folder before XML writing!
		copyFilesToRecordedFolders(project);

		// write the repository
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		ProjectWriter.of(project).writeXML(doc, nlProjectFolder);

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.VERSION, "1.1");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);

		transformer.transform(source, result);

		// extract scenario / settings
		FileUtils.copyInputStreamToFile(NeoLoadWriter.class.getResourceAsStream(ConfigFiles.SCENARIO.fileName),
				new File(nlProjectFolder, "scenario.xml"));
		FileUtils.copyInputStreamToFile(NeoLoadWriter.class.getResourceAsStream(ConfigFiles.SETTINGS.fileName),
				new File(nlProjectFolder, "settings.xml"));
		createConfigZip();
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

	private void copyFilesToRecordedFolders(final Project project) {
		createRecordedFoldersIfNeeded();

		final Set<Request> allRequests = getAllRequests(project);
		allRequests.forEach(request -> {
			final String recordedRequestHeaderFile = request.getRecordedFiles().flatMap(RecordedFiles::recordedRequestHeaderFile).orElse(null);
			final String recordedRequestBodyFile = request.getRecordedFiles().flatMap(RecordedFiles::recordedRequestBodyFile).orElse(null);

			copyRequestContentFile(recordedRequestHeaderFile, recordedRequestBodyFile);
			copyResponseContentFile(request.getRecordedFiles());
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

	private Set<Request> getAllRequests(final Project project) {
		return project.getUserPaths().stream()
				.flatMap(UserPath::flattened)
				.filter(element -> element instanceof Request)
				.map(element -> (Request) element)
				.collect(Collectors.toSet());
	}

	private void copyRequestContentFile(final String recordedRequestHeaderFile, final String recordedRequestBodyFile) {
		//Copy files "lrProjectFolder/data/t22_RequestHeader.htm"
		// and "lrProjectFolder/data/t22_RequestBody.htm"
		//to "nlProjectFolder/recorded-requests/t22_requestHeader.htm"
		final boolean hasHeaders = !isNullOrEmpty(recordedRequestHeaderFile);
		final boolean hasBody = !isNullOrEmpty(recordedRequestBodyFile);
		if (hasHeaders || hasBody) {
			final Path recordedRequestHeaderPathFromLRProject = hasHeaders ? Paths.get(recordedRequestHeaderFile) : null;
			final Path recordedRequestBodyPathFromLRProject = hasBody ? Paths.get(recordedRequestBodyFile) : null;
			final Path fileName = hasBody ? recordedRequestBodyPathFromLRProject : recordedRequestHeaderPathFromLRProject;
			final Path filePathInNLProject = Paths.get(nlProjectFolder, RECORDED_REQUESTS_FOLDER,
					"req_" + fileName.getFileName().toString());
			try (final FileOutputStream fileOutputStream = new FileOutputStream(filePathInNLProject.toFile())) {
				if (hasHeaders) {
					fileOutputStream.write(Files.readAllBytes(recordedRequestHeaderPathFromLRProject));
				}
				if (hasBody) {
					fileOutputStream.write(Files.readAllBytes(recordedRequestBodyPathFromLRProject));
				}
				fileOutputStream.flush();
			} catch (IOException e) {
				logger.error("Error while copying the recorded request header, body to NeoLoad project folder", e);
			}
		}
	}

	private void copyResponseContentFile(final Optional<RecordedFiles> recordedFiles) {
		//Copy file "lrProjectFolder/data/t22.htm"
		//to "nlProjectFolder/recorded-responses/t22.htm"

		final String recordedResponseBodyFile = recordedFiles.flatMap(RecordedFiles::recordedResponseBodyFile).orElse(null);
		if (!isNullOrEmpty(recordedResponseBodyFile)) {
			Path recordedResponseBodyPathFromLRProject = Paths.get(recordedResponseBodyFile);
			Path recordedResponseBodyPathToNLProject = Paths.get(nlProjectFolder, RECORDED_RESPONSE_FOLDER,
					"res_" + recordedResponseBodyPathFromLRProject.getFileName().toString());
			try {
				Files.copy(recordedResponseBodyPathFromLRProject, recordedResponseBodyPathToNLProject, REPLACE_EXISTING);
			} catch (IOException e) {
				logger.error("Error while copying the recorded response body to NeoLoad project folder", e);
			}
		}
	}

	private void createConfigZip() throws IOException {
		Map<String, String> env = new HashMap<>();
		env.put("create", "true");

		Path zipfile = Paths.get(nlProjectFolder, "config.zip");
		URI uri = URI.create("jar:" + zipfile.toUri());

		try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {
			Stream.of(ConfigFiles.values()).forEach(configFile -> {
				Path filePath = Paths.get(nlProjectFolder, configFile.fileName);
				Path filePathInZipfile = zipfs.getPath("/" + configFile.fileName);
				// copy a file into the zip file
				try {
					Files.copy(filePath, filePathInZipfile, REPLACE_EXISTING);
				} catch (IOException e) {
					logger.error("Error writing file in config.zip file", e);
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

	private void writeNLP(String name) throws IOException {
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
		nlpProperties.addProperty("project.config.path", "config.zip");
		nlpProperties.addProperty("project.config.storage", "ZIP");
		nlpProperties.addProperty("team.server.enabled", "false");

		try (final Writer out = new FileWriter(nlp)) {
			nlpProperties.setHeader("Project description file");
			nlpProperties.save(out);
		} catch (ConfigurationException e) {
			logger.error("Error while saving NLP file", e);
		}
	}

	public File getNlProjectFolder() {
		return new File(nlProjectFolder);
	}
}
