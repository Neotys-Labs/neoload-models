package com.neotys.neoload.model.readers.loadrunner.filereader;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_READER;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URL;

import org.junit.Test;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.loadrunner.MethodUtils;
import com.neotys.neoload.model.repository.ConstantVariable;
import com.neotys.neoload.model.repository.CounterNumberVariable;
import com.neotys.neoload.model.repository.FileVariable;
import com.neotys.neoload.model.repository.RandomNumberVariable;
import com.neotys.neoload.model.repository.Variable;
@SuppressWarnings("squid:S2699")
public class ParameterFileReaderTest {

	@Test
	public void loadTest() {
		final URL url = this.getClass().getResource("../projectTest");
		final File projectFolder = new File(url.getFile());
		
		final ProjectFileReader projectFileReader = new ProjectFileReader(LOAD_RUNNER_READER, new TestEventListener(), projectFolder);
		ParameterFileReader pfr = new ParameterFileReader(LOAD_RUNNER_READER,projectFileReader, projectFolder);

		assertThat(pfr.getVariable("param inexistant")).isNull();
		assertThat(pfr.getVariable("NewParam")).isNotNull();
	}
	
	@Test
	public void tableVariableNameTest() {
		URL url = this.getClass().getResource("../projectTest");
		File projectFolder = new File(url.getFile());		
		final ProjectFileReader projectFileReader = new ProjectFileReader(LOAD_RUNNER_READER, new TestEventListener(), projectFolder);
		ParameterFileReader pfr = new ParameterFileReader(LOAD_RUNNER_READER,projectFileReader, projectFolder);

		assertThat(MethodUtils.getCorrespondingVariableNameForNL("param inexistant")).isEqualTo("param inexistant");

		assertThat(MethodUtils.getCorrespondingVariableNameForNL("NewParam")).isEqualTo("NewParam.param5");
		assertThat(MethodUtils.getCorrespondingVariableNameForNL("NewParam3")).isEqualTo("NewParam.Col 3");
		assertThat(pfr.getVariable("TableParam")).isNotNull();
		assertThat(pfr.getVariable("TableParam")).isInstanceOf(FileVariable.class);
		assertThat(pfr.getVariable("Random")).isNotNull();
		assertThat(pfr.getVariable("Random")).isInstanceOf(RandomNumberVariable.class);
		assertThat(((RandomNumberVariable)pfr.getVariable("Random")).getMinValue()).isEqualTo(5);
		assertThat(((RandomNumberVariable)pfr.getVariable("Random")).getMaxValue()).isEqualTo(500);
		assertThat(pfr.getVariable("Unique")).isNotNull();
		assertThat(pfr.getVariable("Unique")).isInstanceOf(CounterNumberVariable.class);
		assertThat(((CounterNumberVariable)pfr.getVariable("Unique")).getStartValue()).isEqualTo(4);
		assertThat(pfr.getVariable("param_custom")).isNotNull();
		assertThat(pfr.getVariable("param_custom")).isInstanceOf(ConstantVariable.class);

	}

	@Test
	public void tableVariableColumnsWithSameFileTest() {
		URL url = this.getClass().getResource("../projectTest");
		File projectFolder = new File(url.getFile());		
		final ProjectFileReader projectFileReader = new ProjectFileReader(LOAD_RUNNER_READER, new TestEventListener(), projectFolder);
		ParameterFileReader pfr = new ParameterFileReader(LOAD_RUNNER_READER,projectFileReader, projectFolder);

		assertThat(pfr.getVariable("user")).isNotNull();
		assertThat(pfr.getVariable("user")).isInstanceOf(FileVariable.class);
		assertThat(((FileVariable)pfr.getVariable("user")).getColumnsNames()).containsExactly("name","passwd");
		assertThat(((FileVariable)pfr.getVariable("user")).getData()).isEmpty();

	}

