package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.v3.project.scenario.*;
import org.apache.jmeter.threads.ThreadGroup;

import javax.annotation.Nullable;

class ConvertPopulationpolicy {

    private ConvertPopulationpolicy() {
        throw new IllegalAccessError();
    }

    static PopulationPolicy popPolicyAnalyse(ThreadGroup threadGroup) {
        int nbUser = threadGroup.getNumThreads();
        int rampUp = threadGroup.getRampUp();
        int loop = Integer.parseInt(threadGroup.getSamplerController().getPropertyAsString("LoopController.loops"));
        boolean planifier = threadGroup.getScheduler();

        LoadDuration loadDuration = null; //Loop infinite si reste comme ça
        final LoadPolicy loadPolicy;
        if (planifier) {
            loadDuration = LoadDuration.builder()
                    .type(LoadDuration.Type.TIME)
                    .value((int) threadGroup.getDuration())
                    .build();
        } else if (loop != -1) {
            loadDuration = LoadDuration.builder()
                    .type(LoadDuration.Type.ITERATION)
                    .value(loop)
                    .build();
        }
        loadPolicy = getLoadPolicy(threadGroup, nbUser, rampUp, loadDuration);

        return PopulationPolicy.builder()
                .loadPolicy(loadPolicy)
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build();
    }

    private static LoadPolicy getLoadPolicy(ThreadGroup threadGroup, int nbUser, int rampUp, @Nullable LoadDuration loadDuration) {
        final LoadPolicy loadPolicy;
        //Sans planification
        if (rampUp == 0) {
            loadPolicy = getConstantLoadPolicy(threadGroup.getDelay(), nbUser, loadDuration);
        } else {
            loadPolicy = getRampupLoadPolicy(threadGroup.getDelay(), nbUser, rampUp, loadDuration);
        }
        return loadPolicy;
    }

    private static LoadPolicy getRampupLoadPolicy(long delay, int nbUser, int rampUp, @Nullable LoadDuration loadDuration) {
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

    private static LoadPolicy getConstantLoadPolicy(long delay, int nbUser, @Nullable LoadDuration loadDuration) {
        return ConstantLoadPolicy.builder()
                .users(nbUser)
                .duration(loadDuration)
                .rampup(delay == 0 ? null : delay)
                .build();
    }

}
