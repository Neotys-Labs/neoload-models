package com.neotys.neoload.model.v3.writers.neoload.sla;

import com.neotys.neoload.model.v3.project.sla.SlaProfile;
import com.neotys.neoload.model.v3.project.sla.SlaThreshold;
import com.neotys.neoload.model.v3.writers.neoload.ElementWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.Optional;

public class SlaProfileWriter extends ElementWriter {

    private final static String XML_TAG_NAME = "sla-profile";


    public SlaProfileWriter(SlaProfile slaProfile) { super(slaProfile);}

    public static SlaProfileWriter of(final SlaProfile slaProfile) { return new SlaProfileWriter(slaProfile);}


    @Override
    public void writeXML(final Document document, final Element parent, final String outputFolder) {
        final Element xmlSla = document.createElement(XML_TAG_NAME);

        super.writeXML(document, xmlSla, outputFolder);

        Arrays.stream(SlaThreshold.Scope.values()).forEach(scope -> {
            scope.getKpis().forEach(kpi -> SlaThresholdWriter.of(getSlaThresholdFromProfile(kpi)).writeXML(document, xmlSla, scope, kpi));
        });

        document.appendChild(xmlSla);

    }

    private Optional<SlaThreshold> getSlaThresholdFromProfile(SlaThreshold.KPI kpi) {
        return ((SlaProfile)element).getThresholds().stream().filter(slaThreshold -> slaThreshold.getKpi()==kpi).findFirst();
    }
}
