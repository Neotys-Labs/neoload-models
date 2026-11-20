package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.List;
import javax.validation.Valid;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({WsEncryptToken.KEYSTORE, WsEncryptToken.ALIAS, WsEncryptToken.KEY_IDENTIFIER_TYPE, WsEncryptToken.SYMMETRIC_ENCRYPTION_ALGORITHM,
		WsEncryptToken.ASYMMETRIC_ENCRYPTION_ALGORITHM, WsEncryptToken.PARTS})
@JsonDeserialize(as = ImmutableWsEncryptToken.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WsEncryptToken extends WsKeystoreToken {
	String KEYSTORE = "keystore";
	String ALIAS = "alias";
	String KEY_IDENTIFIER_TYPE = "key_identifier_type";
	String SYMMETRIC_ENCRYPTION_ALGORITHM = "symmetric_encryption_algorithm";
	String ASYMMETRIC_ENCRYPTION_ALGORITHM = "asymmetric_encryption_algorithm";
	String PARTS = "parts";

	@JsonProperty(KEYSTORE)
	@RequiredCheck(groups = {NeoLoad.class})
	String getKeystore();

	@JsonProperty(ALIAS)
	@RequiredCheck(groups = {NeoLoad.class})
	String getAlias();

	@JsonProperty(KEY_IDENTIFIER_TYPE)
	@JsonInclude(value = Include.CUSTOM, valueFilter = WsKeyIdentifierType.DefaultFilter.class)
	@Value.Default
	default WsKeyIdentifierType getKeyIdentifierType() {
		return WsKeyIdentifierType.ISSUER_REFERENCE;
	}

	@JsonProperty(SYMMETRIC_ENCRYPTION_ALGORITHM)
	@JsonInclude(value = Include.CUSTOM, valueFilter = WsSymmetricEncryptionAlgorithm.DefaultFilter.class)
	@Value.Default
	default WsSymmetricEncryptionAlgorithm getSymmetricEncryptionAlgorithm() {
		return WsSymmetricEncryptionAlgorithm.AES_128;
	}

	@JsonProperty(ASYMMETRIC_ENCRYPTION_ALGORITHM)
	@JsonInclude(value = Include.CUSTOM, valueFilter = WsAsymmetricEncryptionAlgorithm.DefaultFilter.class)
	@Value.Default
	default WsAsymmetricEncryptionAlgorithm getAsymmetricEncryptionAlgorithm() {
		return WsAsymmetricEncryptionAlgorithm.RSA_V1_5;
	}

	@JsonProperty(PARTS)
	@Valid
	List<WsPart> getParts();

	class Builder extends ImmutableWsEncryptToken.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
