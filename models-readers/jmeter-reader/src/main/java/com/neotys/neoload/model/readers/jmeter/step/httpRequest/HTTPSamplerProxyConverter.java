package com.neotys.neoload.model.readers.jmeter.step.httpRequest;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.extractor.ExtractorConverters;
import com.neotys.neoload.model.readers.jmeter.step.javascript.JavascriptConverter;
import com.neotys.neoload.model.v3.project.server.Server;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;


public class HTTPSamplerProxyConverter implements BiFunction<HTTPSamplerProxy, HashTree, List<Step>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPSamplerProxyConverter.class);

    public HTTPSamplerProxyConverter() {
    }

    public List<Step> apply(HTTPSamplerProxy httpSamplerProxy, HashTree hashTree) {
        String domain = httpSamplerProxy.getDomain();
        String path = Optional.ofNullable(Strings.emptyToNull(httpSamplerProxy.getPath())).orElse("/");
        String protocol = Optional.ofNullable(httpSamplerProxy.getProtocol().toLowerCase()).orElse("http");
        int port = httpSamplerProxy.getPort();
        httpSamplerProxy.setPath(path);
        httpSamplerProxy.setPort(port);
        httpSamplerProxy.setProtocol(protocol);

        final Request.Builder req = Request.builder()
                .name(httpSamplerProxy.getName())
                .method(httpSamplerProxy.getMethod())
                .description(httpSamplerProxy.getComment());

        if (domain.isEmpty()) {
            checkDefaultServer(httpSamplerProxy, req);
        } else {
            req.server(Servers.addServer(httpSamplerProxy.getName(), domain, port, protocol, hashTree));
            //Gérer aussi avec l'intégration de variable dans le path
            req.url(buildURL(httpSamplerProxy));
            EventListenerUtils.readSupportedFunction("HTTPSamplerProxy", "HTTPRequest");
        }
        if (!"GET".equals(httpSamplerProxy.getMethod())) {
            createParameters(httpSamplerProxy, req);
        }
        if (hashTree.get(httpSamplerProxy) != null) {
            HTTPHeaderConverter.createHeader(req, hashTree.get(httpSamplerProxy));
            req.addAllExtractors(new ExtractorConverters().convertParameter(hashTree.get(httpSamplerProxy)));
            EventListenerUtils.readSupportedAction("HTTPHeaderManager");
        } else {
            LOGGER.warn("There is not HeaderManager so HTTPRequest do not have Header");
            EventListenerUtils.readSupportedFunctionWithWarn("HeaderManager", "HttpRequest", "Don't have Header Manager");
        }

        Request request = req.build();
        Step javascript = JavascriptConverter.createJavascript(hashTree, httpSamplerProxy);


        if (javascript != null) {
            return ImmutableList.of(javascript, request);
        }
        return ImmutableList.of(request);
    }

    static void checkDefaultServer(HTTPSamplerProxy httpSamplerProxy, Request.Builder req) {
        Server defaultServer = Servers.getDefaultServer();
        if (defaultServer != null) {
            req.server(defaultServer.getName());
            req.method(httpSamplerProxy.getMethod());
            httpSamplerProxy.setDomain(defaultServer.getHost());
            req.url(buildURL(httpSamplerProxy));
        } else {
            LOGGER.warn("This HTTP Request don't have any Server:\n"
                    + httpSamplerProxy.getName());
            EventListenerUtils.readUnsupportedAction("Can't affect a server to the HTTP Request" +
                    "because there isn't a HTTP Default Request attached");
        }
    }

    private void createParameters(HTTPSamplerProxy httpSamplerProxy, Request.Builder req) {
        StringBuilder parameter = new StringBuilder();
        CollectionProperty collectionParameter = httpSamplerProxy.getArguments().getArguments();
        if (httpSamplerProxy.getPostBodyRaw()) {
            for (JMeterProperty ValueParameter : collectionParameter) {
                if (ValueParameter instanceof TestElementProperty) {
                    HTTPArgument httpArgument = (HTTPArgument) ValueParameter.getObjectValue();
                    parameter.append(httpArgument.getValue());
                }
            }
        } else {
            for (JMeterProperty ValueParameter : collectionParameter) {
                if (ValueParameter instanceof TestElementProperty) {
                    HTTPArgument httpArgument = (HTTPArgument) ValueParameter.getObjectValue();
                    parameter.append(httpArgument.getEncodedName());
                    parameter.append(httpArgument.getMetaData());
                    parameter.append((httpArgument.getEncodedValue()));
                    parameter.append("&");
                }
            }
            if (!parameter.toString().isEmpty()) {
                parameter.deleteCharAt(parameter.length() - 1);
            }
        }
        req.body(parameter.toString());
        LOGGER.info("Convert Parameters is a success");
        LOGGER.warn("If the Parameter in Neoload are strange, Please check that you have encoded the parameters in JMeter");
        EventListenerUtils.readSupportedFunction("Http Parameters", "Put parameters into HttpRequest");
    }

    static String buildURL(HTTPSamplerProxy httpSamplerProxy){
        StringBuilder url = new StringBuilder();
        url.append(httpSamplerProxy.getProtocol().toLowerCase());
        url.append("://");
        url.append(httpSamplerProxy.getDomain());
        url.append(":");
        url.append(httpSamplerProxy.getPort());
        url.append(httpSamplerProxy.getPath());
        Map<String, String> parametersMap = httpSamplerProxy.getArguments().getArgumentsAsMap();
        if("GET".equals(httpSamplerProxy.getMethod())){
            url.append("?");
            parametersMap.keySet().forEach(key -> {
                url.append(key);
                url.append("=");
                url.append(parametersMap.get(key));
                url.append("&");
            });
            url.deleteCharAt(url.length() - 1);
        }
        return url.toString();
    }

}
