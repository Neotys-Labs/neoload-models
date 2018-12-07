package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.server.BasicAuthentication;
import com.neotys.neoload.model.v3.project.server.NegotiateAuthentication;
import com.neotys.neoload.model.v3.project.server.NtlmAuthentication;
import com.neotys.neoload.model.v3.project.server.Server;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;


public class IOServerTest extends AbstractIOElementsTest {

	@Test
	public void readServerOnlyRequired() throws IOException {
		final Project expectedProjectWithOnlyRequired = buildProjectContainingServerWithOnlyRequired();
		assertNotNull(expectedProjectWithOnlyRequired);

		read("test-server-only-required", expectedProjectWithOnlyRequired);
	}

	@Test
	public void readServerRequiredAndOptional() throws IOException {
		final Project expectedProjectWithRequiredAndOptional = buildProjectContainingServerWithRequiredAndOptional();
		assertNotNull(expectedProjectWithRequiredAndOptional);

		read("test-server-required-and-optional", expectedProjectWithRequiredAndOptional);
	}

	@Test
	public void readServersRequiredAndOptional() throws IOException {
		final Project expectedProjectWithRequiredAndOptional = buildProjectContainingServersWithRequiredAndOptional();
		assertNotNull(expectedProjectWithRequiredAndOptional);

		read("test-servers-required-and-optional", expectedProjectWithRequiredAndOptional);
	}

	private Project buildProjectContainingServerWithOnlyRequired() {
		final Server server = Server.builder()
				.name("serverName")
				.host("mypc.intranet.neotys.com")
				.port(80L)
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
				.port(443L)
				.scheme(Server.Scheme.HTTPS)
				.authentication(BasicAuthentication.builder()
						.login("neotysuser")
						.password("admin@admin").realm("realm-value")
						.build())
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
				.port(443L)
				.scheme(Server.Scheme.HTTPS)
				.authentication(BasicAuthentication.builder()
						.login("neotysuser")
						.password("admin@admin")
						.realm("realm-value")
						.build())
				.build();

		final Server server2 = Server.builder()
				.name("serverName2")
				.host("mypc2.intranet.neotys.com")
				.port(81L)
				.scheme(Server.Scheme.HTTP)
				.authentication(NegotiateAuthentication.builder()
						.login("neotysusernego")
						.password("admin@adminnego")
						.domain("domain-valuenego")
						.build())
				.build();

		final Server server3 = Server.builder()
				.name("serverName3")
				.host("mypc3.intranet.neotys.com")
				.port(80L)
				.authentication(NtlmAuthentication.builder()
						.login("neotysuserntlm")
						.password("admin@adminntlm")
						.build())
				.build();

		final Server server4 = Server.builder()
				.name("serverName4")
				.host("mypc4.intranet.neotys.com")
				.port(80L)
				.build();

		return Project.builder()
				.name("MyProject")
				.addServers(server1, server2, server3, server4)
				.build();
	}
}
