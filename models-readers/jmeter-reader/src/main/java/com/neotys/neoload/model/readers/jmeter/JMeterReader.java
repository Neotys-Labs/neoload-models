package com.neotys.neoload.model.readers.jmeter;


import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.readers.jmeter.step.httprequest.Servers;
import com.neotys.neoload.model.readers.jmeter.step.thread.ConvertThreadGroupResult;
import com.neotys.neoload.model.readers.jmeter.step.thread.ThreadGroupConverter;
import com.neotys.neoload.model.readers.jmeter.variable.VariableConverters;
import com.neotys.neoload.model.v3.project.DependencyType;
import com.neotys.neoload.model.v3.project.ImmutableDependency;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.project.variable.ConstantVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
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
import java.util.*;

public class JMeterReader extends Reader {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(JMeterReader.class);
    private final String projectName;
    private final String jmeterPath;
    private final StepConverters stepConverters;


    //Constructor
    public JMeterReader(final EventListener eventListener, final String pathFile, final String projectName, final String jmeterPath) {
        super(Objects.requireNonNull(pathFile));
        EventListenerUtils.setEventListener(Objects.requireNonNull(eventListener));
        this.projectName = Objects.requireNonNull(projectName);
        this.jmeterPath = Objects.requireNonNull(jmeterPath);
        this.stepConverters = new StepConverters();
    }

    //Methods

    /**
     * In this method, we load the JMX's HashTree into testPlan variable
     *
     * @param project
     * @param file
     * @return
     */
    Project readScript(final Project.Builder project, final File file) {
        Preconditions.checkNotNull(file, "");
        try {
            EventListenerUtils.startScript(file.getName());

            HashTree testPlanTree = null;
            try {
                testPlanTree = readJMeterProject(file);
            } catch (IOException | UpdateClassLoaderExceptionWrapper e) {
                LOGGER.error("Problem to Load HashTree", e);
            }
            final List<PopulationPolicy> popPolicy = new ArrayList<>();
            Objects.requireNonNull(testPlanTree, "testPlanTree must not be null.");
            Object test = Iterables.getFirst(testPlanTree.list(), null);

            if (!(test instanceof TestPlan)) {
                LOGGER.error("There is not TestPlan at the Highest Level. It's not a functional Script!");
                throw new IllegalArgumentException("Not a functionnal Script");
            }
            TestPlan testPlan = (TestPlan) test;
            final String nameTest = testPlan.getName();
            final String commentTest = testPlan.getComment();

            getVariable(project, testPlan);

            final Collection<HashTree> testPlanSubTree = testPlanTree.values();
            Objects.requireNonNull(testPlanSubTree, "There is nothing in your Script");

            for (HashTree hashTree : testPlanSubTree) {
                final Collection<Object> firstLevelNodes = hashTree.list();
                for (Object o : firstLevelNodes) {
                    convertThreadGroupElement(project, popPolicy, hashTree, o);
                }
            }
            final Scenario scenarioBuilder = getScenario(popPolicy, nameTest, commentTest);
            buildProject(project, scenarioBuilder);
            return project.build();
        } finally {
            EventListenerUtils.endScript();
        }
    }

    void getVariable(final Project.Builder projet, final TestPlan testPlan) {
        final Map<String, String> variableList = testPlan.getUserDefinedVariables();
        for (Map.Entry<String, String> entry : variableList.entrySet()) {
            String value = entry.getValue();
            if ((entry.getValue().contains("${"))) {
                String[] stringList = entry.getValue().split(",,");
                value = stringList[1];
                value = value.replace(")", "");
                value = value.replace("}", "");
            }
            final Variable variable = ConstantVariable.builder()
                    .name(entry.getKey())
                    .value(value)
                    .build();
            projet.addVariables(variable);
        }
    }

    /**
     * Here, we load the JMeter properties to know the version of Jmeter to use and how does the element works
     *
     * @param fichier
     * @return
     * @throws IOException
     */
    HashTree readJMeterProject(final File fichier) throws IOException, UpdateClassLoaderExceptionWrapper {
        JMeterUtils.setJMeterHome(jmeterPath);
        JMeterUtils.loadJMeterProperties(jmeterPath + File.separator + "bin" + File.separator + "jmeter.properties");
        JMeterUtils.initLocale();

        ClasspathUtils.updateClassLoader();

        SaveService.loadProperties();
        HashTree testPlanTree;
        testPlanTree = SaveService.loadTree(fichier);
        return testPlanTree;
    }

    void buildProject(final Project.Builder project, final Scenario scenarioBuilder) {
        project.addScenarios(scenarioBuilder);
        project.addAllServers(Servers.getServers());
        project.name(projectName);

        // the project jmeter needs the functions lib
        project.addDependencies(ImmutableDependency.builder()
                .name("JMeter Tool Library")
                .description("Contains some useful JMeter JS functions.")
                .type(DependencyType.JS_LIBRARY)
                .inputStream(this.getClass().getResourceAsStream("js/jmeter-1.0.js"))
                .filename("jmeter-1.0.js")
                .build());
    }

    Scenario getScenario(final List<PopulationPolicy> popPolicy, final String nameTest, final String commentTest) {
        return Scenario.builder()
                .addAllPopulations(popPolicy)
                .name(nameTest)
                .description(commentTest)
                .build();
    }

    void convertThreadGroupElement(final Project.Builder projet, final List<PopulationPolicy> popPolicy, final HashTree hashTree, final Object o) {
        if (o instanceof ThreadGroup) {
            final ConvertThreadGroupResult result = new ThreadGroupConverter(stepConverters, (ThreadGroup) o, hashTree.get(o)).convert();
            LOGGER.info("Successfully parsed ThreadGroup {}", result);
            projet.addUserPaths(result.getUserPath());
            projet.addPopulations(result.getPopulation());
            popPolicy.add(result.getPopulationPolicy());
            projet.addAllVariables(result.getVariableList());
        } else if (stepConverters.getConvertersMap().containsKey(o.getClass())) {
            final HashTree subtree = new HashTree();
            subtree.add(o);
            subtree.getTree(o).add(hashTree.getTree(o));
            stepConverters.convertStep(subtree);
        } else if (VariableConverters.getConvertersMap().containsKey(o.getClass())) {
            final HashTree subtree = new HashTree();
            subtree.add(o);
            subtree.getTree(o).add(hashTree.getTree(o));
            VariableConverters.convertVariable(null, o);
        } else {
            LOGGER.warn("Unsupported first level node with type {}", o.getClass());
            EventListenerUtils.readUnsupportedAction(o.getClass() + "\n");
        }

    }

    @Override
    public Project read() {
        try {
            final File file = new File(folder);
            final Project.Builder projectBuilder = Project.builder();
            EventListenerUtils.startReadingScripts(1);
            return readScript(projectBuilder, file);
        } finally {
            EventListenerUtils.endReadingScripts();
        }
    }
}



