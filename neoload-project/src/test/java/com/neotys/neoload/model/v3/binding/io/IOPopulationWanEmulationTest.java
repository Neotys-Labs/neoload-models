package com.neotys.neoload.model.v3.binding.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import com.neotys.neoload.model.v3.project.population.WanEmulation;
import com.neotys.neoload.model.v3.project.population.WanEmulationDirection;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import org.junit.Test;

/**
 * WAN emulation of the User Paths of a Population: the "WAN Emulation" columns of the designer's Population table.
 */
public class IOPopulationWanEmulationTest extends AbstractIOElementsTest {

	private static Project getWanEmulation() {
		return Project.builder()
				.schemaVersion("3.2")
				.name("MyProject")
				.addPopulations(Population.builder()
						.name("MyPopulation1")
						.addUserPaths(UserPathPolicy.builder()
								.name("MyUserPath11")
								.distribution(75.4)
								.wanEmulation(WanEmulation.builder()
										.name("ADSL 2048")
										.download(WanEmulationDirection.builder()
												.bandwidth("2560Kbps")
												.latency(40)
												.packetLoss(1.5)
												.build())
										.upload(WanEmulationDirection.builder().bandwidth("256Kbps").build())
										.build())
								.build())
						.addUserPaths(UserPathPolicy.builder()
								.name("MyUserPath12")
								.distribution(24.6)
								.wanEmulation(WanEmulation.builder()
										.download(WanEmulationDirection.builder().latency(100).build())
										.build())
								.build())
						.build())
				.build();
	}

	private static Project getDefaults() {
		return Project.builder()
				.schemaVersion("3.2")
				.name("MyProject")
				.addPopulations(Population.builder()
						.name("MyPopulation1")
						.addUserPaths(UserPathPolicy.builder()
								.name("MyUserPath11")
								.build())
						.build())
				.build();
	}

	@Test
	public void readWanEmulation() throws IOException {
		assertRead("test-populations-wan-emulation", getWanEmulation());
	}

	@Test
	public void writeWanEmulation() throws IOException {
		assertWrite("test-populations-wan-emulation", getWanEmulation());
	}

	@Test
	public void readUnlimitedWanEmulationIsNoWanEmulation() throws IOException {
		assertRead("test-populations-wan-emulation-defaults", getDefaults());
	}

	@Test
	public void writeUnlimitedWanEmulationWritesNothing() throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile("test-populations-wan-emulation-defaults", "yaml"));

		final String written = new IO().write(descriptor, IO.Format.YAML);

		assertFalse(written, written.contains(UserPathPolicy.WAN_EMULATION));
	}

	@Test
	public void readPopulationWithoutWanEmulationHasNone() throws IOException {
		final UserPathPolicy userPath = new IO().read(getFile("test-populations-required-and-optional", "yaml"))
				.getProject().getPopulations().get(0).getUserPaths().get(0);

		assertFalse(userPath.getWanEmulation().isPresent());
	}

	@Test
	public void readLatencyWithoutUnitIsInMilliseconds() throws IOException {
		final Project project = new IO().read("schemaVersion: \"3.2\"\nname: MyProject\npopulations:\n- name: MyPopulation1\n"
				+ "  user_paths:\n  - name: MyUserPath11\n    wan_emulation:\n      upload:\n        latency: 40\n", Project.class);

		final WanEmulation wanEmulation = project.getPopulations().get(0).getUserPaths().get(0).getWanEmulation().orElseThrow();
		assertEquals(Integer.valueOf(40), wanEmulation.getUpload().orElseThrow().getLatency().orElseThrow());
	}

	@Test
	public void readBandwidthWithWrongUnitFailsValidation() throws IOException {
		final String message = validationFailure("test-populations-wan-emulation-invalid-bandwidth");

		assertTrue(message, message.contains("'project.populations[0].user_paths[0].wan_emulation.download.bandwidth': must be a positive bandwidth in bps, Kbps, Mbps or Gbps"));
	}

	@Test
	public void readZeroBandwidthFailsValidation() throws IOException {
		final String message = validationFailure("test-populations-wan-emulation-zero-bandwidth");

		assertTrue(message, message.contains("wan_emulation.download.bandwidth': must be a positive bandwidth"));
	}

	@Test
	public void readLatencyAboveOneSecondFailsValidation() throws IOException {
		final String message = validationFailure("test-populations-wan-emulation-invalid-latency");

		assertTrue(message, message.contains("wan_emulation.download.latency': must be between 0 and 1000."));
	}

	@Test
	public void readPacketLossAboveTenPercentFailsValidation() throws IOException {
		final String message = validationFailure("test-populations-wan-emulation-invalid-packet-loss");

		assertTrue(message, message.contains("wan_emulation.download.packet_loss': must be between 0 and 10."));
	}

	@Test
	public void readLatencyInSecondsFails() {
		final String message = readFailure("test-populations-wan-emulation-latency-unit");

		assertTrue(message, message.contains("'1s' is not a valid duration in milliseconds"));
	}

	@Test
	public void readUnknownWanEmulationSettingFails() {
		final String message = readFailure("test-populations-wan-emulation-unknown-key");

		assertTrue(message, message.contains("jitter"));
	}

	private String validationFailure(final String fileName) throws IOException {
		final ProjectDescriptor descriptor = new IO().read(getFile(fileName, "yaml"));

		final Validation validation = new Validator().validate(descriptor, NeoLoad.class);

		assertFalse(validation.isValid());
		return validation.getMessage().orElse("");
	}

	private String readFailure(final String fileName) {
		try {
			new IO().read(getFile(fileName, "yaml"));
			fail("Expected reading " + fileName + " to fail");
			return "";
		} catch (final IOException e) {
			return e.getMessage();
		}
	}
}
