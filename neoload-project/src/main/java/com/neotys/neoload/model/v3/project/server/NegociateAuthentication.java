package com.neotys.neoload.model.v3.project.server;


import java.util.Optional;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({NegociateAuthentication.LOGIN, NegociateAuthentication.PASSWORD, NegociateAuthentication.DOMAIN})
@JsonDeserialize(as = ImmutableNegociateAuthentication.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface NegociateAuthentication extends Authentication {
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";
	public static final String DOMAIN = "domain";
	
	@RequiredCheck(groups={NeoLoad.class})
	@JsonProperty(LOGIN)
	String getLogin();
	@RequiredCheck(groups={NeoLoad.class})
	@JsonProperty(PASSWORD)
	String getPassword();
	@JsonProperty(DOMAIN)
	Optional<String> getDomain();
}
