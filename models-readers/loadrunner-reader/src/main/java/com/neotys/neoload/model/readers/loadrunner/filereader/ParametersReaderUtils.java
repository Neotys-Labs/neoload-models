package com.neotys.neoload.model.readers.loadrunner.filereader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.FileVariable;
import com.neotys.neoload.model.repository.ImmutableFileVariable;
import com.neotys.neoload.model.repository.Variable;
import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ParametersReaderUtils {
	
	static Logger logger = LoggerFactory.getLogger(ParametersReaderUtils.class);
	
	public static final String FILE_EXT = ".prm";
	public static final String TABLE = "Table";
	public static final String TABLE_LOCATION = "TableLocation";
	public static final String DELIMITER = "Delimiter";
	public static final String COLUMNNAME = "ColumnName";
	
	private ParametersReaderUtils() { }
	
	public static FileVariable parseTableParameter(final String leftBrace, final String rightBrace, final Ini.Section paramSection, final String projectFolder){
		String nextRowType = MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("SelectNextRow"));
		String updateMethod = MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("GenerateNewVal"));
		String paramName = MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("ParamName"));
		List<String> tmpColumnNames = new ArrayList<>();

		String noValueLeftBehavior = MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("OutOfRangePolicy"));

		Optional<String> filename = "Local".equals(MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get(TABLE_LOCATION))) ? Optional.of(projectFolder + File.separator + MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get(TABLE))) :
				Optional.of(MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get(TABLE_LOCATION)) + File.separator + MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get(TABLE)));


		String columnName = paramSection.get(COLUMNNAME);
		if (columnName != null) {
			tmpColumnNames.add(MethodUtils.normalizeString(leftBrace, rightBrace, columnName));
		} else {//LR Table case
			List<String> columnsNames = getLRColumns(leftBrace, rightBrace, paramSection, filename);
			if(columnsNames!=null) {
				tmpColumnNames.addAll(columnsNames);
			} else {
				// Log and return null
				logger.error("Variable without defined column is not supported. The variable "+paramName+" is not converted.");
				return null;
			}
		}

		return ImmutableFileVariable.builder()
				.name(paramName)
				.policy(getPolicy(updateMethod, paramName))
				.columnsDelimiter(MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.getOrDefault(DELIMITER, ",")))
				.columnsNames(tmpColumnNames)
				.fileName(filename)
				//.fileName(MethodUtils.normalizeString(paramSection.get(TABLE)))
				.firstLineIsColumnName(true)
				.numOfFirstRowData(Integer.parseInt(MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("StartRow")))+1)
				.scope(getScope(nextRowType))
				.order(getOrder(nextRowType, paramName))
				.noValuesLeftBehavior(getNoValueLeftBehavior(noValueLeftBehavior, paramName))
				.build();

	}

	private static FileVariable.VariableScope getScope(String type) {
		switch (type) {
			case "Sequential":
				//LR doc : Assigns data to a Vuser sequentially. As a running Vuser accesses the data table, it takes the next available row of data.
				//LR Doc suite : If there are not enough values in the data table, VuGen returns to the first value in the table, continuing in a loop until the end of the test.
				return FileVariable.VariableScope.LOCAL;
			case "Random":
				//LR doc : Assigns a value from a random row in the data table every time a new parameter value is requested.
				//LR doc :When you use the Controller to run a Vuser in a Scenario, you can specify a seed number for random sequencing. Each seed value represents one sequence of random values used for test execution. Whenever you use this seed value, the same sequence of values is assigned to the Vusers in the scenario. You enable this option if you discover a problem in the test execution and want to repeat the test using the same sequence of random values.
				return Variable.VariableScope.GLOBAL;
			case "Unique":
				//LR doc : Allocates a unique block of parameter values to each Vuser in the scenario, and then sequentially assigns values to the parameter for each Vuser from within the Vuser's block of values. Ensure that there is enough data in the table for all Vusers and their iterations. If you have 20 Vusers and you want to perform 5 iterations, your table must contain at least 100 unique values.
				//LR doc : If you run out of unique values, VuGen behaves according to the option you select in the When out of values field.
				return Variable.VariableScope.UNIQUE;
			default:
				return FileVariable.VariableScope.LOCAL;
		}
	}

	private static FileVariable.VariableOrder getOrder(String type, String paramName) {
		switch (type) {
			case "Sequential":
				//LR doc : Assigns data to a Vuser sequentially. As a running Vuser accesses the data table, it takes the next available row of data.
				//LR Doc suite : If there are not enough values in the data table, VuGen returns to the first value in the table, continuing in a loop until the end of the test.
				return FileVariable.VariableOrder.SEQUENTIAL;
			case "Random":
				//LR doc : Assigns a value from a random row in the data table every time a new parameter value is requested.
				//LR doc :When you use the Controller to run a Vuser in a Scenario, you can specify a seed number for random sequencing. Each seed value represents one sequence of random values used for test execution. Whenever you use this seed value, the same sequence of values is assigned to the Vusers in the scenario. You enable this option if you discover a problem in the test execution and want to repeat the test using the same sequence of random values.
				return FileVariable.VariableOrder.RANDOM;
			case "Unique":
				//LR doc : Allocates a unique block of parameter values to each Vuser in the scenario, and then sequentially assigns values to the parameter for each Vuser from within the Vuser's block of values. Ensure that there is enough data in the table for all Vusers and their iterations. If you have 20 Vusers and you want to perform 5 iterations, your table must contain at least 100 unique values.
				//LR doc : If you run out of unique values, VuGen behaves according to the option you select in the When out of values field.
				return FileVariable.VariableOrder.SEQUENTIAL;
			default:
				logger.warn("There isn't any \"Select Next Row\" defined for the parameter \"" + paramName + "\". It will be converted as if there were \"Sequential\"");
				return FileVariable.VariableOrder.SEQUENTIAL;
		}
	}

	private static Variable.VariableNoValuesLeftBehavior getNoValueLeftBehavior(String noValueLeftBehavior, String paramName) {
		switch (noValueLeftBehavior) {
			case "ContinueCyclic":
				return FileVariable.VariableNoValuesLeftBehavior.CYCLE;
			case "ContinueWithLast":
				logger.warn("The functionnality \"Continue with last value\" is not present in NeoLoad. It has been converted to the option \"<NO_VALUE>\" of NeoLoad");
				return FileVariable.VariableNoValuesLeftBehavior.NO_VALUE;
			case "AbortVuser":
				logger.warn("The functionnality \"AbortVuser\" is not present in NeoLoad. It has been changed to \"STOP\" for NeoLoad");
				return FileVariable.VariableNoValuesLeftBehavior.STOP;
			default:
				logger.warn("There isn't any \"Out of range policy\" defined for the parameter \"" + paramName + "\".It will be converted as if there were \"CYCLING\"");
				return FileVariable.VariableNoValuesLeftBehavior.CYCLE;
		}
	}

	private static FileVariable.VariablePolicy getPolicy(String updateMethod, String paramName){
		if ("EachIteration".equals(updateMethod)) {
			return FileVariable.VariablePolicy.EACH_ITERATION;
		}else if ("EachOccurrence".equals(updateMethod)) {
			return FileVariable.VariablePolicy.EACH_USE;
		}else if ("Once".equals(updateMethod)) {
			return FileVariable.VariablePolicy.EACH_VUSER;
		}else {
			logger.error("The update method is missing or invalid for the parameter \"" + paramName + "\"");
			return null;
		}
	}
	
	private static List<String> getLRColumns(final String leftBrace, final String rightBrace, final Ini.Section paramSection, final Optional<String> filename){
		List<String> columnsNames = null;
		String columnIndexes = MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("ColumnIndexesList"));
		try(Stream<String> fileStream = Files.lines(Paths.get(filename.orElseThrow(IllegalStateException::new)))){
			columnsNames = Arrays.asList(fileStream.findFirst().orElseThrow(IllegalStateException::new).split(MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.getOrDefault(DELIMITER, ","))));
		} catch (IllegalStateException|IOException e) {
			logger.error("Reading of file \"" + filename.orElse("") + "\" impossible :\n", e);
			return columnsNames;
		}
		if (columnIndexes != null && !columnIndexes.isEmpty())
			return Arrays.asList(columnIndexes.split(","))
					.stream().map(String::trim).map(Integer::parseInt).map(idx -> idx - 1).map(columnsNames::get).collect(Collectors.toList());
		
		return columnsNames;		
	}

	public static String[][] loadColumnOfDataFile(String fileName, List<String> columnsNames, String columnsDelimiter) {
		List<String[]> tempData = new ArrayList<>();
		List<Integer> colomnIndexs = new ArrayList<>();
		
		try {
			//get column indices for data
			try(Stream<String> fileStream = Files.lines(Paths.get(fileName))){
				List<String> columns = Arrays.asList(fileStream.findFirst().orElseThrow(IllegalStateException::new).split(columnsDelimiter));
				columnsNames.stream().map(columns::indexOf).forEach(index -> {
					if(index == -1) {
						StringBuilder sb = new StringBuilder();
						columnsNames.forEach(colName -> sb.append(colName).append(", "));
						logger.error("Some needed columns in the list \"" + sb.substring(0, sb.length() - 2) + "\" are not in the file \"" + fileName + "\". The values will be changed to a void string");
					}
					colomnIndexs.add(index);
				});
			}
			
			//get data
			try(Stream<String> fileStream = Files.lines(Paths.get(fileName))){
				fileStream.skip(1).forEach(line -> {
					String[] fields = line.split(columnsDelimiter);
					String[] neededFields = new String[columnsNames.size()];
					for (int i = 0 ; i < columnsNames.size() ; i++) {
						int idx = colomnIndexs.get(i);
						neededFields[i] = (idx == -1 || idx>=fields.length) ? "" : fields[idx];
					}
					tempData.add(neededFields);
				});
			}
		} catch (IOException e) {
			logger.error("An arror occured while reading the file \"" + fileName + "\" :\n", e);
		}
		
		String[][] tempArr = new String[tempData.size()][];
		return tempData.toArray(tempArr);
	}

	public static FileVariable handleVariableColumns(final String leftBrace, final String rightBrace, final Ini.Section paramSection, final FileVariable refVarFile) {
		String newColName = MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get(COLUMNNAME));

		List allColumns = ImmutableList.builder().addAll(refVarFile.getColumnsNames()).add(newColName).build();

		return  ImmutableFileVariable.builder()
				.from(refVarFile)
				.columnsNames(allColumns)
				.build();
	}
	
	/**
	 * 
	 * @param paramSection
	 * @param refVarFile
	 * @param lrFolder
	 * @return
	 */
	public static FileVariable handleDataForFileVariable(final String leftBrace, final String rightBrace, final Ini.Section paramSection, final FileVariable refVarFile, final File lrFolder) {
		//load file in memory and add it to the data of the parameter
		String [][] currentData = refVarFile.getData()
				.orElseGet(() -> loadColumnOfDataFile(refVarFile.getFileName().orElseThrow(IllegalStateException::new),
						refVarFile.getColumnsNames(),
						refVarFile.getColumnsDelimiter()));

		List<String> newCol = new ArrayList<>();
		newCol.add(MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get(COLUMNNAME)));
		String [][] newColumnData = loadColumnOfDataFile(lrFolder.getPath() + File.separator + MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get(TABLE)),
				newCol, MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get(DELIMITER)));
		
		//create and fill the newData Array
		if(newColumnData.length != currentData.length) {
			logger.warn("The parameter \"" + paramSection.get("ParamName") + "\"which use the option \"Same line as\" does not have the same number of line of the reference parameter. Values will be completed with void string");
		}
		boolean newDataIsBigger = newColumnData.length > currentData.length;
		int maxRow = Math.max(newColumnData.length, currentData.length);
		int minRow = Math.min(newColumnData.length, currentData.length);
		String [][] newData = new String[maxRow][];
		int currentDataColumns = refVarFile.getColumnsNames().size(); 

		//Here we don't use maxRow but currentData.length because we don't need the exceeding value (which are never used)
		for(int i = 0 ; i < currentData.length ; i++) {
			String[] newLine = new String[currentDataColumns + 1];
			for (int j = 0 ; j < currentDataColumns ; j++) {
				newLine[j] = i < minRow || !newDataIsBigger ? currentData[i][j] : "";
			}
			newLine[currentDataColumns] = i < minRow || newDataIsBigger ? newColumnData[i][0] : "";
			newData[i] = newLine;
		}
		
		//recreate an immutable with the new data in memory
		return  ImmutableFileVariable.builder()
				.from(refVarFile)
				.data(newData)
				.fileName(Optional.empty())
				.build();
	}
}
