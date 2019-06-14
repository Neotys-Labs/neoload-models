package com.neotys.neoload.model.readers.loadrunner.filereader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.neotys.neoload.model.readers.loadrunner.LoadRunnerReader;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.*;
import com.neotys.neoload.model.repository.Variable.VariableScope;
import com.neotys.neoload.model.repository.Variable.VariablePolicy;
import com.neotys.neoload.model.repository.Variable.VariableNoValuesLeftBehavior;
import com.neotys.neoload.model.repository.Variable.VariableOrder;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;

public class ParameterFileReader extends IniFileReader {

	static Logger logger = LoggerFactory.getLogger(ParameterFileReader.class);

	private Map<String, Variable> variables;

	private List<String> variablesNotDone;

	private final LoadRunnerReader reader;
	private final ProjectFileReader projectFileReader;

	private Map<String, String> nLVarValueFromLRVarName;

	private final String leftBrace;
	private final String rightBrace;

	public ParameterFileReader(final LoadRunnerReader reader, final ProjectFileReader projectFileReader, final File folderFullPath) {
		super(folderFullPath, projectFileReader.getParameterFile());
		this.projectFileReader = projectFileReader;
		variables = new HashMap<>();
		variablesNotDone = new ArrayList<>();
		nLVarValueFromLRVarName = new HashMap<>();
		this.leftBrace = projectFileReader.getLeftBrace();
		this.rightBrace = projectFileReader.getRightBrace();
		this.reader = reader;
		//Load of general section
		sectionNames.stream().filter(s -> s.startsWith("parameter:")).forEach(this::handleParameter);

		fillTableValuesFromDependantParameter();
		MethodUtils.setVariableMapping(nLVarValueFromLRVarName);
	}

