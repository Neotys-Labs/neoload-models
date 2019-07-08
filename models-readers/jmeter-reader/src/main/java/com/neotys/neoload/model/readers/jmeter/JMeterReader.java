package com.neotys.neoload.model.readers.jmeter;


import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.v3.project.ImmutableProject;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.readers.Reader;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class JMeterReader extends Reader {

    private static final Logger LOG = LoggerFactory.getLogger(com.neotys.neoload.model.readers.jmeter.JMeterReader.class);
    private final EventListener eventListener;
    private final String projectName;
    private final String jmeterPath;
    private final Converters converters;

    public JMeterReader(final EventListener eventListener, final String pathFile, final String projectName, final String jmeterPath) {
        super(Objects.requireNonNull(pathFile));
        this.eventListener = Objects.requireNonNull(eventListener);
        this.projectName = Objects.requireNonNull(projectName);
        this.jmeterPath = Objects.requireNonNull(jmeterPath);
        this.converters = new Converters(eventListener);
    }


    ImmutableProject readScript(final Project.Builder projet, final File fichier) {
        Preconditions.checkNotNull(fichier, "");
        try {
            eventListener.startScript(fichier.getName());

            HashTree testPlanTree = readJMeterProject(fichier);
            List<PopulationPolicy> popPolicy = new ArrayList<>();
            String nameTest;
            String commentTest;
            Objects.requireNonNull(testPlanTree, "testPlanTree must not be null.");
            Object test = Iterables.getFirst(testPlanTree.list(), null);

            if (!(test instanceof TestPlan)) {
                LOG.error("There is not TestPlan at the Highest Level. It's not a functional Script!");
                throw new IllegalArgumentException("Not a functionnal Script");
            }

            TestPlan testPlan = (TestPlan) test;
            nameTest = testPlan.getName();
            commentTest = testPlan.getComment();
            Collection<HashTree> testPlanSubTree = testPlanTree.values();
            Objects.requireNonNull(testPlanSubTree, "There is nothing in your Script");

            for (HashTree hashTree : testPlanSubTree) {
                Collection<Object> firstLevelNodes = hashTree.list();
                for (Object o : firstLevelNodes) {
                    convertThreadGroupElement(projet, popPolicy, hashTree, o);
                }
            }
            Scenario scenarioBuilder = getScenario(popPolicy, nameTest, commentTest);
            buildProject(projet, scenarioBuilder);

            return projet.build();
        } finally {
            eventListener.endScript();
        }
    }

    HashTree readJMeterProject(final File fichier) {
        JMeterUtils.setJMeterHome(jmeterPath);
        JMeterUtils.loadJMeterProperties(jmeterPath + File.separator + "bin" + File.separator + "jmeter.properties");

        JMeterUtils.initLocale();

        try {
            SaveService.loadProperties();
        } catch (IOException e) {
            LOG.error("Problem to find SaveProperties",e);
        }

        HashTree testPlanTree = null;
        try {
            testPlanTree = SaveService.loadTree(fichier);
        } catch (IOException e) {
            LOG.error("Error to charge the HashTree",e);
        }
        return testPlanTree;
    }

    void buildProject(Project.Builder projet, Scenario scenarioBuilder) {
        projet.addScenarios(scenarioBuilder);
        projet.addAllServers(Servers.getServers());
        projet.name(projectName);

    }

     Scenario getScenario(List<PopulationPolicy> popPolicy, String nameTest, String commentTest) {
        return Scenario.builder()
                        .addAllPopulations(popPolicy)
                        .name(nameTest)
                        .description(commentTest)
                        .build();
    }

     void convertThreadGroupElement(Project.Builder projet, List<PopulationPolicy> popPolicy, HashTree hashTree, Object o) {
        if (o instanceof ThreadGroup) {
            ConvertThreadGroupResult result = converters.convertThreadGroup((ThreadGroup) o, hashTree.get(o));
            LOG.info("Successfully parsed ThreadGroup {}", result);
            projet.addUserPaths(result.getUserPath());
            projet.addPopulations(result.getPopulation());
            popPolicy.add(result.getPopulationPolicy());
        } else {
            LOG.warn("Unsupported first level node with type {}", o.getClass());
            eventListener.readUnsupportedAction(o.getClass() + "\n");
        }
    }

    @Override
    public Project read() {
        try {
            File fichier = new File(folder);
            Project.Builder projectBuilder = Project.builder();
            eventListener.startReadingScripts(1);
            return readScript(projectBuilder, fichier);
        } finally {
            eventListener.endReadingScripts();
        }
    }
}



