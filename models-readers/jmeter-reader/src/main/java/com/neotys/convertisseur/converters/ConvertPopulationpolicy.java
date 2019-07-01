package com.neotys.convertisseur.converters;

import com.neotys.neoload.model.v3.project.scenario.*;
import org.apache.jmeter.threads.ThreadGroup;

public class ConvertPopulationpolicy {

    public static PopulationPolicy popPolicyAnalyse(ThreadGroup threadGroup) {
        int NbUser = threadGroup.getNumThreads();
        int rampUp = threadGroup.getRampUp();
        int loop   = Integer.parseInt(threadGroup.getSamplerController().getPropertyAsString("LoopController.loops"));
        boolean planifier = threadGroup.getScheduler();

        LoadDuration loadDuration = null; //Loop infinite si reste comme ça
        LoadPolicy loadPolicy = null;

        //TODO DurationPolicy
        if (planifier ){

            loadDuration = LoadDuration.builder()
                    .type(LoadDuration.Type.TIME)
                    .value((int)threadGroup.getDuration())
                    .build();
        }

        else if (loop != -1 && !planifier){
            loadDuration = LoadDuration.builder()
                    .type(LoadDuration.Type.ITERATION)
                    .value(loop)
                    .build();
        }

        //TODO LoadPolicy
        //Sans planification
        if (rampUp == 0 && !planifier){
            loadPolicy = ConstantLoadPolicy.builder()
                    .users(NbUser)
                    .duration(loadDuration)
                    .build();
        }
        else if (rampUp>0 && !planifier){
            loadPolicy = RampupLoadPolicy.builder()
                    .minUsers(1)
                    .maxUsers(NbUser)
                    .incrementUsers(Math.max(1,rampUp / NbUser))
                    .incrementEvery(LoadDuration.builder()
                            .value(1)
                            .type(LoadDuration.Type.ITERATION)
                            .build())
                    .duration(loadDuration)
                    .build();
        }
        //Avec Planification
        else if (rampUp>0 && planifier){
            loadPolicy = RampupLoadPolicy.builder()
                    .minUsers(1)
                    .maxUsers(NbUser)
                    .incrementUsers(Math.max(1,rampUp / NbUser))
                    .incrementEvery(LoadDuration.builder()
                            .value(1)
                            .type(LoadDuration.Type.ITERATION)
                            .build())
                    .duration(loadDuration)
                    .rampup((int)threadGroup.getDelay())
                    .build();
        }
        else if (rampUp==0 && planifier){
            loadPolicy = ConstantLoadPolicy.builder()
                    .users(NbUser)
                    .duration(loadDuration)
                    .rampup((int)threadGroup.getDelay())
                    .build();
        }

        PopulationPolicy populationPolicy = PopulationPolicy.builder()
                .loadPolicy(loadPolicy)
                .name(threadGroup.getName())
                .description(threadGroup.getComment())
                .build();

        return populationPolicy;
    }
}
