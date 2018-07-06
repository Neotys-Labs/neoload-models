package com.neotys.neoload.model.repository;

import java.util.List;
import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface FileVariable extends Variable {
	
	String getColumnsDelimiter();
	
	List<String> getColumnsNames();

	//Either data or filename needs to be filled but not both
	Optional<String[][]> getData();
	Optional<String> getFileName();
	
	boolean getFirstLineIsColumnName();
	
	int getNumOfFirstRowData();
	
}
