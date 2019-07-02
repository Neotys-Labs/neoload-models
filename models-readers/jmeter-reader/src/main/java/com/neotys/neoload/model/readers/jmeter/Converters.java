package com.neotys.neoload.model.readers.jmeter;

import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import com.neotys.neoload.model.v3.project.scenario.*;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Step;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jorphan.collections.HashTree;
import java.util.ArrayList;
import com.neotys.neoload.model.listener.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

final class Converters {
    private static final Logger LOGGER = LoggerFactory.getLogger(Converters.class);
    private final Map<Class, BiFunction<?, HashTree, List<Step>>> convertersMap;
    private final EventListener eventListener;


    @SuppressWarnings("unchecked")
    private <T> BiFunction<Object, HashTree, List<Step>> getConverters(Class<T> clazz) {
        return (BiFunction<Object, HashTree, List<Step>>) convertersMap.get(clazz);
    }

    Converters(final EventListener eventListener){

        this.eventListener = eventListener;
        convertersMap = ImmutableMap.of(
                TransactionController.class, new TransactionControllerConverter(this,eventListener),
                HTTPSamplerProxy.class, new HTTPSamplerProxyConverter(eventListener),
                ConstantTimer.class, new ConstantTimerConverter(eventListener));

    }

     ConvertThreadGroupResult convertThreadGroup(ThreadGroup threadGroup, HashTree subTree) {
        //Create User Path
        UserPath.Builder upBuilder = UserPath.builder();
            upBuilder.name(threadGroup.getName());
            upBuilder.description(threadGroup.getComment());

        //process subtree
        final List<Step> steps = convertStep(subTree);

         Container containerBuilder = getContainer(steps);

         upBuilder.actions(containerBuilder);

         UserPathPolicy userpolicy = getUserPathPolicy(threadGroup);

         Population populationBuilder = getPopulation(threadGroup, userpolicy);

         PopulationPolicy populationPolicy = PopulationPolicyConverter.convert(threadGroup);

        return new ConvertThreadGroupResult(upBuilder.build(), populationBuilder, populationPolicy);
    }

    static Container getContainer(List<Step> steps) {
        return Container.builder()
                    .addAllSteps(steps)
                    .build();
    }

    static UserPathPolicy getUserPathPolicy(ThreadGroup threadGroup) {
        return UserPathPolicy
                    .builder()
                    .name(threadGroup.getName())
                    .description(threadGroup.getComment())
                    .build();
    }

    private Population getPopulation(ThreadGroup threadGroup, UserPathPolicy userpolicy) {
        return Population
                    .builder()
                    .addUserPaths(userpolicy)
                    .name(threadGroup.getName())
                    .build();
    }

    List<Step> convertStep(HashTree subTree) {
        //walk sub tree and convert each step
        ArrayList<Step> list = new ArrayList<>();

        for (Object o : subTree.list()) {
            BiFunction<Object, HashTree, List<Step>> converter = getConverters(o.getClass());
            if(converter!=null) {
                list.addAll(converter.apply(o, subTree));
                continue;
            }
            LOGGER.error("UNKNOWN TYPE ");
            eventListener.readUnsupportedAction(o.getClass()+"\n");


        }

        return list;
    }

}
