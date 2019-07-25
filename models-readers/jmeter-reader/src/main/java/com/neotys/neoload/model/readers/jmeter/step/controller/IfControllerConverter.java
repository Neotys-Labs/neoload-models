package com.neotys.neoload.model.readers.jmeter.step.controller;

import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.If;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.control.IfController;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class IfControllerConverter implements BiFunction<IfController, HashTree, List<Step>> {

    private final StepConverters converter;
    private static final Logger LOGGER = LoggerFactory.getLogger(IfControllerConverter.class);

    public IfControllerConverter(StepConverters converters) {
        this.converter = converters;
    }

    public List<Step> apply(IfController ifController, HashTree hashTree) {
        List<Step> containerList = new ArrayList<>();
        convertIfController(containerList, ifController, hashTree);
        LOGGER.info("IfController corretly converted");
        EventListenerUtils.readSupportedAction("IfController");
        return containerList;
    }

    private void convertIfController(List<Step> containerList, IfController ifController, HashTree hashTree) {
        int i= 0;
        If.Builder ifBuilder = If.builder()
                .name(ifController.getName())
                .description(ifController.getCondition());
        HashTree subTree = hashTree.get(ifController);
        if(!ifController.isEvaluateAll()){
            for (Object o : subTree.list()){
                HashTree children = new HashTree();
                children.add(o);
                children.get(o).add(subTree.getTree(o));
                ifBuilder.name(ifController.getName() +" children " + o.getClass().getSimpleName()+ (i++))
                        .then(Container.builder()
                                .addAllSteps(converter.convertStep(children))
                                .build());
                containerList.add(ifBuilder.build());
            }
        }else{
            ifBuilder
                    .then(Container.builder()
                    .addAllSteps(converter.convertStep(subTree))
                    .build());
            containerList.add(ifBuilder.build());
        }
        if(ifController.isUseExpression()){
            LOGGER.warn("We don't convert the Interprete of Variable Expression");
            EventListenerUtils.readSupportedFunctionWithWarn("IfController","Options", "Variable Expression");
        }

    }
}
