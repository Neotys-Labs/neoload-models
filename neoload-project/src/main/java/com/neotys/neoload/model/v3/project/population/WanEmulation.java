package com.neotys.neoload.model.v3.project.population;

import java.util.Optional;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * The WAN emulation of a User Path in a Population, as the "WAN Emulation" columns of the designer's Population
 * table: the bandwidth, latency and packet loss of the download and of the upload.
 * <p>
 * The profile is stored by value, like in a NeoLoad project: {@link #getName()} is only the label shown in NeoLoad.
 * A direction left out has no limit. The mobile profiles of NeoLoad, whose values depend on a signal strength, are
 * written with the values of their selected signal strength.
 */
@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({WanEmulation.NAME, WanEmulation.DOWNLOAD, WanEmulation.UPLOAD})
@JsonSerialize(as = ImmutableWanEmulation.class)
@JsonDeserialize(as = ImmutableWanEmulation.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WanEmulation {
	String NAME = "name";
	String DOWNLOAD = "download";
	String UPLOAD = "upload";

	/**
	 * The label of the WAN emulation profile shown in NeoLoad. Empty: the profile is shown as customized.
	 */
	@JsonProperty(NAME)
	Optional<String> getName();

	/** The network conditions of the responses, from the server to the Virtual Users. Empty: no limit. */
	@JsonProperty(DOWNLOAD)
	@Valid
	Optional<WanEmulationDirection> getDownload();

	/** The network conditions of the requests, from the Virtual Users to the server. Empty: no limit. */
	@JsonProperty(UPLOAD)
	@Valid
	Optional<WanEmulationDirection> getUpload();

	/**
	 * A direction without any limit is the same as no direction: drop it so it is not written back as an empty
	 * object.
	 */
	@Value.Check
	default WanEmulation dropUnlimitedDirections() {
		final boolean unlimitedDownload = getDownload().filter(WanEmulationDirection::isUnlimited).isPresent();
		final boolean unlimitedUpload = getUpload().filter(WanEmulationDirection::isUnlimited).isPresent();
		if (!unlimitedDownload && !unlimitedUpload) {
			return this;
		}
		return ImmutableWanEmulation.copyOf(this)
				.withDownload(unlimitedDownload ? Optional.empty() : getDownload())
				.withUpload(unlimitedUpload ? Optional.empty() : getUpload());
	}

	/** Whether the profile has no limit in either direction: the same as no WAN emulation, whatever its name. */
	@JsonIgnore
	default boolean isUnlimited() {
		return getDownload().isEmpty() && getUpload().isEmpty();
	}

	class Builder extends ImmutableWanEmulation.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
