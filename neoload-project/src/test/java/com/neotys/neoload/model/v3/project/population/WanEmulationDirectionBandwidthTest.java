package com.neotys.neoload.model.v3.project.population;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

/**
 * {@link WanEmulationDirection#BANDWIDTH_PATTERN} compares numbers with a regular expression: it is checked here
 * against the arithmetic, a positive value up to {@link WanEmulationDirection#MAX_BANDWIDTH_BPS} bps.
 */
public class WanEmulationDirectionBandwidthTest {

	private static final Pattern BANDWIDTH = Pattern.compile(WanEmulationDirection.BANDWIDTH_PATTERN);
	private static final Pattern ANY_RATE = Pattern.compile("^([0-9]+(\\.[0-9]{1,3})?)([KMG]?)bps$");
	private static final Map<String, BigDecimal> UNITS = Map.of("", BigDecimal.ONE, "K", BigDecimal.valueOf(1_000L),
			"M", BigDecimal.valueOf(1_000_000L), "G", BigDecimal.valueOf(1_000_000_000L));
	private static final BigDecimal MAX = BigDecimal.valueOf(WanEmulationDirection.MAX_BANDWIDTH_BPS);

	@Test
	public void acceptsUpToTheNeoLoadMaximum() {
		for (final String valid : List.of("unlimited", "1bps", "0.001bps", "2560Kbps", "1.5Mbps", "2147483640bps",
				"2147483640.000bps", "2147483.64Kbps", "2147483.640Kbps", "2147.483Mbps", "2.147Gbps", "2Gbps",
				"002.147Gbps")) {
			assertEquals(valid, true, BANDWIDTH.matcher(valid).matches());
		}
		for (final String invalid : List.of("0bps", "0.000Gbps", "2147483640.001bps", "2147483641bps", "3000000000bps",
				"2147483.641Kbps", "2147484Kbps", "2147.484Mbps", "2148Mbps", "2.148Gbps", "3Gbps", "2560kbps",
				"1.0001Mbps")) {
			assertEquals(invalid, false, BANDWIDTH.matcher(invalid).matches());
		}
	}

	@Test
	public void agreesWithTheArithmeticAroundTheMaximumOfEachUnit() {
		final List<String> values = new ArrayList<>();
		for (final Map.Entry<String, BigDecimal> unit : UNITS.entrySet()) {
			final BigDecimal max = MAX.divide(unit.getValue());
			for (int step = -2_000; step <= 2_000; step++) {
				final BigDecimal value = max.add(BigDecimal.valueOf(step, 3));
				for (int scale = 0; scale <= 3; scale++) {
					final String number = value.setScale(scale, RoundingMode.DOWN).toPlainString();
					values.add(number + unit.getKey() + "bps");
					values.add("00" + number + unit.getKey() + "bps");
				}
			}
		}
		values.forEach(WanEmulationDirectionBandwidthTest::assertAgreesWithTheArithmetic);
	}

	@Test
	public void agreesWithTheArithmeticOnRandomValues() {
		final Random random = new Random(41741L);
		for (int i = 0; i < 200_000; i++) {
			final StringBuilder value = new StringBuilder(Long.toString(Math.floorMod(random.nextLong(),
					(long) Math.pow(10, 1 + random.nextInt(11)))));
			final int decimals = random.nextInt(4);
			if (decimals > 0) {
				value.append('.').append(String.format("%0" + decimals + "d", random.nextInt((int) Math.pow(10, decimals))));
			}
			final String unit = List.of("", "K", "M", "G").get(random.nextInt(4));
			assertAgreesWithTheArithmetic(value + unit + "bps");
		}
	}

	private static void assertAgreesWithTheArithmetic(final String value) {
		final Matcher rate = ANY_RATE.matcher(value);
		if (!rate.matches()) {
			return;
		}
		final BigDecimal bitsPerSecond = new BigDecimal(rate.group(1)).multiply(UNITS.get(rate.group(3)));
		final boolean expected = bitsPerSecond.signum() > 0 && bitsPerSecond.compareTo(MAX) <= 0;
		assertEquals(value, expected, BANDWIDTH.matcher(value).matches());
	}
}