	private void handleParameter(final String param) {
		final Wini.Section paramSection = fileInfos.get(param);
		final String paramName = MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("ParamName"));
		final String paramType = MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("Type"));
		switch (paramType) {
			case "TTable":
				final String warning = "For compatibility purpose, parameter \"TTABLE\" has been change to \"File\" in NeoLoad.";
				projectFileReader.readSupportedParameterWithWarn(paramType, paramName, warning);
				parseTableParameter(paramSection, param, paramName);
				break;
			case ParametersReaderUtils.TABLE:
				projectFileReader.readSupportedParameter(paramType, paramName);
				parseTableParameter(paramSection, param, paramName);
				break;
			case "Unique":
				projectFileReader.readSupportedParameter(paramType, paramName);
				parseCounterParameter(paramSection, paramName);
				break;
			case "Random":
				projectFileReader.readSupportedParameter(paramType, paramName);
				parseRandomParameter(paramSection, paramName);
				break;
			case "Custom":
				projectFileReader.readSupportedParameter(paramType, paramName);
				parseCustomParameter(paramSection, paramName);
				break;
			default:
				projectFileReader.readUnsupportedParameter(paramType, paramName);
		}
	}

	@Override
	String getFileExtension() {
		return ParametersReaderUtils.FILE_EXT;
	}

	private void parseCounterParameter(Wini.Section paramSection, String paramName) {

		VariablePolicy pol = getPolicy(MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("GenerateNewVal")), paramName);

		CounterNumberVariable variable = ImmutableCounterNumberVariable.builder().name(paramName).startValue(
				Integer.parseInt(MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("StartValue")))).maxValue(
						Integer.MAX_VALUE).increment(1).scope(VariableScope.GLOBAL).noValuesLeftBehavior(
								Variable.VariableNoValuesLeftBehavior.NO_VALUE).order(Variable.VariableOrder.SEQUENTIAL).policy(pol).build();
		variables.put(paramName, variable);
	}

	private void parseRandomParameter(Wini.Section paramSection, String paramName) {

		Variable.VariablePolicy pol = getPolicy(MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("GenerateNewVal")), paramName);

		RandomNumberVariable variable = ImmutableRandomNumberVariable.builder().name(paramName).minValue(
				Long.parseLong(MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("MinValue")))).maxValue(
						Long.parseLong(MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("MaxValue")))).scope(
								VariableScope.LOCAL).policy(pol).build();
		variables.put(paramName, variable);
	}

	private void parseCustomParameter(Wini.Section paramSection, String paramName) {

		ConstantVariable variable = ImmutableConstantVariable.builder().name(paramName).constantValue(
				MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("CurrentValue"))).description(
						MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("Prompt")))
				//Here we put NeoLoad Default
				.scope(VariableScope.GLOBAL).policy(VariablePolicy.EACH_VUSER).order(VariableOrder.SEQUENTIAL).noValuesLeftBehavior(
						VariableNoValuesLeftBehavior.CYCLE).build();
		variables.put(paramName, variable);
	}

	@VisibleForTesting
	protected VariablePolicy getPolicy(final String policy, final String paramName) {
		switch (policy) {
			case "EachOccurrence":
				projectFileReader.readSupportedParameter(policy, paramName);
				return VariablePolicy.EACH_USE;
			case "EachIteration":
				projectFileReader.readSupportedParameter(policy, paramName);
				return VariablePolicy.EACH_ITERATION;
			case "Once":
				projectFileReader.readSupportedParameter(policy, paramName);
				return VariablePolicy.EACH_VUSER;
			default:
				final String warning = "Converted to EACH_USE in NeoLoad";
				projectFileReader.readSupportedParameterWithWarn(policy, paramName, warning);
				return VariablePolicy.EACH_USE;
		}
	}

	private void parseTableParameter(final Wini.Section paramSection, final String sectionIdentifier, final String paramName) {
		if (paramSection.get("SelectNextRow").startsWith("\"Same line as ")) {
			variablesNotDone.add(sectionIdentifier);
			return;
		}
		FileVariable variable = ParametersReaderUtils.parseTableParameter(leftBrace, rightBrace, paramSection, folder.getAbsolutePath());

		if (variable != null) {
			if ("Local".equals(MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get(ParametersReaderUtils.TABLE_LOCATION)))) {
				reader.addDataFilesToCopy(new File(variable.getFileName().orElseThrow(IllegalStateException::new)));
			}

			nLVarValueFromLRVarName.put(variable.getName(), variable.getName() + "." + variable.getColumnsNames().get(0));
			variables.put(paramName, variable);
		}
	}

	/**
	 * For each parameter with a "Same line as ", we use the same parameter as the initially create one.
	 * If it's the same file, we just add the column to the current reference data.
	 * Overwise we change the type to the TABLE parameter and we load the data in memory, 
	 */
	private void fillTableValuesFromDependantParameter() {
		variablesNotDone.stream().forEach(this::manageVariable);
	}

	private void manageVariable(final String variableName) {
		final Wini.Section paramSection = fileInfos.get(variableName);
		//here we don't need to test if it exists because it has been added to this list on these tests
		String referenceVariableName = MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("SelectNextRow")).substring(
				"Same line as ".length());
		Variable refVar = variables.get(referenceVariableName);
		if (refVar == null || !(refVar instanceof FileVariable)) {
			logger.error("The reference parameter \"" + referenceVariableName
					+ "\" does not exist or is not of the correct type for the parameter \"" + variableName);
		} else {
			FileVariable refVarFile = (FileVariable) refVar;
			nLVarValueFromLRVarName.put(MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("ParamName")),
					refVarFile.getName() + "." + MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get("ColumnName")));
			FileVariable newVariable = ImmutableFileVariable.copyOf(refVarFile);
			if (!(refVarFile.getFileName().isPresent() && refVarFile.getFileName().get().equals(
					//Here we create the dataFile full path to compare with the full path of the dependant variable
					"Local".equals(MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get(ParametersReaderUtils.TABLE_LOCATION)))
							? folder + File.separator + MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get(ParametersReaderUtils.TABLE))
							: MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get(ParametersReaderUtils.TABLE_LOCATION)) + File.separator
									+ MethodUtils.normalizeString(leftBrace, rightBrace, paramSection.get(ParametersReaderUtils.TABLE))))) {
				newVariable = ParametersReaderUtils.handleDataForFileVariable(leftBrace, rightBrace, paramSection, refVarFile, folder);
				reader.removeDataFilesToCopyIf(dataFile -> dataFile.getAbsolutePath().equals(refVarFile.getFileName().orElse("")));
			}
			newVariable = ParametersReaderUtils.handleVariableColumns(leftBrace, rightBrace, paramSection, newVariable);
			variables.put(referenceVariableName, newVariable);
		}
	}

	public Variable getVariable(String name) {
		return variables.get(name);
	}

	public List<Variable> getAllVariables() {
		return variables.keySet().stream().map(variables::get).collect(Collectors.toList());
	}
}
