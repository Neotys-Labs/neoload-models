package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.v3.project.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

final class Servers {

    private static final Set<Server> SERVER_LIST = new HashSet<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPSamplerProxyConverter.class);

    private Servers() {
        throw new IllegalAccessError();
    }

    static void addServer(final String host, final int port, final String protocol) {
        Server.Scheme scheme = "https".equalsIgnoreCase(protocol) ? Server.Scheme.HTTPS : Server.Scheme.HTTP;
        LOGGER.info("Creation of a new Server");

        Server serve = Server.builder()
                .name(host)
                .port(Integer.toString(port))
                .host(host)
                .scheme(scheme)
                .build();
        SERVER_LIST.add(serve);

    }

    static Set<Server> getServers() {
        return SERVER_LIST;
    }

}
