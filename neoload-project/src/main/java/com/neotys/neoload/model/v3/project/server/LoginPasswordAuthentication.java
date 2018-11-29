package com.neotys.neoload.model.v3.project.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;

public interface LoginPasswordAuthentication extends Authentication {
	String BASIC_AUTHENTICATION 		= "basic_authentication";
	String NTLM_AUTHENTICATION 			= "ntlm_authentication";
	String NEGOCIATE_AUTHENTICATION 	= "negociate_authentication";

	String LOGIN = "login";
	String PASSWORD = "password";

	@JsonProperty(LOGIN)
	@RequiredCheck
	String getLogin();

	@JsonProperty(PASSWORD)
	@RequiredCheck
	String getPassword();
}
