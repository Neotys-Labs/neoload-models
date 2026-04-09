package com.neotys.neoload.model.v3.project.variable;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableSqlVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION, SqlVariable.QUERY, SqlVariable.DRIVER, SqlVariable.URL, SqlVariable.LOGIN, SqlVariable.PASSWORD, SqlVariable.COLUMN_NAMES})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface SqlVariable extends Variable {

	String QUERY = "query";
	String DRIVER = "driver";
	String URL = "url";
	String LOGIN = "login";
	String PASSWORD = "password";
	String COLUMN_NAMES = "column_names";

	@JsonProperty(QUERY)
	@RequiredCheck(groups = {NeoLoad.class})
	String getQuery();

	@JsonProperty(DRIVER)
	@RequiredCheck(groups = {NeoLoad.class})
	String getDriver();

	@JsonProperty(URL)
	@RequiredCheck(groups = {NeoLoad.class})
	String getUrl();

	@JsonProperty(LOGIN)
	Optional<String> getLogin();

	@JsonProperty(PASSWORD)
	Optional<String> getPassword();

	@JsonProperty(COLUMN_NAMES)
	List<String> getColumnNames();

	class Builder extends ImmutableSqlVariable.Builder {}
	static Builder builder() {
		return new Builder();
	}
}