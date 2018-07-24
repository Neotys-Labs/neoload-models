package com.neotys.neoload.model.readers.loadrunner.filereader;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
	private LinkedHashMap<String,String> actions;
	private Ini.Section actionsSection;
	
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
		
		//load of actions
		actions = new LinkedHashMap<>();
		if (!sectionNames.contains("Actions")) {
			logger.error("There is no Actions section in the file : " + getFileName());
			logger.warn("The default brace values will be { and }");
			
		}else {	
			actionsSection = fileInfos.get("Actions");
			actionsSection.keySet().forEach(this::addAction);
		}
	}

	private void addAction(final String action) {
		actions.put(action,actionsSection.get(action));				
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

	public Map<String,String> getActions() {
		return actions;
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
