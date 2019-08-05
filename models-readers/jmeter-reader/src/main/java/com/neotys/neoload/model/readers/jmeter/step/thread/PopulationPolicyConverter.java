package com.neotys.neoload.model.readers.jmeter.step.thread;

import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.scenario.*;
import org.apache.jmeter.threads.ThreadGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class convert the ThreadGroup of JMeter into a PopulationPolicy of Neoload
 */
class PopulationPolicyConverter {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(PopulationPolicyConverter.class);

    //Constructor
    private PopulationPolicyConverter() {
        throw new IllegalAccessError();
    }

    ///Methods
    static PopulationPolicy convert(ThreadGroup threadGroup) {
        int nbUser = threadGroup.getNumThreads();
        int rampUp = threadGroup.getRampUp();
        int loop = Integer.parseInt(threadGroup.getSamplerController().getPropertyAsString("LoopController.loops"));
        boolean planifier = threadGroup.getScheduler();
        //Infinite Loop if LoadDuration is null
        final LoadDuration loadDuration = getIterationLoadDuration(threadGroup, loop, planifier);
        final LoadPolicy loadPolicy = getLoadPolicy(threadGroup, nbUser, rampUp, loadDuration);
        EventListenerUtils.readSupportedFunction("ThreadGroup Parameters","PopulationPolicy");
        LOGGER.info("Convertion of Population Policy");
        return PopulationPolicy.builder()
                .loadPolicy(loadPolicy)
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build();
    }

    private static LoadDuration getIterationLoadDuration(ThreadGroup threadGroup, int loop, boolean planifier) {
        EventListenerUtils.readSupportedAction("LoadDuration");
        if (planifier) {
            return getTimeLoadDuration(threadGroup);
        } else if (loop != -1) {
            return getIterationLoadDuration(loop);
        }
        return null;
    }

    private static LoadDuration getIterationLoadDuration(int loop) {
        EventListenerUtils.readSupportedAction("IterationDuration");
        return LoadDuration.builder()
                .type(LoadDuration.Type.ITERATION)
                .value(loop)
                .build();
    }


    private static LoadDuration getTimeLoadDuration(ThreadGroup threadGroup) {
        EventListenerUtils.readSupportedAction("TimeDuration");
        return LoadDuration.builder()
                .type(LoadDuration.Type.TIME)
                .value((int) threadGroup.getDuration())
                .build();
    }

    private static LoadPolicy getLoadPolicy(ThreadGroup threadGroup, int nbUser, int rampUp, LoadDuration loadDuration) {
        final LoadPolicy loadPolicy;
        //Sans planification
        if (rampUp == 0) {
            loadPolicy = getConstantLoadPolicy((int) threadGroup.getDelay(), nbUser, loadDuration);
        } else {
            loadPolicy = getRampupLoadPolicy((int) threadGroup.getDelay(), nbUser, rampUp, loadDuration);
        }
        return loadPolicy;
    }

    @SuppressWarnings("ConstantConditions")
    private static LoadPolicy getRampupLoadPolicy(Integer delay, int nbUser, int rampUp, LoadDuration loadDuration) {
        EventListenerUtils.readSupportedAction("RampUpPolicy");
        return RampupLoadPolicy.builder()
                .minUsers(1)
                .maxUsers(nbUser)
                .incrementUsers(Math.max(1, rampUp / nbUser))
                .incrementEvery(LoadDuration.builder()
                        .value(1)
                        .type(LoadDuration.Type.ITERATION)
                        .build())
                .duration(loadDuration)
                .rampup(delay == 0 ? null : delay)
                .build();
    }

    @SuppressWarnings("ConstantConditions")
    private static LoadPolicy  getConstantLoadPolicy(Integer delay, int nbUser, LoadDuration loadDuration) {
        EventListenerUtils.readSupportedAction("ConstantPolicy");
        return ConstantLoadPolicy.builder()
                .users(nbUser)
                .duration(loadDuration)
                .rampup(delay == 0 ? null : delay)
                .build();
    }

}
