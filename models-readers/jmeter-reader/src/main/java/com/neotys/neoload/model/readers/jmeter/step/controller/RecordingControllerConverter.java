package com.neotys.neoload.model.readers.jmeter.step.controller;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.protocol.http.control.RecordingController;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

/**
 * This class convert the RecordingController of JMeter into Transaction of Neoload
 */
public class RecordingControllerConverter implements BiFunction<RecordingController, HashTree, List<Step>> {

    //Attributs
    private final StepConverters converter;

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordingControllerConverter.class);

    //Constructor
    public RecordingControllerConverter(StepConverters stepConverters) {
        this.converter = stepConverters;
    }

    //Methods
    /**
     * We convert the RecordingController like it was a SimpleController
     *
     * @param recordingController
     * @param hashTree
     * @return
     */
    public List<Step> apply(RecordingController recordingController, HashTree hashTree) {
        Container.Builder builder = Container.builder().description(recordingController.getComment()).name(recordingController.getName());
        builder.addAllSteps(converter.convertStep(hashTree.get(recordingController)));
        LOGGER.info("RecordingController correctly converted");
        EventListenerUtils.readSupportedFunction("RecordingController","RecordingController");
        return ImmutableList.of(builder.build());
    }
}

