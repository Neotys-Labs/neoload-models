package com.neotys.neoload.model.readers;


import com.neotys.neoload.model.Project;

public abstract class Reader {
		
	protected String folder;
	
	public Reader(String folder) {
		this.folder = folder;
	}

	public abstract Project read();

}
