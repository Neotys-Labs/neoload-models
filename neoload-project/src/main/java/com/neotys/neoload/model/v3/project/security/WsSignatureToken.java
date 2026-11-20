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
@JsonPropertyOrder({WsSignatureToken.KEYSTORE, WsSignatureToken.ALIAS, WsSignatureToken.KEY_IDENTIFIER_TYPE, WsSignatureToken.SIGNATURE_ALGORITHM,
		WsSignatureToken.DIGEST_ALGORITHM, WsSignatureToken.CANONICALIZATION_ALGORITHM, WsSignatureToken.PARTS})
@JsonDeserialize(as = ImmutableWsSignatureToken.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WsSignatureToken extends WsKeystoreToken {
	String KEYSTORE = "keystore";
	String ALIAS = "alias";
	String KEY_IDENTIFIER_TYPE = "key_identifier_type";
	String SIGNATURE_ALGORITHM = "signature_algorithm";
	String DIGEST_ALGORITHM = "digest_algorithm";
	String CANONICALIZATION_ALGORITHM = "canonicalization_algorithm";
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

	@JsonProperty(SIGNATURE_ALGORITHM)
	@JsonInclude(value = Include.CUSTOM, valueFilter = WsSignatureAlgorithm.DefaultFilter.class)
	@Value.Default
	default WsSignatureAlgorithm getSignatureAlgorithm() {
		return WsSignatureAlgorithm.RSA_SHA1;
	}

	@JsonProperty(DIGEST_ALGORITHM)
	@JsonInclude(value = Include.CUSTOM, valueFilter = WsDigestAlgorithm.DefaultFilter.class)
	@Value.Default
	default WsDigestAlgorithm getDigestAlgorithm() {
		return WsDigestAlgorithm.SHA1;
	}

	@JsonProperty(CANONICALIZATION_ALGORITHM)
	@JsonInclude(value = Include.CUSTOM, valueFilter = WsCanonicalizationAlgorithm.DefaultFilter.class)
	@Value.Default
	default WsCanonicalizationAlgorithm getCanonicalizationAlgorithm() {
		return WsCanonicalizationAlgorithm.EXCLUSIVE_CANONICAL_XML_OMIT_COMMENTS;
	}

	@JsonProperty(PARTS)
	@Valid
	List<WsPart> getParts();

	class Builder extends ImmutableWsSignatureToken.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
