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
		SecretVaultVariable.SECRET_IDENTIFIER})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface SecretVaultVariable extends Variable {

	String PROVIDER_ID = "provider_id";
	String SECRET_IDENTIFIER = "secret_identifier";

	@JsonProperty(PROVIDER_ID)
	@RequiredCheck(groups = {NeoLoad.class})
	String getProviderId();

	@JsonProperty(SECRET_IDENTIFIER)
	@RequiredCheck(groups = {NeoLoad.class})
	String getSecretIdentifier();

	@Override
	@Value.Default
	default Scope getScope() {
		return Scope.LOCAL;
	}

	@Override
	@Value.Default
	default ChangePolicy getChangePolicy() {
		return ChangePolicy.EACH_USER;
	}

	class Builder extends ImmutableSecretVaultVariable.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
