package com.neotys.neoload.model.v3.binding.io;


import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.server.BasicAuthentication;
import com.neotys.neoload.model.v3.project.server.NegotiateAuthentication;
import com.neotys.neoload.model.v3.project.server.NtlmAuthentication;
import com.neotys.neoload.model.v3.project.server.Server;
import org.junit.Test;

import java.io.IOException;

import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static junit.framework.TestCase.assertNotNull;


public class IOServerTest extends AbstractIOElementsTest {

	@Test
	public void readServersOnlyRequired() throws IOException {
		final Project expectedProjectWithOnlyRequired = buildProject(getServersOnlyRequired());
		assertNotNull(expectedProjectWithOnlyRequired);

		read("test-servers-only-required", expectedProjectWithOnlyRequired);
	}

	@Test
	public void writeServersOnlyRequired() throws IOException {
		final Project expectedProjectWithOnlyRequired = buildProject(getServersOnlyRequired());
		assertNotNull(expectedProjectWithOnlyRequired);

		write("test-servers-only-required", expectedProjectWithOnlyRequired);
	}

	@Test
	public void readServersRequiredAndOptional() throws IOException {
		final Project expectedProjectWithRequiredAndOptional = buildProject(getServersRequiredAndOptional());
		assertNotNull(expectedProjectWithRequiredAndOptional);

		read("test-servers-required-and-optional", expectedProjectWithRequiredAndOptional);
	}

	@Test
	public void writeServersRequiredAndOptional() throws IOException {
		final Project expectedProjectWithRequiredAndOptional = buildProject(getServersRequiredAndOptional());
		assertNotNull(expectedProjectWithRequiredAndOptional);

		write("test-servers-required-and-optional", expectedProjectWithRequiredAndOptional);
	}

	private Server getServersOnlyRequired() {
		return Server.builder()
				.name("MyServer")
				.host("myserver.intranet.neotys.com")
				.port("80")
				.build();
	}

	private Server[] getServersRequiredAndOptional() {
		final Server server1 = Server.builder()
				.name("MyServer1")
				.host("myserver1.intranet.neotys.com")
				.scheme(Server.Scheme.HTTPS)
				.port("443")
				.authentication(BasicAuthentication.builder()
						.login("neotysuser")
						.password("admin@admin")
						.build())
				.build();

		final Server server2 = Server.builder()
				.name("MyServer2")
				.host("myserver2.intranet.neotys.com")
				.scheme(Server.Scheme.HTTPS)
				.port("8443")
				.authentication(BasicAuthentication.builder()
						.login("neotysuser")
						.password("admin@admin")
						.realm("realm-value")
						.build())
				.build();

		final Server server3 = Server.builder()
				.name("MyServer3")
				.host("myserver3.intranet.neotys.com")
				.scheme(Server.Scheme.HTTP)
				.port("80")
				.authentication(NegotiateAuthentication.builder()
						.login("neotysusernego")
						.password("admin@adminnego")
						.build())
				.build();

		final Server server4 = Server.builder()
				.name("MyServer4")
				.host("myserver4.intranet.neotys.com")
				.scheme(Server.Scheme.HTTP)
				.port("8080")
				.authentication(NegotiateAuthentication.builder()
						.login("neotysusernego")
						.password("admin@adminnego")
						.domain("domain-valuenego")
						.build())
				.build();

		final Server server5 = Server.builder()
				.name("MyServer5")
				.host("myserver5.intranet.neotys.com")
				.scheme(Server.Scheme.HTTPS)
				.port("8443")
				.authentication(NtlmAuthentication.builder()
						.login("neotysuserntlm")
						.password("admin@adminntlm")
						.build())
				.build();

		final Server server6 = Server.builder()
				.name("MyServer6")
				.host("myserver6.intranet.neotys.com")
				.scheme(Server.Scheme.HTTPS)
				.port("8443")
				.authentication(NtlmAuthentication.builder()
						.login("neotysuserntlm")
						.password("admin@adminntlm")
						.domain("domain-valuentlm")
						.build())
				.build();

		return new Server[]{server1, server2, server3, server4, server5, server6};
	}
}
