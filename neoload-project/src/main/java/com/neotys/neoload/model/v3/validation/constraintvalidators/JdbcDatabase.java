package com.neotys.neoload.model.v3.validation.constraintvalidators;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The databases NeoLoad knows how to connect to, with the JDBC url prefix and the driver class names
 * accepted for each of them. Mirrors the protocols and the drivers of the NeoLoad SqlDatabaseConnection,
 * which is not visible from this project.
 */
enum JdbcDatabase {
	MYSQL("jdbc:mysql://", "com.mysql.cj.jdbc.Driver", "com.mysql.jdbc.Driver"),
	POSTGRESQL("jdbc:postgresql://", "org.postgresql.Driver"),
	DB2("jdbc:db2://", "com.ibm.db2.jcc.DB2Driver"),
	ORACLE("jdbc:oracle:thin:@", "oracle.jdbc.driver.OracleDriver", "oracle.jdbc.OracleDriver"),
	SQL_SERVER("jdbc:sqlserver://", "com.microsoft.sqlserver.jdbc.SQLServerDriver");

	private final String urlPrefix;
	private final List<String> drivers;

	JdbcDatabase(final String urlPrefix, final String... drivers) {
		this.urlPrefix = urlPrefix;
		this.drivers = Arrays.asList(drivers);
	}

	static Optional<JdbcDatabase> ofUrl(final String url) {
		return Arrays.stream(values()).filter(database -> url.startsWith(database.urlPrefix)).findFirst();
	}

	static Optional<JdbcDatabase> ofDriver(final String driver) {
		return Arrays.stream(values()).filter(database -> database.drivers.contains(driver)).findFirst();
	}
}
