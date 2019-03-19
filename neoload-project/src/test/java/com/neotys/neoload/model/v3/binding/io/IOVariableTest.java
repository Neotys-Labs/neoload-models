package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.variable.*;
import org.junit.Test;

import java.io.IOException;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.TestCase.assertNotNull;


public class IOVariableTest extends AbstractIOElementsTest {

	@Test
	public void readVariableOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingVariable();
		assertNotNull(expectedProject);

		read("test-variable-only-required", expectedProject);
	}

	private Project buildProjectContainingVariable() {

		final Variable constantVariable = ConstantVariable.builder()
				.name("constant_variable")
				.value("118218")
				.build();

		final Variable fileVariable = FileVariable.builder()
				.name("cities_file")
				.description("cities variable file description")
				.columnNames(newArrayList("City", "Country", "Population", "Longitude", "Latitude"))
				.isFirstLineColumnNames(false)
				.startFromLine(5)
				.delimiter(";")
				.path("data/list_of_cities.csv")
				.changePolicy(FileVariable.ChangePolicy.EACH_USER)
				.scope(FileVariable.Scope.UNIQUE)
				.order(FileVariable.Order.SEQUENTIAL)
				.outOfValue(FileVariable.OutOfValue.STOP)
				.build();

		final Variable fileVariable2 = FileVariable.builder()
				.name("cities2_file")
				.description("cities2 variable file description")
				.isFirstLineColumnNames(true)
				.startFromLine(1)
				.delimiter(";")
				.path("data/list_of_cities.csv")
				.changePolicy(FileVariable.ChangePolicy.EACH_PAGE)
				.scope(FileVariable.Scope.LOCAL)
				.order(FileVariable.Order.RANDOM)
				.outOfValue(FileVariable.OutOfValue.NO_VALUE)
				.build();

		final Variable counterVariable = CounterVariable.builder()
				.name("My Counter")
				.start(0)
				.end(1)
				.increment(10)
				.changePolicy(Variable.ChangePolicy.EACH_ITERATION)
				.scope(Variable.Scope.LOCAL)
				.outOfValue(Variable.OutOfValue.CYCLE)
				.build();

		final Variable randomNumberVariable = RandomNumberVariable.builder()
				.name("MyRandomNumber")
				.min(9999)
				.max(-1)
				.isPredictable(false)
				.changePolicy(Variable.ChangePolicy.EACH_REQUEST)
				.build();

		return Project.builder()
				.name("MyProject")
				.addVariables(constantVariable, fileVariable, fileVariable2, counterVariable, randomNumberVariable)
				.build();
	}
}
