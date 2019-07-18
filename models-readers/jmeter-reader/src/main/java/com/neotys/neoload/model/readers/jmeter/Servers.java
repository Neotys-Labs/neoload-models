package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.v3.project.server.*;
import org.apache.jmeter.protocol.http.control.AuthManager;
import org.apache.jmeter.protocol.http.control.Authorization;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

final class Servers {

    private static final Set<ServerWrapper> SERVER_LIST = new HashSet<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPSamplerProxyConverter.class);

    private Servers() {
        throw new IllegalAccessError();
    }

    static void addServer(final String name, final String host, final int port, final String protocol, final HashTree hashTree, final String url) {
        Server.Scheme scheme = "https".equalsIgnoreCase(protocol) ? Server.Scheme.HTTPS : Server.Scheme.HTTP;
        final int serverPort;
        if (port <= 0) {
            serverPort = scheme == Server.Scheme.HTTP ? 80 : 443;
        } else {
            serverPort = port;
        }

        Server.Builder serve = Server.builder()
                .name(name)
                .port(Integer.toString(serverPort))
                .host(host)
                .scheme(scheme);
        for (Object o : hashTree.list()) {
            if (o instanceof AuthManager) {
                checkAuthentification(serve, hashTree, url, o);
            }
        }

        SERVER_LIST.add(new ServerWrapper(serve.build()));
        LOGGER.info("Creation of a new Server is a success");
        EventListenerUtils.readSupportedAction("Server");
    }

    static void checkAuthentification(Server.Builder serve, HashTree hashTree, String url, Object o) {
        AuthManager authManager = (AuthManager) o;
        try {
            checkTypeAuthentification(authManager, serve, url);
        } catch (MalformedURLException e) {
            LOGGER.error("Problem to get the url of the AUthorization Manager", e);
        }
    }

    static void checkTypeAuthentification(AuthManager authManager, Server.Builder serve, String url) throws MalformedURLException {

        Authorization authorization = authManager.getAuthForURL(new URL(url));
        if (authorization != null) {
            if (authorization.expectsModification()) {
                LOGGER.warn("We can't manage the clear auth for each iteration");
                EventListenerUtils.readUnsupportedParameter("Authorization Manager", "Options", "Clear for each iteration");
            }
            if (authorization.getMechanism().equals(AuthManager.Mechanism.BASIC) || authorization.getMechanism().equals(AuthManager.Mechanism.BASIC_DIGEST)) {
                serve.authentication(BasicAuthentication.builder()
                        .login(authorization.getUser())
                        .password(authorization.getPass())
                        .realm(authorization.getRealm().isEmpty() ? authorization.getDomain() : authorization.getRealm())
                        .build());
            } else if (authorization.getMechanism().equals(AuthManager.Mechanism.DIGEST)) {
                serve.authentication(NegotiateAuthentication.builder()
                        .login(authorization.getUser())
                        .password(authorization.getPass())
                        .domain(authorization.getDomain().isEmpty() ? authorization.getRealm() : authorization.getDomain())
                        .build());
            } else if (authorization.getMechanism().equals(AuthManager.Mechanism.KERBEROS)) {
                serve.authentication(NtlmAuthentication.builder()
                        .login(authorization.getUser())
                        .password(authorization.getPass())
                        .domain(authorization.getDomain().isEmpty() ? authorization.getRealm() : authorization.getDomain())
                        .build());
            }
        }
    }

    static Set<Server> getServers() {
        return SERVER_LIST.stream().map(ServerWrapper::getServer).collect(Collectors.toSet());
    }

    static void clear() {
        SERVER_LIST.clear();
    }

    private static class ServerWrapper {
        private final Server server;

        private ServerWrapper(Server server) {
            this.server = server;
        }

        Server getServer() {
            return server;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ServerWrapper that = (ServerWrapper) o;
            return server.getHost().equals(that.server.getHost()) &&
                    server.getPort().equals(that.server.getPort()) &&
                    server.getScheme().equals(that.server.getScheme());
        }

        @Override
        public int hashCode() {
            return Objects.hash(server.getHost(), server.getPort(), server.getScheme());
        }
    }

}
