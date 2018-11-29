package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.server.ImmutableBasicAuthentication;
import com.neotys.neoload.model.v3.project.server.ImmutableNegociateAuthentication;
import com.neotys.neoload.model.v3.project.server.ImmutableNtlmAuthentication;
import com.neotys.neoload.model.v3.project.server.Server;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static junit.framework.TestCase.assertNotNull;


public class IOServerTest extends AbstractIOElementsTest {

	@Test
	public void readServerOnlyRequired() throws IOException, URISyntaxException {
		final Project expectedProjectWithOnlyRequired = buildProjectContainingServerWithOnlyRequired();
		assertNotNull(expectedProjectWithOnlyRequired);

		read("test-server-only-required", expectedProjectWithOnlyRequired);
	}

	@Test
	public void readServerRequiredAndOptional() throws IOException, URISyntaxException {
		final Project expectedProjectWithRequiredAndOptional = buildProjectContainingServerWithRequiredAndOptional();
		assertNotNull(expectedProjectWithRequiredAndOptional);

		read("test-server-required-and-optional", expectedProjectWithRequiredAndOptional);
	}

	@Test
	public void readServersRequiredAndOptional() throws IOException, URISyntaxException {
		final Project expectedProjectWithRequiredAndOptional = buildProjectContainingServersWithRequiredAndOptional();
		assertNotNull(expectedProjectWithRequiredAndOptional);

		read("test-servers-required-and-optional", expectedProjectWithRequiredAndOptional);
	}

	private Project buildProjectContainingServerWithOnlyRequired() {
		final Server server = Server.builder()
				.name("serverName")
				.host("mypc.intranet.neotys.com")
				.build();

		return Project.builder()
				.name("MyProject")
				.addServers(server)
				.build();
	}

	private Project buildProjectContainingServerWithRequiredAndOptional() {
		final Server server = Server.builder()
				.name("serverName")
				.host("mypc.intranet.neotys.com")
				.port(443)
				.scheme(Server.Scheme.HTTPS)
				.authentication(ImmutableBasicAuthentication.builder()
					.login("neotysuser")
					.password("admin@admin").realm("realm-value").build())
				.build();

		return Project.builder()
				.name("MyProject")
				.addServers(server)
				.build();
	}


	private Project buildProjectContainingServersWithRequiredAndOptional() {
		final Server server1 = Server.builder()
				.name("serverName")
				.host("mypc.intranet.neotys.com")
				.port(443)
				.scheme(Server.Scheme.HTTPS)
				.authentication(ImmutableBasicAuthentication.builder()
						.login("neotysuser")
						.password("admin@admin")
						.realm("realm-value").build())
				.build();

		final Server server2 = Server.builder()
				.name("serverName2")
				.host("mypc2.intranet.neotys.com")
				.port(81)
				.scheme(Server.Scheme.HTTP)
				.authentication(ImmutableNegociateAuthentication.builder()
						.login("neotysusernego")
						.password("admin@adminnego")
						.domain("domain-valuenego").build())
				.build();

		final Server server3 = Server.builder()
				.name("serverName3")
				.host("mypc3.intranet.neotys.com")
				.authentication(ImmutableNtlmAuthentication.builder()
						.login("neotysuserntml")
						.password("admin@adminntml").build())
				.build();

		final Server server4 = Server.builder()
				.name("serverName4")
				.host("mypc4.intranet.neotys.com")
				.build();

		return Project.builder()
				.name("MyProject")
				.addServers(server1, server2, server3, server4)
				.build();
	}
}
