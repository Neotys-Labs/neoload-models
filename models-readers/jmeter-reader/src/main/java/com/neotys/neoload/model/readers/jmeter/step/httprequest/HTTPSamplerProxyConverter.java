package com.neotys.neoload.model.readers.jmeter.step.httprequest;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.extractor.ExtractorConverters;
import com.neotys.neoload.model.readers.jmeter.step.javascript.JavaScriptConverter;
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

/**
 * This class convert the HTTPSamplerProxy of JMeter into a HTTPRequest Step of Neoload
 */
public class HTTPSamplerProxyConverter implements BiFunction<HTTPSamplerProxy, HashTree, List<Step>> {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPSamplerProxyConverter.class);

    //Methods
    /**
     * Here we create the httpRequest of Neoload
     * First we get the values back,
     * Then we check if the domain is null, if it's null, we check if a default server exist to attached it
     * Then we put the parameters into the body and check the header
     * @param httpSamplerProxy
     * @param hashTree
     * @return
     */
    public List<Step> apply(final HTTPSamplerProxy httpSamplerProxy, final HashTree hashTree) {
        final String domain = httpSamplerProxy.getDomain();
        final String path = Optional.ofNullable(Strings.emptyToNull(httpSamplerProxy.getPath())).orElse("/");
        final String protocol = Optional.ofNullable(httpSamplerProxy.getProtocol().toLowerCase()).orElse("http");
        final int port = httpSamplerProxy.getPort();
        httpSamplerProxy.setPath(path);
        httpSamplerProxy.setPort(port);
        httpSamplerProxy.setProtocol(protocol);

        final Request.Builder req = Request.builder()
                .name(httpSamplerProxy.getName())
                .method(httpSamplerProxy.getMethod())
                .description(httpSamplerProxy.getComment())
                .followRedirects(httpSamplerProxy.getFollowRedirects() || httpSamplerProxy.getAutoRedirects());

        if (domain.isEmpty()) {
            checkDefaultServer(httpSamplerProxy, req);
        } else {
            req.server(Servers.addServer(httpSamplerProxy.getName(), domain, port, protocol, hashTree));
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

        final Request request = req.build();
        final Step javascript = JavaScriptConverter.createJavascript(hashTree, httpSamplerProxy);


        if (javascript != null) {
            return ImmutableList.of(javascript, request);
        }
        return ImmutableList.of(request);
    }

    static void checkDefaultServer(final HTTPSamplerProxy httpSamplerProxy, final Request.Builder req) {
        final Server defaultServer = Servers.getDefaultServer();
        if (defaultServer != null) {
            req.server(defaultServer.getName());
            req.method(httpSamplerProxy.getMethod());
            httpSamplerProxy.setDomain(defaultServer.getHost());
            req.url(buildURL(httpSamplerProxy));
        } else {
            LOGGER.warn("This HTTP Request don't have any Server:\n"
                    + httpSamplerProxy.getName());
            EventListenerUtils.readUnsupportedAction("Can't affect a server to the HTTP Request " +
                    "because there isn't a HTTP Default Request attached");
        }
    }

    private void createParameters(final HTTPSamplerProxy httpSamplerProxy, final Request.Builder req) {
        final StringBuilder parameter = new StringBuilder();
        final CollectionProperty collectionParameter = httpSamplerProxy.getArguments().getArguments();
        if (httpSamplerProxy.getPostBodyRaw()) {
            for (JMeterProperty ValueParameter : collectionParameter) {
                if (ValueParameter instanceof TestElementProperty) {
                    final HTTPArgument httpArgument = (HTTPArgument) ValueParameter.getObjectValue();
                    parameter.append(httpArgument.getValue());
                }
            }
        } else {
            for (JMeterProperty ValueParameter : collectionParameter) {
                if (ValueParameter instanceof TestElementProperty) {
                    final HTTPArgument httpArgument = (HTTPArgument) ValueParameter.getObjectValue();
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

    static String buildURL(final HTTPSamplerProxy httpSamplerProxy){
        final StringBuilder url = new StringBuilder();
        url.append(httpSamplerProxy.getProtocol().toLowerCase());
        url.append("://");
        url.append(httpSamplerProxy.getDomain());
        url.append(":");
        url.append(httpSamplerProxy.getPort());
        url.append(httpSamplerProxy.getPath());
        final Map<String, String> parametersMap = httpSamplerProxy.getArguments().getArgumentsAsMap();
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
