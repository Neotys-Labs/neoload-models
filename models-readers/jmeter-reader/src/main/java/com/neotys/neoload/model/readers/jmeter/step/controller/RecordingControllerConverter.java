package com.neotys.neoload.model.readers.jmeter.step.controller;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Step;
import oracle.jrockit.jfr.Recording;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.protocol.http.control.RecordingController;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;

public class RecordingControllerConverter implements BiFunction<RecordingController, HashTree, List<Step>> {

    private final StepConverters converter;

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordingControllerConverter.class);

    public RecordingControllerConverter(StepConverters stepConverters) {
        this.converter = stepConverters;
    }

    public List<Step> apply(RecordingController recordingController, HashTree hashTree) {
        Container.Builder builder = Container.builder().description(recordingController.getComment()).name(recordingController.getName());
        builder.addAllSteps(converter.convertStep(hashTree.get(recordingController)));
        LOGGER.info("RecordingController corretly converted");
        EventListenerUtils.readSupportedFunction("RecordingController","RecordingController");
        return ImmutableList.of(builder.build());
    }
}

