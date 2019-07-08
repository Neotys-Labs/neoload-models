package com.neotys.neoload.model.v3.readers;

import com.neotys.neoload.model.v3.project.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class Reader {
		
	protected String folder;
	private List<File> dataFilesToCopy = new ArrayList<>();
	
	public Reader(String folder) {
		this.folder = folder;
	}

	public Map<String, List<File>> getFileToCopy() {
		Map<String, List<File>> returnedMap = new HashMap<>();
		returnedMap.put("variables", dataFilesToCopy);
		return returnedMap;
	}

	public void addDataFilesToCopy(final File file) {
		dataFilesToCopy.add(file);
	}

	public void removeDataFilesToCopyIf(final Predicate<File> filter) {
		dataFilesToCopy.removeIf(filter);
	}

	public abstract Project read();
}
