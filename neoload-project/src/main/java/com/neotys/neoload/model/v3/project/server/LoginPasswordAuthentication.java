package com.neotys.neoload.model.v3.project.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;

public interface LoginPasswordAuthentication extends Authentication {
	String LOGIN = "login";
	String PASSWORD = "password";

	@JsonProperty(LOGIN)
	@RequiredCheck
	String getLogin();

	@JsonProperty(PASSWORD)
	@RequiredCheck
	String getPassword();
}
