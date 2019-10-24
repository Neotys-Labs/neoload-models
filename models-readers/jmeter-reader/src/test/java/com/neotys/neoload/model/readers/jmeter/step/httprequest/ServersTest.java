package com.neotys.neoload.model.readers.jmeter.step.httprequest;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.server.BasicAuthentication;
import com.neotys.neoload.model.v3.project.server.NegotiateAuthentication;
import com.neotys.neoload.model.v3.project.server.NtlmAuthentication;
import com.neotys.neoload.model.v3.project.server.Server;
import org.apache.jmeter.protocol.http.control.AuthManager;
import org.apache.jmeter.protocol.http.control.Authorization;
import org.apache.jorphan.collections.HashTree;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.spy;

public class ServersTest {

    @Before
    public void before()   {
        TestEventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        Servers.clear();
    }

    @Test
    public void testAddServers(){
        Servers.addServer("toto","blazedemo",8080,"HTTP",new HashTree());
        Set<Server> server = Servers.getServers();
        assertEquals(server.iterator().next().getPort(),"8080");
        assertEquals(server.iterator().next().getHost(),"blazedemo");
        assertEquals(server.iterator().next().getScheme().name(),"HTTP");
    }

    @Test
    public void testAddServersWithoutProtocol(){
        Servers.addServer("toto","host",8080,null,new HashTree());
        Set<Server> server = Servers.getServers();
        assertEquals(server.iterator().next().getPort(),"8080");
        assertEquals(server.iterator().next().getHost(), "host");
        assertEquals(server.iterator().next().getScheme().name(),"HTTP");
    }

    @Test
    public void testAddServersWithoutHost(){
        Servers.addServer("toto",null,8080,"HTTP",new HashTree());
        Set<Server> server = Servers.getServers();
        assertEquals(server.iterator().next().getPort(),"8080");
        assertNull(server.iterator().next().getHost());
        assertEquals(server.iterator().next().getScheme().name(),"HTTP");
    }

   @Test
   public void testCheckAuthentificationBasic(){
       AuthManager authManager = new AuthManager();
       Authorization authorization = new Authorization();
       authorization.setURL("http://localhost:80/");
       authorization.setMechanism(AuthManager.Mechanism.BASIC);
       authorization.setPass("thomas");
       authorization.setUser("martinez");
       authorization.setRealm("localhost");

       authManager.addAuth(authorization);

       HashTree hashTree = new HashTree();
       hashTree.add(authManager);

       Servers.addServer("auth","localhost",80,"http",hashTree);
       Server.Builder expect = Server.builder()
               .name("auth")
               .port(Integer.toString(80))
               .host("localhost")
               .scheme(Server.Scheme.HTTP)
               .authentication(BasicAuthentication.builder()
                       .login(authorization.getUser())
                       .password(authorization.getPass())
                       .realm(authorization.getRealm())
                       .build());
       assertEquals(expect.build().getName(),Servers.checkServer(expect.build()));
   }

    @Test
    public void testCheckAuthentificationDigest() {
        AuthManager authManager = new AuthManager();
        Authorization authorization = new Authorization();
        authorization.setURL("http://localhost:80/");
        authorization.setMechanism(AuthManager.Mechanism.DIGEST);
        authorization.setPass("thomas");
        authorization.setUser("martinez");
        authorization.setDomain("localhost");

        authManager.addAuth(authorization);

        HashTree hashTree = new HashTree();
        hashTree.add(authManager);

        Servers.addServer("auth", "localhost", 80, "http", hashTree);
        Server.Builder expect = Server.builder()
                .name("auth")
                .port(Integer.toString(80))
                .host("localhost")
                .scheme(Server.Scheme.HTTP)
                .authentication(NegotiateAuthentication.builder()
                        .login(authorization.getUser())
                        .password(authorization.getPass())
                        .domain(authorization.getDomain())
                        .build());
        assertEquals(expect.build().getName(), Servers.checkServer(expect.build()));
    }

    @Test
    public void testCheckAuthentificationKerberos() {
        AuthManager authManager = new AuthManager();
        Authorization authorization = new Authorization();
        authorization.setURL("http://localhost:80/");
        authorization.setMechanism(AuthManager.Mechanism.KERBEROS);
        authorization.setPass("thomas");
        authorization.setUser("martinez");
        authorization.setRealm("localhost");

        authManager.addAuth(authorization);

        HashTree hashTree = new HashTree();
        hashTree.add(authManager);

        Servers.addServer("auth", "localhost", 80, "http", hashTree);
        Server.Builder expect = Server.builder()
                .name("auth")
                .port(Integer.toString(80))
                .host("localhost")
                .scheme(Server.Scheme.HTTP)
                .authentication(NtlmAuthentication.builder()
                        .login(authorization.getUser())
                        .password(authorization.getPass())
                        .domain(authorization.getDomain())
                        .build());
        assertEquals(expect.build().getName(), Servers.checkServer(expect.build()));
    }

    @After
    public void after(){Servers.clear();}


}
