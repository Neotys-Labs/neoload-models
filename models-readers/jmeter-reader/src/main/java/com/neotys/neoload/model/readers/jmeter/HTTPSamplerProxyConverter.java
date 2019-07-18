package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;


public class HTTPSamplerProxyConverter implements BiFunction<HTTPSamplerProxy, HashTree, List<Step>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPSamplerProxyConverter.class);

    HTTPSamplerProxyConverter() {
    }

    private void createParameters(HTTPSamplerProxy httpSamplerProxy, Request.Builder req) {
        StringBuilder parameter = new StringBuilder();
        CollectionProperty collectionParameter = httpSamplerProxy.getArguments().getArguments();
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
        req.body(parameter.toString());
        LOGGER.info("Convert Parameters is a success");
        LOGGER.warn("If the Parameter in Neoload are strange, Please check that you have encoded the parameters in JMeter");
        EventListenerUtils.readSupportedAction("Put parameters into HttpRequest");
    }

    public List<Step> apply(HTTPSamplerProxy httpSamplerProxy, HashTree hashTree) {
        String domain = httpSamplerProxy.getDomain();
        String path = httpSamplerProxy.getPath();
        String protocol = httpSamplerProxy.getProtocol().toLowerCase();
        int port = httpSamplerProxy.getPort();

        final Request.Builder req = Request.builder()
                .name(httpSamplerProxy.getName())
                .method(httpSamplerProxy.getMethod())
                .description(httpSamplerProxy.getComment());

        if (domain.isEmpty()) {
            checkDefaultServer(httpSamplerProxy, hashTree,req);
        } else {
            req.server(Servers.addServer(httpSamplerProxy.getName(), domain, httpSamplerProxy.getPort(), httpSamplerProxy.getProtocol(), hashTree));
            String url = protocol + "://" + domain + ":" + port + path;
            //Gérer aussi avec l'intégration de variable dans le path
            req.url(url);
            EventListenerUtils.readSupportedAction("HTTPSampler");
        }
        createParameters(httpSamplerProxy, req);
        if (hashTree.get(httpSamplerProxy) != null) {
            HTTPHeaderConverter.createHeader(req, hashTree.get(httpSamplerProxy));
            req.addAllExtractors(new ExtractorConverters().convertParameter(hashTree.get(httpSamplerProxy)));
            EventListenerUtils.readSupportedAction("HTTPHeaderManager");
        } else {
            LOGGER.warn("There is not HeaderManager so HTTPRequest do not have Header");
            EventListenerUtils.readSupportedFunctionWithWarn("", "HttpRequest", null, "Don't have Header Manager");
        }
        Step javascript = CookieManagerConverter.createCookie(hashTree);

        if (javascript != null) {
            return ImmutableList.of(req.build(), javascript);
        }
        return ImmutableList.of(req.build());
    }

    private void checkDefaultServer(HTTPSamplerProxy httpSamplerProxy, HashTree hashTree, Request.Builder req) {
        boolean find = false;
        for (Object o : hashTree.list()) {
            if (o instanceof ConfigTestElement) {
                find = true;
                ConfigTestElement configTestElement = (ConfigTestElement) o;
                HTTPDefaultSetModel httpDefaultSetModel = buildHttpDefault(configTestElement);
                req.server(Servers.addServer(httpDefaultSetModel.getName(), httpDefaultSetModel.checkDomain(), httpDefaultSetModel.checkPort(), httpDefaultSetModel.checkProtocol(), hashTree));
                req.url(httpDefaultSetModel.checkProtocol() + "://" + httpDefaultSetModel.checkDomain() + ":" + httpDefaultSetModel.getPort() + httpDefaultSetModel.checkPath());
            }
        }
        if(find){
            LOGGER.warn("This HTTP Request don't have any Server, we create a new  local server");
            req.server(Servers.addServer("localhost", "localhost", 80, "http", new HashTree()));
            req.url("http://localhost:80/");
        }
    }

    private ImmutableHTTPDefaultSetModel buildHttpDefault(ConfigTestElement configTestElement) {
        ImmutableHTTPDefaultSetModel.Builder httpDefaultSetModel = ImmutableHTTPDefaultSetModel.builder()
                .name(configTestElement.getName());
        final PropertyIterator propertyIterator = configTestElement.propertyIterator();
        while (propertyIterator.hasNext()) {
            JMeterProperty jMeterProperty = propertyIterator.next();
            switch (jMeterProperty.getName()) {
                case "HTTPSampler.domain":
                    httpDefaultSetModel.domain(jMeterProperty.getStringValue());
                    break;
                case "HTTPSampler.port":
                    httpDefaultSetModel.port(jMeterProperty.getStringValue());
                    break;
                case "HTTPSampler.protocol":
                    httpDefaultSetModel.protocol(jMeterProperty.getStringValue());
                    break;
                case "HTTPSampler.path":
                    httpDefaultSetModel.path(jMeterProperty.getStringValue());
                    break;
                default:
                    break;
            }
        }
        return httpDefaultSetModel.build();
    }
}
