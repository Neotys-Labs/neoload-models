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

/**
 * This class convert The HTTPDefaultRequest of JMeter into a DefaultServer in our programm
 * If we find a HttpRequest without values, we attached the Default_Server to this HttpRequest
 */
public class HttpDefaultRequestConverter implements BiFunction<ConfigTestElement, HashTree, List<Step>> {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpDefaultRequestConverter.class);

    //Methods
    @Override
    public List<Step> apply(final ConfigTestElement configTestElement, final HashTree hashTree) {
        if ((HttpDefaultsGui.class.getName().equals((configTestElement).getPropertyAsString(TestElement.GUI_CLASS)))) {
            final HTTPDefaultSetModel httpDefaultSetModel = buildHttpDefault(configTestElement);
            Servers.addDefaultServer(httpDefaultSetModel.getName(), httpDefaultSetModel.checkDomain(), httpDefaultSetModel.checkPort(), httpDefaultSetModel.checkProtocol(), hashTree);
            LOGGER.info("Conversion of HttpRequest Default");
            EventListenerUtils.readSupportedFunction("HttpRequestDefault", "Http Request Default Server");
        }
        final List<Step> stepList = null;
        return stepList;
    }

    /**
     * Like for CSVData, we have to browse the properties of this element
     * @param configTestElement
     * @return
     */
    @SuppressWarnings("Duplicates")
    static HTTPDefaultSetModel buildHttpDefault(final ConfigTestElement configTestElement) {
        final ImmutableHTTPDefaultSetModel.Builder httpDefaultSetModel = ImmutableHTTPDefaultSetModel.builder()
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
