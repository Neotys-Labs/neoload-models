package com.neotys.neoload.model.readers;

import com.neotys.neoload.model.Project;

import java.io.File;
import java.util.List;
import java.util.Map;

@Deprecated
public abstract class Reader {
		
	protected String folder;
	
	public Reader(String folder) {
		this.folder = folder;
	}

	public abstract Map<String, List<File>> getFileToCopy();

	public abstract Project read();
}