	@Test
	public void tableVariableColumnsWithDifferentFileTest() {
		URL url = this.getClass().getResource("../projectTest");
		File projectFolder = new File(url.getFile());		
		final ProjectFileReader projectFileReader = new ProjectFileReader(LOAD_RUNNER_READER, new TestEventListener(), projectFolder);
		ParameterFileReader pfr = new ParameterFileReader(LOAD_RUNNER_READER,projectFileReader, projectFolder);

		assertThat(pfr.getVariable("NewParam")).isNotNull();
		assertThat(pfr.getVariable("NewParam")).isInstanceOf(FileVariable.class);
		assertThat(((FileVariable)pfr.getVariable("NewParam")).getColumnsNames()).containsExactly("param5","Col 3");
		assertThat(((FileVariable)pfr.getVariable("NewParam")).getData()).isNotEmpty();
		String[][] expectedData = {{"value12", "value31"}, {"value15", "value32"}};
		assertThat(((FileVariable)pfr.getVariable("NewParam")).getData().get()).contains(expectedData);

	}

	@Test
	public void tableVariableColumnsWithDifferentFileSizeTest() {
		URL url = this.getClass().getResource("../projectTest");
		File projectFolder = new File(url.getFile());		
		final ProjectFileReader projectFileReader = new ProjectFileReader(LOAD_RUNNER_READER, new TestEventListener(), projectFolder);
		ParameterFileReader pfr = new ParameterFileReader(LOAD_RUNNER_READER,projectFileReader, projectFolder);

		assertThat(pfr.getVariable("param1")).isNotNull();
		assertThat(pfr.getVariable("param1")).isInstanceOf(FileVariable.class);
		assertThat(((FileVariable)pfr.getVariable("param1")).getColumnsNames()).containsExactly("param2","param6");
		assertThat(((FileVariable)pfr.getVariable("param1")).getData()).isNotEmpty();
		String[][] expectedData = {{"value2", "value13"}, {"value5", "value16"}, {"value8",""}};
		assertThat(((FileVariable)pfr.getVariable("param1")).getData().get()).contains(expectedData);
	}

	@Test
	public void tableVariableColumnsWithDifferentFileSize2Test() {
		URL url = this.getClass().getResource("../projectTest");
		File projectFolder = new File(url.getFile());		
		final ProjectFileReader projectFileReader = new ProjectFileReader(LOAD_RUNNER_READER, new TestEventListener(), projectFolder);
		ParameterFileReader pfr = new ParameterFileReader(LOAD_RUNNER_READER,projectFileReader, projectFolder);

		assertThat(pfr.getVariable("param2_1")).isNotNull();
		assertThat(pfr.getVariable("param2_1")).isInstanceOf(FileVariable.class);
		assertThat(((FileVariable)pfr.getVariable("param2_1")).getColumnsNames()).containsExactly("param6","param2");
		assertThat(((FileVariable)pfr.getVariable("param2_1")).getData()).isNotEmpty();
		String[][] expectedData = {{"value13", "value2"}, {"value16", "value5"}};
		assertThat(((FileVariable)pfr.getVariable("param2_1")).getData().get()).contains(expectedData);

	}

	@Test
	public void getPolicyTest() {
		URL url = this.getClass().getResource("../projectTest");
		File projectFolder = new File(url.getFile());		
		final ProjectFileReader projectFileReader = new ProjectFileReader(LOAD_RUNNER_READER, new TestEventListener(), projectFolder);
		final ParameterFileReader reader = new ParameterFileReader(LOAD_RUNNER_READER, projectFileReader, projectFolder);
		assertThat(reader.getPolicy("EachOccurrence", "")).isEqualTo(Variable.VariablePolicy.EACH_USE);
		assertThat(reader.getPolicy("EachIteration", "")).isEqualTo(Variable.VariablePolicy.EACH_ITERATION);
		assertThat(reader.getPolicy("Once", "")).isEqualTo(Variable.VariablePolicy.EACH_VUSER);
		assertThat(reader.getPolicy("INVALID", "")).isEqualTo(Variable.VariablePolicy.EACH_USE);
	}
	
	
}
