package com.neotys.neoload.model.readers.jmeter.step.httpRequest;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.ContainerUtils;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.variable.VariableConverters;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.protocol.http.config.gui.HttpDefaultsGui;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class HTTPSamplerProxyConverterTest {

    private TestEventListener spy;

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
        Servers.clear();
        new ContainerUtils();
        new VariableConverters();
    }


    @Test
    public void testApplyWithoutHeaderURLEncoded() {

        EventListenerUtils.setEventListener(spy);
        HTTPSamplerProxyConverter httpconverter = new HTTPSamplerProxyConverter();
        HTTPSamplerProxy request = new HTTPSamplerProxy();
        HashTree hashtree = Mockito.mock(HashTree.class);
        when(hashtree.get(request)).thenReturn(null);

        HTTPArgument httpArgument = new HTTPArgument();
        Arguments arguments = new Arguments();
        List<Argument> argumentList = new ArrayList<>();

        argumentList.add(httpArgument);
        arguments.setArguments(argumentList);

        httpArgument.setMetaData("=");
        httpArgument.setName("Allez!!");
        httpArgument.setValue("L'OL");
        httpArgument.setAlwaysEncoded(true);
        request.setDomain("host");
        request.setPath("/login");
        request.setPort(8080);
        request.setProtocol("https");
        request.setMethod("POST");
        request.addTestElement(httpArgument);
        request.setArguments(arguments);

        httpconverter.apply(request, hashtree);
        List<Step> result = httpconverter.apply(request, hashtree);
        Request neoloadRequest = Request.builder()
                .body("Allez%21%21=L%27OL")
                .url("https://host:8080/login")
                .method("POST")
                .server("")
                .description("")
                .name("")
                .build();

        assertEquals(neoloadRequest, result.get(0));
    }


    @Test
    public void testApplyWithHeader() {
        EventListenerUtils.setEventListener(spy);
        HTTPSamplerProxyConverter httpconverter = new HTTPSamplerProxyConverter();
        // HTTP Sampler
        HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();
        httpSamplerProxy.setDomain("localhost");
        httpSamplerProxy.setPort(8090);
        httpSamplerProxy.setPath("/");
        httpSamplerProxy.setMethod("POST");
        httpSamplerProxy.setName("HTTP Request");
        httpSamplerProxy.addEncodedArgument("Body Data", "{\"1\":\"2\"}", "");
        httpSamplerProxy.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        httpSamplerProxy.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
        HeaderManager headerManager = new HeaderManager();
        Header header = new Header();
        header.setName("Content-Type");
        header.setValue( "application/json");
        headerManager.add(header);
        headerManager.setName(JMeterUtils.getResString("header_manager_title")); // $NON-NLS-1$
        headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
        headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());

        HashTree hashtree = new HashTree();
        HashTree subtree = new HashTree();

        httpSamplerProxy.setDomain("host");
        httpSamplerProxy.setPath("/login");
        httpSamplerProxy.setPort(8080);
        httpSamplerProxy.setProtocol("HTTPS");
        httpSamplerProxy.setMethod("Post");

        hashtree.add(httpSamplerProxy);
        subtree.add(headerManager);
        hashtree.get(httpSamplerProxy).add(subtree);


        List<Step> result = httpconverter.apply(httpSamplerProxy, hashtree);
        for (Step step : result) {
            step.flattened()
                    .forEach(c -> {
                        if (c instanceof Request) {
                            for (com.neotys.neoload.model.v3.project.userpath.Header head : ((Request) c).getHeaders())
                            assertEquals(head.getName(), header.getName());
                            assertEquals(header.getValue(),header.getValue());
                        }

                    });
        }

    }

    @Test
    public void testBuildHttpDefault(){
        ConfigTestElement configTestElement = new ConfigTestElement();
        configTestElement.setProperty("HTTPSampler.domain","localhost");
        configTestElement.setProperty("HTTPSampler.port","80");
        configTestElement.setProperty("HTTPSampler.protocol","http");
        configTestElement.setProperty("HTTPSampler.path","/");
        configTestElement.setName("Test");

        ImmutableHTTPDefaultSetModel expected = ImmutableHTTPDefaultSetModel.builder()
                .name("Test")
                .path("/")
                .protocol("http")
                .domain("localhost")
                .port("80")
                .build();

        HTTPDefaultSetModel result = HttpDefaultRequestConverter.buildHttpDefault(configTestElement);
        assertEquals(result,expected);
    }

    @Test
    public void testCheckDefaultServerwithHttpDefaults(){
        HashTree hashTree = new HashTree();
        HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();
        httpSamplerProxy.setMethod("GET");
        ConfigTestElement configTestElement = new ConfigTestElement();
        configTestElement.setProperty("HTTPSampler.domain","localhost");
        configTestElement.setProperty("HTTPSampler.port","80");
        configTestElement.setProperty("HTTPSampler.protocol","http");
        configTestElement.setProperty("HTTPSampler.path","/");
        configTestElement.setName("Test");
        configTestElement.setProperty(TestElement.GUI_CLASS, HttpDefaultsGui.class.getName());
        hashTree.add(configTestElement);

        Request.Builder result = Request.builder();
        Request.Builder expected = Request.builder();
        new HttpDefaultRequestConverter().apply(configTestElement,hashTree);

        HTTPSamplerProxyConverter.checkDefaultServer(httpSamplerProxy,result);
        expected.server(Servers.addServer("Test","localhost",80,"http",hashTree));
        expected.url("http://localhost:80");
        assertEquals(result.build(),expected.build());
    }

    @Test
    public void testCheckDefaultServerwithoutHTTPDefaults(){
        HashTree hashTree = new HashTree();
        HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();

        Request.Builder result = Request.builder();
        Request.Builder expected = Request.builder();
        HTTPSamplerProxyConverter.checkDefaultServer(httpSamplerProxy,result);
        assertEquals(result.build(),expected.build());
        verify(spy,times(1)).readUnsupportedAction("Can't affect a server to the HTTP Request" +
                " because there isn't a HTTP Default Request attached");
    }

    @After
    public void after(){
        Servers.clear();
    }
}
