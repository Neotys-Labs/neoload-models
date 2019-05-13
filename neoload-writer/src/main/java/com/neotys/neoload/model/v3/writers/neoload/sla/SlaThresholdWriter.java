package com.neotys.neoload.model.v3.writers.neoload.sla;

import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.writers.neoload.WriterUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class SlaThresholdWriter {

    private final static String XML_TAG_NAME = "sla-threshold";
    private final static String XML_UUID_ATTR = "uuid";
    private final static String XML_ENABLED_ATTR = "enabled";
    private final static String XML_FAMILY_ATTR = "family";
    private final static String XML_PERCENT_ATTR = "percent";
    private final static String XML_IDENTIFIER_ATTR = "identifier";

    private final static  String FAMILY_PER_RUN = "PER_RUN";
    private final static  String FAMILY_PER_TIME_INTERVAL = "PER_TIME_INTERVAL";

    ImmutableMap<SlaThreshold.KPI, String> kpiConverterMap = ImmutableMap.<SlaThreshold.KPI, String>builder()
            .put(SlaThreshold.KPI.AVG_REQUEST_RESP_TIME, "AVERAGE_REQUEST_RESPONSE_TIME")
            .put(SlaThreshold.KPI.AVG_PAGE_RESP_TIME, "AVERAGE_PAGE_RESPONSE_TIME")
            .put(SlaThreshold.KPI.AVG_TRANSACTION_RESP_TIME, "AVERAGE_CONTAINER_RESPONSE_TIME")
            .put(SlaThreshold.KPI.AVG_RESP_TIME, "AVERAGE_RESPONSE_TIME")
            .put(SlaThreshold.KPI.AVG_THROUGHPUT_PER_SEC, "AVERAGE_THROUGHPUT_PER_SECOND")
            .put(SlaThreshold.KPI.AVG_REQUEST_PER_SEC, "AVERAGE_HITS_PER_SECOND")
            .put(SlaThreshold.KPI.AVG_ELT_PER_SEC, "AVERAGE_HITS_PER_SECOND")
            .put(SlaThreshold.KPI.PERC_TRANSACTION_RESP_TIME, "PERCENTILE_CONTAINER_RESPONSE_TIME")
            .put(SlaThreshold.KPI.ERRORS_COUNT, "TOTAL_ERRORS")
            .put(SlaThreshold.KPI.ERROR_RATE, "ERROR_PERCENTILE")
            .put(SlaThreshold.KPI.ERRORS_PER_SEC, "ERRORS_PER_SECOND")
            .put(SlaThreshold.KPI.COUNT, "TOTAL_HITS")
            .put(SlaThreshold.KPI.THROUGHPUT, "TOTAL_THROUGHPUT")
            .build();

    private Optional<SlaThreshold> slaThreshold;

    public SlaThresholdWriter(Optional<SlaThreshold> slaThreshold) {
        this.slaThreshold = slaThreshold;
    }

    public static SlaThresholdWriter of(Optional<SlaThreshold> slaThreshold) { return new SlaThresholdWriter(slaThreshold);}

    public void writeXML(final Document document, final org.w3c.dom.Element currentElement, SlaThreshold.Scope scope, SlaThreshold.KPI kpi) {

        final Element xmlThreshold = document.createElement(XML_TAG_NAME);

        xmlThreshold.setAttribute(XML_ENABLED_ATTR, Boolean.toString(slaThreshold.isPresent()));
        xmlThreshold.setAttribute(XML_FAMILY_ATTR, getXMLScope(scope));
        xmlThreshold.setAttribute(XML_PERCENT_ATTR, Integer.toString(slaThreshold.isPresent() ? slaThreshold.get().getPercent().orElse(SlaThreshold.DEFAULT_PERCENT):1));
        xmlThreshold.setAttribute(XML_IDENTIFIER_ATTR, kpiConverterMap.get(kpi));
        xmlThreshold.setAttribute(XML_UUID_ATTR, UUID.randomUUID().toString());

        slaThreshold.ifPresent(slaThreshold1 -> slaThreshold1.getConditions().forEach(slaThresholdCondition -> WriterUtils.<SlaThresholdConditionWriter>getWriterFor(slaThresholdCondition).writeXML(document, xmlThreshold)));
        currentElement.appendChild(xmlThreshold);
    }

    public static String getXMLScope(SlaThreshold.Scope scope) {
        switch (scope) {
            case PER_TEST: return FAMILY_PER_RUN;
            case PER_INTERVAL: return FAMILY_PER_TIME_INTERVAL;
        }
        return FAMILY_PER_RUN;
    }
}
