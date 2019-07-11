package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.Iterables;
import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.v3.project.server.Server;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Set;

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
        Servers.addServer("blazedemo",8080,"HTTP");
        Set<Server> server = Servers.getServers();
        Assert.assertEquals(server.iterator().next().getPort(),"8080");
        Assert.assertEquals(server.iterator().next().getHost(),"blazedemo");
        Assert.assertEquals(server.iterator().next().getScheme().name(),"HTTP");
    }

    @Test
    public void testAddServersWithoutProtocol(){
        Servers.addServer("host",8080,null);
        Set<Server> server = Servers.getServers();
        Assert.assertEquals(server.iterator().next().getPort(),"8080");
        Assert.assertEquals(server.iterator().next().getHost(), "host");
        Assert.assertEquals(server.iterator().next().getScheme().name(),"HTTP");
    }

    @Test
    public void testAddServersWithoutHost(){
        Servers.addServer(null,8080,"HTTP");
        Set<Server> server = Servers.getServers();
        Assert.assertEquals(server.iterator().next().getPort(),"8080");
        Assert.assertNull(server.iterator().next().getHost());
        Assert.assertEquals(server.iterator().next().getScheme().name(),"HTTP");
    }

    @Test
    public void testAddServerWithoutPort() {
        Servers.addServer("blazedemo",0,"HTTP");
        Set<Server> servers = Servers.getServers();
        Assertions.assertThat(servers).hasSize(1);
        Server newServer = Iterables.getFirst(servers, null);

        Assertions.assertThat(newServer.getPort()).isEqualTo("80");
        Assertions.assertThat(newServer.getHost()).isEqualTo("blazedemo");
        Assertions.assertThat(newServer.getScheme()).isEqualTo(Server.Scheme.HTTP);
    }

    @Test
    public void testAddServerWithoutPortAndHttps() {
        Servers.addServer("blazedemo",0,"HTTPS");
        Set<Server> servers = Servers.getServers();
        Assertions.assertThat(servers).hasSize(1);
        Server newServer = Iterables.getFirst(servers, null);

        Assertions.assertThat(newServer.getPort()).isEqualTo("443");
        Assertions.assertThat(newServer.getHost()).isEqualTo("blazedemo");
        Assertions.assertThat(newServer.getScheme()).isEqualTo(Server.Scheme.HTTPS);
    }

    @After
    public void after(){Servers.clear();}


}
