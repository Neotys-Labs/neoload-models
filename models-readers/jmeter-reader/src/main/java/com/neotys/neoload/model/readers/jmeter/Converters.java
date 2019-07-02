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
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public final class Converters {

   private static Map<Class, BiFunction<?, HashTree, List<Step>>> CONVERTERS = ImmutableMap.of(
            TransactionController.class, new TransactionControllerConverter(),
            HTTPSamplerProxy.class, new HTTPSamplerProxyConverter(null),
            ConstantTimer.class, new ConstantTimerConverter()

    );

    @SuppressWarnings("unchecked")
    private static <T> BiFunction<Object, HashTree, List<Step>> getConverters(Class<T> clazz) {
        return (BiFunction<Object, HashTree, List<Step>>) CONVERTERS.get(clazz);
    }

    public static ConvertThreadGroupResult convertThreadGroup(ThreadGroup threadGroup, HashTree subTree) {
        //Create User Path
        UserPath.Builder upBuilder = UserPath.builder();
            upBuilder.name(threadGroup.getName());
            upBuilder.description(threadGroup.getComment());

        //process subtree
        final List<Step> steps = convertStep(subTree);

        Container containerBuilder = Container.builder()
                .addAllSteps(steps)
                .build();

        upBuilder.actions(containerBuilder);

        UserPathPolicy userpolicy = UserPathPolicy
                .builder()
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build()
                ;
        //Create population
        Population populationBuilder = Population
                .builder()
                .addUserPaths(userpolicy)
                .name(threadGroup.getName())
                .build();


        PopulationPolicy populationPolicy = new ConvertPopulationpolicy().popPolicyAnalyse(threadGroup);

        return new ConvertThreadGroupResult(upBuilder.build(), populationBuilder, populationPolicy);
    }

    public static List<Step> convertStep(HashTree subTree) {
        //walk sub tree and convert each step
        ArrayList<Step> list = new ArrayList<>();

        for (Object o : subTree.list()) {
            BiFunction<Object, HashTree, List<Step>> converter = getConverters(o.getClass());
            if(converter!=null) {
                list.addAll(converter.apply(o, subTree));
                continue;
            }
            System.err.println("UNKNOWN TYPE " + o.getClass()+"\n");
        }

        return list;
    }

}
