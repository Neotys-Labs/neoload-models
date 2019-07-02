package com.neotys.neoload.model.readers.jmeter;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jorphan.collections.HashTree;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static com.neotys.convertisseur.converters.Servers.addServer;

public class HTTPSamplerProxyConverter extends JMeterConverter implements BiFunction<HTTPSamplerProxy, HashTree, List<Step>> {

    public HTTPSamplerProxyConverter(EventListener eventListener) {
        super(eventListener);
    }

    @Override
    public List<Step> apply(HTTPSamplerProxy httpSamplerProxy, HashTree hashTree) {
        System.out.println("Converting HTTPSamplerProxy");
        getEventListener().readSupportedAction("HTTPSampler");
        Optional<String> domain = Optional.ofNullable(Strings.emptyToNull(httpSamplerProxy.getDomain()));
        Optional<String> path = Optional.ofNullable(Strings.emptyToNull(httpSamplerProxy.getPath()));
        Optional<String> protocol = Optional.ofNullable(Strings.emptyToNull(httpSamplerProxy.getProtocol()));

        int port = httpSamplerProxy.getPort();
        Request.Builder req = Request.builder()
                .method(httpSamplerProxy.getMethod())
                .description(httpSamplerProxy.getComment())
                ;

        //TODO partie Parameters
        String parametre="";
        System.out.println("Putting Parameters");
        CollectionProperty collectionParameter = httpSamplerProxy.getArguments().getArguments();
        for (JMeterProperty Valeurparametre : collectionParameter) {
            parametre = parametre + Valeurparametre.getStringValue() + "&";
        }

        req.body(parametre);
        //TODO partie des header
        System.out.println("Putting headers");
        HTTPHeaderConverter header = new HTTPHeaderConverter(httpSamplerProxy, req, hashTree);
        getEventListener().readSupportedAction("HTTPHeaderManager");
        req = header.Create_Header();

        //TODO server
        Servers.addServer(domain.orElse("host"), httpSamplerProxy.getPort(), httpSamplerProxy.getProtocol());
        String url = protocol.orElse("http") + "://" + domain.orElse("host") + ":" + port + path.orElse("/");
        req.url(url);
        req.server(domain.orElse("host"));
        path.ifPresent(req::name);

        return ImmutableList.of(req.build());
    }
}
