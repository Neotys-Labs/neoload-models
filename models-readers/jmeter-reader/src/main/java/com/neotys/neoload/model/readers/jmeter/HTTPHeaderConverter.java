package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.v3.project.userpath.Request;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jorphan.collections.HashTree;

public class HTTPHeaderConverter {
    private final HTTPSamplerProxy HttpRequest;
    private Request.Builder request;
    private final HashTree branche;

    public HTTPHeaderConverter(HTTPSamplerProxy httpSamplerProxy, Request.Builder req, HashTree hashTree) {
       this.HttpRequest = httpSamplerProxy;
       this.request = req;
       this.branche = hashTree;
    }

    public Request.Builder Create_Header(){
        HashTree samplerChildren =  branche.get(HttpRequest);
        for (Object o : samplerChildren.list()) {
            if (o instanceof HeaderManager) {
                HeaderManager head = (HeaderManager) o;
                CollectionProperty headers = head.getHeaders();
                for (JMeterProperty headerProperty : headers) {
                    if(headerProperty instanceof TestElementProperty) {
                        TestElementProperty tep = (TestElementProperty) headerProperty;
                        Object objectHeader = tep.getObjectValue();
                        if(objectHeader instanceof Header) {
                            Header header = (Header) objectHeader;
                            request.addHeaders(com.neotys.neoload.model.v3.project.userpath.Header.builder().name(header.getName()).value(header.getValue()).build());
//
                        }
                    }
                }

            }
        }

        return request;
    }
}
