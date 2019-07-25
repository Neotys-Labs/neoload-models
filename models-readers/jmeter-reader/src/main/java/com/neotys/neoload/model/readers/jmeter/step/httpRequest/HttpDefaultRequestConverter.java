package com.neotys.neoload.model.readers.jmeter.step.httpRequest;

import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.protocol.http.config.gui.HttpDefaultsGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

public class HttpDefaultRequestConverter implements BiFunction<ConfigTestElement, HashTree, List<Step>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpDefaultRequestConverter.class);

    public HttpDefaultRequestConverter() {
    }

    @Override
    public List<Step> apply(ConfigTestElement configTestElement, HashTree hashTree) {
        if ((HttpDefaultsGui.class.getName().equals((configTestElement).getPropertyAsString(TestElement.GUI_CLASS)))) {
            HTTPDefaultSetModel httpDefaultSetModel = buildHttpDefault(configTestElement);
            Servers.addDefaultServer(httpDefaultSetModel.getName(), httpDefaultSetModel.checkDomain(), httpDefaultSetModel.checkPort(), httpDefaultSetModel.checkProtocol(), hashTree);
            LOGGER.info("Conversion of HttpRequest Default");
            EventListenerUtils.readSupportedFunction("HttpRequestDefault", "Http Request Default Server");
        }
        List<Step> stepList;
        stepList = null;
        return stepList;
    }

    @SuppressWarnings("Duplicates")
    static HTTPDefaultSetModel buildHttpDefault(ConfigTestElement configTestElement) {
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
