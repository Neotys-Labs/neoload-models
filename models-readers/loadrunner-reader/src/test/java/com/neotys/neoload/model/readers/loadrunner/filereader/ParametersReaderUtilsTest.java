package com.neotys.neoload.model.readers.loadrunner.filereader;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.ini4j.Ini;
import org.ini4j.Profile;
import org.junit.Test;

import com.neotys.neoload.model.repository.FileVariable;
import com.neotys.neoload.model.repository.Variable;
@SuppressWarnings("squid:S2699")
public class ParametersReaderUtilsTest {

	@Test
	public void parseTableParameterTest() {
		Ini ini = new Ini();
		Profile.Section section = ini.add("parameter:user");
		section.put("ColumnName", "name");
		section.put("SelectNextRow", "Unique");
		section.put("GenerateNewVal","EachIteration");
		section.put("ParamName","userVarName");
		section.put("OutOfRangePolicy","ContinueCyclic");
		section.put("Type","Table");
		section.put("TableLocation","Local");
		section.put("Table","user.dat");
		section.put("StartRow","1");

		FileVariable variable = ParametersReaderUtils.parseTableParameter("{", "}", section, ".");

		assertThat(variable.getName()).isEqualTo("userVarName");
		assertThat(variable.getColumnsNames()).containsExactly("name");
		assertThat(variable.getOrder()).isEqualTo(Optional.of(Variable.VariableOrder.SEQUENTIAL));
	}

	@Test
	public void parseTableMultiParametersTest() {
		final Ini ini = new Ini();
		Profile.Section userSection = ini.add("parameter:user");
		userSection.put("ColumnName", "\"name\"");
		userSection.put("SelectNextRow", "\"Unique\"");
		userSection.put("GenerateNewVal","\"EachIteration\"");
		userSection.put("ParamName","\"userVarName\"");
		userSection.put("OutOfRangePolicy","\"ContinueCyclic\"");
		userSection.put("Type","\"Table\"");
		userSection.put("TableLocation","\"Local\"");
		userSection.put("Table","\"user.dat\"");
		userSection.put("StartRow","\"1\"");
		userSection.put("Delimiter","\",\"");

		Profile.Section passwdSection = ini.add("parameter:password");
		passwdSection.put("ColumnName", "\"passwd\"");
		passwdSection.put("SelectNextRow", "\"Same line as user\"");
		passwdSection.put("GenerateNewVal","\"EachIteration\"");
		passwdSection.put("ParamName","\"passwdVarName\"");
		passwdSection.put("OutOfRangePolicy","\"ContinueCyclic\"");
		passwdSection.put("Type","\"Table\"");
		passwdSection.put("TableLocation","\"Local\"");
		passwdSection.put("Table","\"user.dat\"");
		passwdSection.put("StartRow","\"1\"");
		passwdSection.put("Delimiter","\",\"");

		FileVariable userVariable = ParametersReaderUtils.parseTableParameter("{", "}", userSection, ".");
		FileVariable newUserVariable = ParametersReaderUtils.handleVariableColumns("{", "}", passwdSection, userVariable);

		assertThat(newUserVariable.getName()).isEqualTo("userVarName");
		assertThat(newUserVariable.getColumnsNames()).containsExactly("name","passwd");
		assertThat(newUserVariable.getFirstLineIsColumnName()).isTrue();
		assertThat(newUserVariable.getNumOfFirstRowData()).isEqualTo(2);
		assertThat(newUserVariable.getOrder()).isEqualTo(Optional.of(Variable.VariableOrder.SEQUENTIAL));

	}


	
	@Test
	public void loadColumnOfDataFileTest() {
		String[][] expectedResult = {
				{"value1","value3"},
				{"value4","value6"},
				{"value7","value9"}
		};
		URL url = this.getClass().getResource("../projectTest");
		File file = new File(url.getFile() + File.separator + "param1.dat");
		List<String> columnNames = new ArrayList<>();
		columnNames.add("param1");
		columnNames.add("param3");

		assertThat(ParametersReaderUtils.loadColumnOfDataFile(file.getPath(), columnNames, ",")).containsExactly(expectedResult);
	}

	@Test
	public void loadColumnOfDataFileWithEmptyValueTest() {
		String[][] expectedResult = {
				{"value1","value3"},
				{"value4",""},
				{"","value9"}
		};
		URL url = this.getClass().getResource("../projectTest");
		File file = new File(url.getFile() + File.separator + "param4.dat");
		List<String> columnNames = new ArrayList<>();
		columnNames.add("param1");
		columnNames.add("param3");

		assertThat(ParametersReaderUtils.loadColumnOfDataFile(file.getPath(), columnNames, ",")).containsExactly(expectedResult);
	}

	@Test
	public void loadColumnOfDataFileWithEmptyLineTest() {
		String[][] expectedResult = {
				{"value1","value3"},
				{"value4","value6"},
				{"value7","value9"}
		};
		URL url = this.getClass().getResource("../projectTest");
		File file = new File(url.getFile() + File.separator + "param5.dat");
		List<String> columnNames = new ArrayList<>();
		columnNames.add("param1");
		columnNames.add("param3");

		assertThat(ParametersReaderUtils.loadColumnOfDataFile(file.getPath(), columnNames, ",")).containsExactly(expectedResult);
	}

	
}
