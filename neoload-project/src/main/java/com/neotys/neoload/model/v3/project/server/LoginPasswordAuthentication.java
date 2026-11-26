package com.neotys.neoload.model.v3.project.server;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An authentication with a login and a password. {@code basic_authentication}, {@code ntlm_authentication} and
 * {@code negotiate_authentication} require both; an item of {@code authentications} accepts empty ones, as the
 * Authorization table of the NeoLoad GUI does.
 */
public interface LoginPasswordAuthentication extends Authentication {

	String BASIC_AUTHENTICATION 		= "basic_authentication";
	String NEGOTIATE_AUTHENTICATION 	= "negotiate_authentication";
	String NTLM_AUTHENTICATION 			= "ntlm_authentication";

	String LOGIN = "login";
	String PASSWORD = "password";

	@JsonProperty(LOGIN)
	String getLogin();

	@JsonProperty(PASSWORD)
	String getPassword();
}
