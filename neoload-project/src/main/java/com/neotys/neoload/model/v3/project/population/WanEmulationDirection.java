package com.neotys.neoload.model.v3.project.population;

import java.util.Optional;

import javax.validation.constraints.Pattern;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.DoubleToPercentageConverter;
import com.neotys.neoload.model.v3.binding.converter.MillisecondsToStringConverter;
import com.neotys.neoload.model.v3.binding.converter.PercentageToDoubleConverter;
import com.neotys.neoload.model.v3.binding.converter.StringToMillisecondsConverter;
import com.neotys.neoload.model.v3.validation.constraints.DigitsCheck;
import com.neotys.neoload.model.v3.validation.constraints.RangeCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

/**
 * The network conditions of one direction (download or upload) of a {@link WanEmulation}. Each value is optional:
 * without bandwidth, the bandwidth is unlimited; without latency or packet loss, there is none.
 */
@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({WanEmulationDirection.BANDWIDTH, WanEmulationDirection.LATENCY, WanEmulationDirection.PACKET_LOSS})
@JsonSerialize(as = ImmutableWanEmulationDirection.class)
@JsonDeserialize(as = ImmutableWanEmulationDirection.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface WanEmulationDirection {
	String BANDWIDTH = "bandwidth";
	String LATENCY = "latency";
	String PACKET_LOSS = "packet_loss";

	/** The value of {@link #getBandwidth()} meaning no bandwidth limit, the same as no bandwidth. */
	String UNLIMITED = "unlimited";

	/** A positive number, with up to 3 decimals, followed by its unit; or {@value #UNLIMITED}. */
	String BANDWIDTH_PATTERN = "^(" + UNLIMITED + "|(?!0+(\\.0+)?[KMG]?bps$)[0-9]+(\\.[0-9]{1,3})?[KMG]?bps)$";

	int MAX_LATENCY = 1000;
	int MAX_PACKET_LOSS = 10;

	/**
	 * The bandwidth, as a positive rate in {@code bps}, {@code Kbps}, {@code Mbps} or {@code Gbps} (for example
	 * {@code 2560Kbps} or {@code 1.5Mbps}), or {@value #UNLIMITED}. Empty: unlimited.
	 */
	@JsonProperty(BANDWIDTH)
	Optional<@Pattern(regexp = BANDWIDTH_PATTERN, groups = {NeoLoad.class},
			message = "must be a positive bandwidth in bps, Kbps, Mbps or Gbps (for example 2560Kbps), or unlimited.") String> getBandwidth();

	/**
	 * The latency added to each packet, in milliseconds, from 0 to {@value #MAX_LATENCY} as in NeoLoad. Empty: none.
	 */
	@JsonProperty(LATENCY)
	@JsonSerialize(converter = MillisecondsToStringConverter.class)
	@JsonDeserialize(converter = StringToMillisecondsConverter.class)
	@RangeCheck(min = 0, max = MAX_LATENCY, groups = {NeoLoad.class})
	Optional<Integer> getLatency();

	/**
	 * The percentage of packets lost, from 0 to {@value #MAX_PACKET_LOSS}% as in NeoLoad. Empty: none.
	 */
	@JsonProperty(PACKET_LOSS)
	@JsonSerialize(converter = DoubleToPercentageConverter.class)
	@JsonDeserialize(converter = PercentageToDoubleConverter.class)
	@DigitsCheck(integer = 2, fraction = 3, groups = {NeoLoad.class})
	@RangeCheck(min = 0, max = MAX_PACKET_LOSS, groups = {NeoLoad.class})
	Optional<Double> getPacketLoss();

	/**
	 * Default values are never written: an unlimited bandwidth, no latency and no packet loss are the same as no
	 * value.
	 */
	@Value.Check
	default WanEmulationDirection dropDefaultValues() {
		final boolean unlimited = getBandwidth().filter(UNLIMITED::equals).isPresent();
		final boolean noLatency = getLatency().filter(latency -> latency == 0).isPresent();
		final boolean noPacketLoss = getPacketLoss().filter(packetLoss -> packetLoss == 0d).isPresent();
		if (!unlimited && !noLatency && !noPacketLoss) {
			return this;
		}
		return ImmutableWanEmulationDirection.copyOf(this)
				.withBandwidth(unlimited ? Optional.empty() : getBandwidth())
				.withLatency(noLatency ? Optional.empty() : getLatency())
				.withPacketLoss(noPacketLoss ? Optional.empty() : getPacketLoss());
	}

	/** Whether the direction has no limit: no bandwidth, latency nor packet loss. */
	@JsonIgnore
	default boolean isUnlimited() {
		return getBandwidth().isEmpty() && getLatency().isEmpty() && getPacketLoss().isEmpty();
	}

	class Builder extends ImmutableWanEmulationDirection.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
