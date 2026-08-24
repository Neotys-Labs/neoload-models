package com.neotys.neoload.model.v3.binding.io;


import static com.neotys.neoload.model.v3.project.variable.Variable.ChangePolicy.EACH_USER;
import static com.neotys.neoload.model.v3.project.variable.Variable.Order.SEQUENTIAL;
import static com.neotys.neoload.model.v3.project.variable.Variable.OutOfValue.STOP;
import static com.neotys.neoload.model.v3.project.variable.Variable.Scope.UNIQUE;
import static junit.framework.TestCase.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.variable.SqlVariable;

public class IOSqlTest extends AbstractIOElementsTest {

	@Test
	public void readSqlOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingSqlOnlyRequired();
		assertNotNull(expectedProject);

		read("test-sql-only-required", expectedProject);
	}

	@Test
	public void writeSqlOnlyRequired() throws IOException {
		final Project expectedProject = buildProjectContainingSqlOnlyRequired();
		assertNotNull(expectedProject);

		write("test-sql-only-required", expectedProject);
	}

	@Test
	public void readSqlRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingSqlRequiredAndOptional();
		assertNotNull(expectedProject);

		read("test-sql-required-and-optional", expectedProject);
	}

	@Test
	public void writeSqlRequiredAndOptional() throws IOException {
		final Project expectedProject = buildProjectContainingSqlRequiredAndOptional();
		assertNotNull(expectedProject);

		write("test-sql-required-and-optional", expectedProject);
	}

	private Project buildProjectContainingSqlOnlyRequired() {
		final SqlVariable minimalSqlVariable = SqlVariable.builder()
				.name("MySql")
				.url("jdbc:mysql://localhost:3306/mydb")
				.query("SELECT username, email FROM users")
				.build();

		return Project.builder()
				.name("MyProject")
				.addVariables(minimalSqlVariable)
				.build();
	}

	private Project buildProjectContainingSqlRequiredAndOptional() {
		final SqlVariable fullSqlVariable = SqlVariable.builder()
				.name("MySql")
				.description("MySqlDescription")
				.driver("com.mysql.jdbc.Driver")
				.url("jdbc:mysql://localhost:3306/mydb")
				.login("login_admin")
				.password("password_admin")
				.query("SELECT username, email FROM users")
				.addColumnNames("username", "email")
				.changePolicy(EACH_USER)
				.scope(UNIQUE)
				.order(SEQUENTIAL)
				.outOfValue(STOP)
				.build();

		return Project.builder()
				.name("MyProject")
				.addVariables(fullSqlVariable)
				.build();
	}
}
