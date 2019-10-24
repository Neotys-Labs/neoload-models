package com.neotys.neoload.model.readers.loadrunner.filereader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public abstract class IniFileReader {
	
	static Logger logger = LoggerFactory.getLogger(IniFileReader.class);
	
	protected File folder;
	private Optional<String> fileName;
	protected Wini fileInfos;
	protected final Set<String> sectionNames = new HashSet<>();
	
	IniFileReader(final File folderFullPath){
		this(folderFullPath, "");
	}
	
	IniFileReader(final File folderFullPath, String filename) {
		this.fileName = filename != null && !filename.equals("") ? Optional.of(filename) : Optional.empty();
		verifyPathAndLoadFile(folderFullPath);
		if(fileInfos!=null) {
			sectionNames.addAll(fileInfos.keySet());
		}
	}
	
	protected void verifyPathAndLoadFile(final File folderFullPath) {
		folder = folderFullPath;
		if(!folder.isDirectory()) {
			logger.error("Source parameter is not a folder. Please review your parameters and try again.");
			throw new IllegalArgumentException("Source path is not a folder");
		}
		File fileToLoad = new File(folderFullPath + File.separator + getFileName() );
		if(Strings.isNullOrEmpty(getFileName())){
			logger.warn("ParameterFile not defined.");
			return;
		} else if (!fileToLoad.isFile()){
			logger.warn(fileToLoad.getName()+ " is not a file.");
		}		
		if(!fileToLoad.isFile()){			
			fileToLoad = Arrays.asList(folder.listFiles()).stream()
					.filter(s -> s.getName().endsWith(getFileExtension()))
					.findFirst().orElse(null);
			if(fileToLoad==null){
				logger.warn("Cannot find any .prm file in project folder.");
				return;
			}
			logger.warn("The file " + fileToLoad.getName()+ " has been load instead");						
		}
		fileInfos = new Wini();
		try {
			fileInfos.load(fileToLoad);
		} catch (InvalidFileFormatException e) {
			logger.error("An error occurred while opening the project file \""+fileToLoad.getName()+"\". There is a format error in the file :\n" + e);
		} catch (IOException e) {
			logger.error("An error occurred while opening the project file \""+fileToLoad.getName()+"\". The error is :\n" + e);
		}
	}
	
	abstract String getFileExtension();
	
	public String getFileName() {
		return fileName.orElse(folder.getName() + getFileExtension());
	}
	
}
