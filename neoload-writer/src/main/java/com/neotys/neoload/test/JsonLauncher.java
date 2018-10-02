package com.neotys.neoload.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.neotys.neoload.model.ImmutableProject;
import com.neotys.neoload.model.Project;
import com.neotys.neoload.model.repository.*;
import com.neotys.neoload.model.scenario.*;
import com.neotys.neoload.model.writers.neoload.NeoLoadWriter;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonLauncher {

    static Logger logger = LoggerFactory.getLogger(JsonLauncher.class);

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GuavaModule());
        objectMapper.registerModule(new Jdk8Module());

        Server server1 = ImmutableServer.builder().name("jack").host("jack").port("80").build();
//       Server server2 = ImmutableServer.builder().name("magnum").host("magnum").port("80").build();

        UserPath user1 = ImmutableUserPath.builder()
                .name("MyUser1")
                .initContainer(ImmutableContainer.builder().name("init").build())
                .actionsContainer(ImmutableContainer.builder().name("Action")
                        .addChilds(ImmutablePage.builder().name("page").description("description").thinkTime(1000).isDynamic(true).build())
                		.addChilds(ImmutableGetPlainRequest.builder().name("/index1.html").path("/index1.html").server(server1).httpMethod(Request.HttpMethod.GET).build())
                        .addChilds(ImmutableDelay.builder().name("delay").delay("1000").isThinkTime(true).build())
                        .build())
                .endContainer(ImmutableContainer.builder().name("end").build())
                .build();

//        UserPath user2 = ImmutableUserPath.builder()
//                .name("MyUser1")
//                .initContainer(ImmutableContainer.builder().name("init").build())
//                .actionsContainer(ImmutableContainer.builder().name("Action")
//                        .addChilds(ImmutableGetPlainRequest.builder().name("/index2.html").path("/index2.html").server(server2).httpMethod(Request.HttpMethod.GET).build())
//                        .addChilds(ImmutableDelay.builder().name("delay").delay("1000").isThinkTime(true).build())
//                        .addChilds(ImmutableContainer.builder().name("container")
//                        		.addChilds(ImmutableGetPlainRequest.builder().name("/helloworld2.html").path("/helloworld2.html").server(server2).httpMethod(Request.HttpMethod.GET).build())
//                        		.build())
//                        .build())
//                .endContainer(ImmutableContainer.builder().name("end").build())
//                .build();

        Population population1 = ImmutablePopulation.builder().name("myPopulation1").addSplits(ImmutablePopulationSplit.builder().userPath("MyUser1").percentage(100).build()).build();
//        Population population2 = ImmutablePopulation.builder().name("myPopulation2").addSplits(ImmutablePopulationSplit.builder().userPath("MyUser2").percentage(100).build()).build();


        Scenario scenario = ImmutableScenario.builder()
        		.name("myScenario")
                .addPopulations(ImmutablePopulationPolicy.builder()
                		.name("myPopulation1")
                		.loadPolicy(ImmutableConstantLoadPolicy.builder()
                				.users(25)
                				.duration(ImmutableDuration.builder()
                						.value(120)
                						.type(Duration.Type.TIME)
                						.build())
                				.build())
                		.build())
                .build();

//        Scenario scenario2 = ImmutableScenario.builder().name("myScenario2")
//                .putPopulations("myPopulation1",ImmutableScenarioPolicies.builder()
//                        .durationPolicy(ImmutableIterationDurationPolicy.builder().build())
//                        .loadPolicy(ImmutableRampupLoadPolicy.builder().initialLoad(6).incrementLoad(3).incrementIteration(4).iterationNumber(9).build())
//                        .build())
//                .build();
//
//        Scenario scenario3 = ImmutableScenario.builder().name("myScenario3")
//                .putPopulations("myPopulation1",ImmutableScenarioPolicies.builder()
//                        .durationPolicy(ImmutableIterationDurationPolicy.builder().build())
//                        .loadPolicy(ImmutableConstantLoadPolicy.builder().load(100).iterationNumber(9).build())
//                        .build())
//                .putPopulations("myPopulation2",ImmutableScenarioPolicies.builder()
//                        .durationPolicy(ImmutableIterationDurationPolicy.builder().build())
//                        .loadPolicy(ImmutableConstantLoadPolicy.builder().load(200).iterationNumber(9).build())
//                        .build())
//                .build();
//
//        Scenario scenario4 = ImmutableScenario.builder().name("myScenario4")
//                .putPopulations("myPopulation1",ImmutableScenarioPolicies.builder()
//                        .durationPolicy(ImmutableTimeDurationPolicy.builder().duration(122).build())
//                        .loadPolicy(ImmutablePeaksLoadPolicy.builder().minimumLoad(9).minimumTime(366).maximumLoad(1505).maximumTime(188).startPolicy(PeaksLoadPolicy.StartPolicy.MAXIMUM_LOAD).build())
//                        .build())
//                .build();

        Project project = ImmutableProject.builder()
                .name("MyProjectAsCode")
                .addServers(server1)
                .addUserPaths(user1)
                .addPopulations(population1)
                .addScenarios(scenario)
                .build();

//        Project project = ImmutableProject.builder()
//                .name("MyProjectAsCode")
//                .addServers(server1, server2)
//                .addUserPaths(user1, user2)
//                .addPopulations(population1, population2)
//                .addScenarios(scenario, scenario2, scenario3, scenario4)
//                .build();

        NeoLoadWriter writer = new NeoLoadWriter(project, "C:/tmp/Neotys/workspace-converters/output",null);
        writer.write(true);



        try {
            String json = objectMapper.writeValueAsString(project);
            System.out.println(json);
            logger.info(json);
        } catch (Exception e) {
            logger.error("error creating json content",e);
        }

        try {
            InputStream stream = JsonLauncher.class.getResourceAsStream("project.json");
            Project readProject = objectMapper.readValue(stream, Project.class);
            stream.close();
            System.out.println(readProject.toString());
            logger.info(readProject.toString());
            
        }catch(Exception e) {
            logger.error("error reading the json file",e);
        }
    }


}
