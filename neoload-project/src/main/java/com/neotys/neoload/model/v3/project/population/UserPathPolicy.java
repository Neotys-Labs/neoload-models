package com.neotys.neoload.model.v3.project.population;

import java.util.Optional;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.converter.DoubleToPercentageConverter;
import com.neotys.neoload.model.v3.binding.converter.PercentageToDoubleConverter;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.DigitsCheck;
import com.neotys.neoload.model.v3.validation.constraints.RangeCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, UserPathPolicy.DISTRIBUTION, UserPathPolicy.BROWSER, UserPathPolicy.CACHE, UserPathPolicy.WAN_EMULATION})
@JsonSerialize(as = ImmutableUserPathPolicy.class)
@JsonDeserialize(as = ImmutableUserPathPolicy.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface UserPathPolicy extends Element {
	String DISTRIBUTION = "distribution";
	String BROWSER = "browser";
	String CACHE = "cache";
	String WAN_EMULATION = "wan_emulation";

	String AS_RECORDED = "as_recorded";
	String NEW_USER = "new_user";
	String RETURNING_USER = "returning_user";

	Cache DEFAULT_CACHE = Cache.AS_RECORDED;

	/**
	 * How the Virtual Users handle the browser cache, as the "Handle Cache" column of the designer's
	 * Population table.
	 */
	enum Cache {
		/** The cache behaves as during the recording. */
		@JsonProperty(UserPathPolicy.AS_RECORDED)
		AS_RECORDED,
		/** The cache is empty: a first visit. */
		@JsonProperty(UserPathPolicy.NEW_USER)
		NEW_USER,
		/** The cache is already filled: the user came before. */
		@JsonProperty(UserPathPolicy.RETURNING_USER)
		RETURNING_USER
	}

	@JsonSerialize(converter=DoubleToPercentageConverter.class)
	@JsonDeserialize(converter=PercentageToDoubleConverter.class)
	@JsonProperty(DISTRIBUTION)
	@DigitsCheck(integer=3, fraction=1, groups={NeoLoad.class})
	@RangeCheck(min=0, max=100, groups={NeoLoad.class})
	Optional<Double> getDistribution();

	/**
	 * The browser emulated by the Virtual Users. Empty: the recorded browser.
	 */
	@JsonProperty(BROWSER)
	@Valid
	Optional<Browser> getBrowser();

	@JsonProperty(CACHE)
	@JsonInclude(value = Include.CUSTOM, valueFilter = DefaultCacheFilter.class)
	@Value.Default
	default Cache getCache() {
		return DEFAULT_CACHE;
	}

	/**
	 * The WAN emulation of the Virtual Users. Empty: none, the network is not limited.
	 */
	@JsonProperty(WAN_EMULATION)
	@Valid
	Optional<WanEmulation> getWanEmulation();

	/**
	 * Defaults are not written back as empty objects: a browser left with all its defaults is the recorded browser,
	 * the same as no browser; a WAN emulation without any limit is the same as no WAN emulation, whatever its name.
	 */
	@Value.Check
	default UserPathPolicy dropDefaultBrowserAndWanEmulation() {
		final boolean defaultBrowser = getBrowser().filter(Browser.builder().build()::equals).isPresent();
		final boolean unlimitedWanEmulation = getWanEmulation().filter(WanEmulation::isUnlimited).isPresent();
		if (!defaultBrowser && !unlimitedWanEmulation) {
			return this;
		}
		return ImmutableUserPathPolicy.copyOf(this)
				.withBrowser(defaultBrowser ? Optional.empty() : getBrowser())
				.withWanEmulation(unlimitedWanEmulation ? Optional.empty() : getWanEmulation());
	}

	// Jackson value filter excluding the default cache from serialization.
	class DefaultCacheFilter {
		@Override
		public boolean equals(final Object value) { return DEFAULT_CACHE.equals(value); }
		@Override
		public int hashCode() { return DEFAULT_CACHE.hashCode(); }
	}

	class Builder extends ImmutableUserPathPolicy.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
