package com.neotys.neoload.model.v3.project.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.AuthenticationDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.AuthenticationSerializer;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value= JsonInclude.Include.NON_EMPTY)
@JsonSerialize(using= AuthenticationSerializer.class)
@JsonDeserialize(using= AuthenticationDeserializer.class)
public interface Authentication {
	String BASIC_AUTHENTICATION 		= "basic_authentication";
	String NTLM_AUTHENTICATION 			= "ntlm_authentication";
	String NEGOCIATE_AUTHENTICATION 	= "negotiate_authentication";

	String LOGIN = "login";
	String PASSWORD = "password";

	@JsonProperty(LOGIN)
	@RequiredCheck(groups = {NeoLoad.class})
	String getLogin();

	@JsonProperty(PASSWORD)
	@RequiredCheck(groups = {NeoLoad.class})
	String getPassword();
}
