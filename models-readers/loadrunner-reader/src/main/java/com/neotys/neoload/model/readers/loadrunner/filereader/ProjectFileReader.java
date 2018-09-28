package com.neotys.neoload.model.readers.loadrunner.filereader;

import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;


public class ProjectFileReader extends IniFileReader{

	static Logger logger = LoggerFactory.getLogger(ProjectFileReader.class);
	public static final String FILE_EXT = ".usr";
	private final EventListener eventListener;
	private final LoadRunnerReader reader;
	private Pattern variablePattern;
	private String leftBrace;
	private String rightBrace;
	private String parameterFile;
	// key: action name, value: action file
	private LinkedHashMap<String,String> alls;
	private Ini.Section actionsSection;

	private List<String> inits = new LinkedList<>();
	private List<String> actions = new LinkedList<>();
	private List<String> ends = new LinkedList<>();
	
	public ProjectFileReader(final LoadRunnerReader reader, final EventListener eventListener, final File folderFullPath){
		super(folderFullPath);
		this.reader = reader;
		this.eventListener = eventListener;

		if(!folderFullPath.exists()) throw new IllegalArgumentException("Source project path does not exist");

		this.leftBrace = "{";
		this.rightBrace = "}";
		this.variablePattern = Pattern.compile("\\Q" + leftBrace + "\\E((?!\\Q"+ rightBrace +"\\E).)+\\Q" + rightBrace + "\\E");
		this.parameterFile = "";
		
		//Load of general section
		if (!sectionNames.contains("General")) {
			logger.warn("There is no general section in the file : " + getFileName());
			logger.warn("The default brace values will be { and }");
			logger.warn("The default parameter file will be the first \"*.prm file found\"");			
		}else {	
			Ini.Section generalSection = fileInfos.get("General");
			this.leftBrace = MethodUtils.normalizeString(leftBrace, rightBrace, generalSection.getOrDefault("ParamLeftBrace", "{"));
			this.rightBrace = MethodUtils.normalizeString(leftBrace, rightBrace, generalSection.getOrDefault("ParamRightBrace", "}"));
			this.parameterFile = MethodUtils.normalizeString(leftBrace, rightBrace, generalSection.getOrDefault("ParameterFile", ""));
		}
		
		//load of alls
		alls = new LinkedHashMap<>();
		if (!sectionNames.contains("Actions")) {
			logger.error("There is no Actions section in the file : " + getFileName());
			logger.warn("The default brace values will be { and }");
			
		}else {	
			actionsSection = fileInfos.get("Actions");
			actionsSection.keySet().forEach(this::addAction);
		}

		readLogic(folderFullPath);
	}

	private void readLogic(final File folderFullPath) {
		if (sectionNames.contains("RunLogicFiles")) {
			final String logicFileName = fileInfos.get("RunLogicFiles").get("Default Profile");
			File logicFile = new File(folderFullPath + File.separator + logicFileName );
			if (!logicFile.isFile()){
				logger.warn(logicFile.getName()+ " is not a file.");
			} else {
				final Wini logicFileInfos = new Wini();
				try {
					logicFileInfos.load(logicFile);
				} catch (InvalidFileFormatException e) {
					logger.error("An error occurred while opening the project file \"" + logicFile.getName() + "\". There is a format error in the file :\n" + e);
				} catch (IOException e) {
					logger.error("An error occurred while opening the project file \"" + logicFile.getName() + "\". The error is :\n" + e);
				}
				inits.addAll(getOrderedItems(logicFileInfos, "RunLogicInitRoot"));
				actions.addAll(getOrderedItems(logicFileInfos, "RunLogicRunRoot"));
				ends.addAll(getOrderedItems(logicFileInfos, "RunLogicEndRoot"));
				return;
			}
		}
		inits.add("vuser_init");
		ends.add("vuser_end");
	}

	private List<String> getOrderedItems(final Wini logicFileInfos, final String sectionName) {
		if(logicFileInfos.containsKey(sectionName)){
			final Profile.Section section = logicFileInfos.get(sectionName);
			if(section != null) {
				final String orderedActionsList = MethodUtils.unquote(section.get("RunLogicActionOrder"));
				return Arrays.asList(orderedActionsList.split(","));
			}
		}
		return Collections.emptyList();
	}

	private void addAction(final String action) {
		alls.put(action,actionsSection.get(action));
	}

	public String getVirtualUserName() {
		if(getFileName().endsWith(getFileExtension())) {
			return getFileName().substring(0,getFileName().lastIndexOf(getFileExtension()));
		}
		return getFileName();
	}
	
	@Override
	String getFileExtension() {
		return FILE_EXT;
	}
	
	public Pattern getVariablePattern() {
		return variablePattern;
	}

	public String getLeftBrace() {
		return leftBrace;
	}

	public String getRightBrace() {
		return rightBrace;
	}

	public String getParameterFile() {
		return parameterFile;
	}

	public Map<String,String> getAllActionsMap() {
		return alls;
	}

	public List<String> getActions() {
		return actions;
	}

	public List<String> getInits() {
		return inits;
	}

	public List<String> getEnds() {
		return ends;
	}

	public static boolean isUSRFile(final File file){
		return file.getName().endsWith(ProjectFileReader.FILE_EXT);
	}
	
	public static boolean containsUSRFile(final File folder){
		if(!folder.isDirectory()) return false;
		return Arrays.asList(folder.listFiles()).stream().filter(ProjectFileReader::isUSRFile).findAny().isPresent();
	}

	public void readSupportedParameter(final String parameterType, final String parameterName) {
		eventListener.readSupportedParameter(reader.getCurrentScriptName(), parameterType, parameterName);		
	}
	
	public void readSupportedParameterWithWarn(final String parameterType, final String parameterName, final String warning) {
		eventListener.readSupportedParameterWithWarn(reader.getCurrentScriptName(), parameterType, parameterName,  warning);		
	}
	
	public void readUnsupportedParameter(final String parameterType, final String parameterName) {
		eventListener.readUnsupportedParameter(reader.getCurrentScriptName(), parameterType, parameterName);		
	}
}
