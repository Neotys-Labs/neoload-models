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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;


public class HTTPSamplerProxyConverter implements BiFunction<HTTPSamplerProxy, HashTree, List<Step>> {

    private final EventListener eventListener;
    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPSamplerProxyConverter.class);


    HTTPSamplerProxyConverter(EventListener eventListener) {
        this.eventListener = eventListener;

    }


    @Override
    public List<Step> apply(HTTPSamplerProxy httpSamplerProxy, HashTree hashTree) {
        eventListener.readSupportedAction("HTTPSampler");
        Optional<String> domain = Optional.ofNullable(Strings.emptyToNull(httpSamplerProxy.getDomain()));
        Optional<String> path = Optional.ofNullable(Strings.emptyToNull(httpSamplerProxy.getPath()));
        Optional<String> protocol = Optional.ofNullable(Strings.emptyToNull(httpSamplerProxy.getProtocol()));

        int port = httpSamplerProxy.getPort();
        final Request.Builder req = Request.builder()
                .method(httpSamplerProxy.getMethod())
                .description(httpSamplerProxy.getComment())
                ;


        StringBuilder parametre = new StringBuilder();
        CollectionProperty collectionParameter = httpSamplerProxy.getArguments().getArguments();
        for (JMeterProperty Valeurparametre : collectionParameter) {
            parametre.append(Valeurparametre.getStringValue());
            parametre.append("&");
        }

        req.body(parametre.toString());


        eventListener.readSupportedAction("HTTPHeaderManager");
        LOGGER.info("HTTPHeader correctly converted");
        HTTPHeaderConverter.createHeader(httpSamplerProxy, req, hashTree);

        Servers.addServer(domain.orElse("host"), httpSamplerProxy.getPort(), httpSamplerProxy.getProtocol());
        String url = protocol.orElse("http") + "://" + domain.orElse("host") + ":" + port + path.orElse("/");
        req.url(url);
        req.server(domain.orElse("host"));
        path.ifPresent(req::name);
        eventListener.readSupportedAction("HTTPSampler");
        LOGGER.info("HTTPSampler correctly converted");



        return ImmutableList.of(req.build());
    }
}
