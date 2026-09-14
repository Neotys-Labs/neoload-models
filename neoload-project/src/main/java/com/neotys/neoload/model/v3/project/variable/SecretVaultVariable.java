package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(as = ImmutableSecretVaultVariable.class)
@JsonPropertyOrder({Variable.NAME, Variable.DESCRIPTION,
		SecretVaultVariable.PROVIDER_ID,
		SecretVaultVariable.SECRET_ID})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface SecretVaultVariable extends Variable {

	String PROVIDER_ID = "provider_id";
	String SECRET_ID = "secret_id";

	@JsonProperty(PROVIDER_ID)
	@RequiredCheck(groups = {NeoLoad.class})
	String getProviderId();

	@JsonProperty(SECRET_ID)
	@RequiredCheck(groups = {NeoLoad.class})
	String getSecretId();

	class Builder extends ImmutableSecretVaultVariable.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
