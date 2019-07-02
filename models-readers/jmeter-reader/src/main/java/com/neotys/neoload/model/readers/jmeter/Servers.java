package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.v3.project.server.Server;
import java.util.HashSet;
import java.util.Set;

public final class Servers {

    private Servers() {
    }

    private static final Set<Server> SERVERS = new HashSet<>();

    public static void addServer(final String host, final int port, final String protocol) {
        //TODO
        Server.Scheme scheme = "https".equalsIgnoreCase(protocol) ? Server.Scheme.HTTPS : Server.Scheme.HTTP;
        System.out.println("creation Server");

        Server serve = Server.builder()
                .name(host)
                .port(Integer.toString(port))
                .host(host)
                .scheme(scheme)
                .build();
        SERVERS.add(serve);
        //build server and put in set
    }

    public static Set<Server> getServers() {
        return SERVERS;
    }

}
