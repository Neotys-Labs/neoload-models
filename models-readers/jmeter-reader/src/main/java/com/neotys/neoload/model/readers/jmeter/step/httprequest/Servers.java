package com.neotys.neoload.model.readers.jmeter.step.httprequest;

import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
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

/**
 * This class create and join the server to the HTTPRequest
 */
public final class Servers {

    //Attributs
    private static final Set<ServerWrapper> SERVER_LIST = new HashSet<>();
    private static Server SERVER_Default_LIST = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(Servers.class);

    //Constructor
    private Servers() {
        throw new IllegalAccessError();
    }

    //Methods

    @SuppressWarnings("Duplicates")
    public static String addServer(final String name, final String host, final int port, final String protocol, final HashTree hashTree) {
        final String url = protocol + "://" + host+ ":" + port + "/";
        final Server.Scheme scheme = "https".equalsIgnoreCase(protocol) ? Server.Scheme.HTTPS : Server.Scheme.HTTP;
        final Server.Builder serve = Server.builder()
                .name(name)
                .port(Integer.toString(port))
                .host(host)
                .scheme(scheme);
        for (Object o : hashTree.list()) {
            if (o instanceof AuthManager) {
                checkAuthentification(serve, url, o);
            }
        }
        LOGGER.info("Creation of a new Server is a success");
        EventListenerUtils.readSupportedFunction("Add Server to a HttpRequest","Server");
        /*
        We use the Wrapper to compare servers with own criteria
         */
        if (SERVER_LIST.add(new ServerWrapper(serve.build()))){
            return serve.build().getName();
        }
        else{
            return checkServer(serve.build());
        }
    }

    @SuppressWarnings("Duplicates")
    public static String addDefaultServer(final String name, final String host, final int port, final String protocol, final HashTree hashTree) {
        final String url = protocol + "://" + host+ ":" + port + "/";
        final Server.Scheme scheme = "https".equalsIgnoreCase(protocol) ? Server.Scheme.HTTPS : Server.Scheme.HTTP;
        final Server.Builder serve = Server.builder()
                .name(name)
                .port(Integer.toString(port))
                .host(host)
                .scheme(scheme);
        for (Object o : hashTree.list()) {
            if (o instanceof AuthManager) {
                checkAuthentification(serve, url, o);
            }
        }
        LOGGER.info("Creation of a new Default Server is a success");
        EventListenerUtils.readSupportedFunction("Add Default Server to a HttpRequest","Server");
        SERVER_Default_LIST = serve.build();
        SERVER_LIST.add(new ServerWrapper(serve.build()));
        return name;
    }

     static String checkServer(final ImmutableServer build) {
        String serverName= "";
        for(Server s : Servers.getServers()){
            if (s.getPort().equals(build.getPort()) && s.getScheme().equals(build.getScheme()) && s.getHost().equals(build.getHost())){
                serverName = s.getName();
            }
        }
        return serverName;
    }

    static void checkAuthentification(final Server.Builder serve, final String url, final Object o) {
        final AuthManager authManager = (AuthManager) o;
        try {
            checkTypeAuthentification(authManager, serve, url);
        } catch (MalformedURLException e) {
            LOGGER.error("Problem to get the url of the Authorization Manager", e);
        }
    }

    static void checkTypeAuthentification(final AuthManager authManager, final Server.Builder serve, final String url) throws MalformedURLException {

        final Authorization authorization = authManager.getAuthForURL(new URL(url));
        if (authorization != null) {
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

    public static Set<Server> getServers() {
        return SERVER_LIST.stream().map(ServerWrapper::getServer).collect(Collectors.toSet());
    }

    public static Server getDefaultServer() {
        return SERVER_Default_LIST;
    }

    public static void clear() {
        SERVER_LIST.clear();
    }

    /**
     * This class be used for compare the servers and add them into the Server_List
     * If they are not equals with our criteria
     */
    private static class ServerWrapper {
        private final Server server;

        private ServerWrapper(Server server) {
            this.server = server;
        }

        Server getServer() {
            return server;
        }

        @Override
        /**
         * With our criteria are:
         * They don't have to have the same host
         * And they don't have to have the same port
         * And they don't have to have the same Protocol
         */
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final ServerWrapper that = (ServerWrapper) o;
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
