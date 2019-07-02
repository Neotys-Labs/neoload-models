package com.neotys.neoload.model.readers.jmeter;


import com.google.common.collect.Iterables;
import com.neotys.neoload.model.listener.EventListener;
import com.neotys.neoload.model.v3.project.ImmutableProject;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.scenario.Scenario;
import com.neotys.neoload.model.v3.readers.Reader;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

public class JMeterReader extends Reader {

    private static final Logger LOG = LoggerFactory.getLogger(com.neotys.neoload.model.readers.jmeter.JMeterReader.class);
    private final EventListener eventListener;
    private final String projectName;
    private final String jmeterpath;

    public JMeterReader(final EventListener eventListener, final String pathFile, final String projectName, final String jmeterPath) {
        super(pathFile);
        this.eventListener = eventListener;
        this.projectName = projectName;
        this.jmeterpath = jmeterPath;
    }

    private ImmutableProject readScript(final Project.Builder projet, final File fichier) throws Exception {
        try {
            eventListener.startScript(fichier.getName());
            StandardJMeterEngine jmeter = new StandardJMeterEngine();
            JMeterUtils.setJMeterHome(jmeterpath);
            JMeterUtils.loadJMeterProperties(jmeterpath + File.separator + "bin" + File.separator + "jmeter.properties");

            JMeterUtils.initLogging();
            JMeterUtils.initLocale();

            SaveService.loadProperties();

            HashTree testPlanTree = null;
            testPlanTree = SaveService.loadTree(fichier);

            List<PopulationPolicy> popPolicy = new ArrayList();

            JMeterTreeModel treeModel = new JMeterTreeModel(new Object());
            JMeterTreeNode root = (JMeterTreeNode) treeModel.getRoot();
            treeModel.addSubTree(testPlanTree, root);

            String name_test;
            String comment_test;
            Object test = Iterables.getFirst(testPlanTree.list(), null);

            checkState(test instanceof TestPlan, "first element must be a test plan.");

            if (!(test instanceof TestPlan)) {
                LOG.error("There is not TestPlan at the Highest Level. It's not a functional Script!");
                throw new IllegalArgumentException("Not a functionnal Script");
            }

            TestPlan testPlan = (TestPlan) test;
            name_test = testPlan.getName();
            comment_test = testPlan.getComment();
            Collection<HashTree> testPlanSubTree = testPlanTree.values();

            for (HashTree hashTree : testPlanSubTree) {
                Collection<Object> firstLevelNodes = hashTree.list();
                for (Object o : firstLevelNodes) {
                    if (o instanceof ThreadGroup) {
                        ConvertThreadGroupResult result = Converters.convertThreadGroup((ThreadGroup) o, hashTree.get(o));
                        LOG.info("Successfully parsed ThreadGroup {}", result);
                        projet.addUserPaths(result.getUserPath());
                        projet.addPopulations(result.getPopulation());
                        popPolicy.add(result.getPopulationPolicy());

                    } else {
                        LOG.warn("Unsupported first level node with type {}", o.getClass());
                    }
                }
            }

            Scenario scenarioBuilder = Scenario.builder()
                    .addAllPopulations(popPolicy)
                    .name(name_test)
                    .description(comment_test)
                    .build();

            projet.addScenarios(scenarioBuilder);
            projet.addAllServers(Servers.getServers());
            projet.name(projectName);
            ImmutableProject project = projet.build();
            Validator validator = new Validator();
            Validation validation = validator.validate(project, NeoLoad.class);
            validation.getMessage();

            return projet.build();
        } finally {
            eventListener.endScript();
        }
    }

    @Override
    public Project read() {

        try {

            File fichier = new File(folder);
            Project.Builder projectBuilder = Project.builder();
            eventListener.startReadingScripts(1);
            if (fichier == null) {
                throw new IllegalStateException("No JMeter project found.");
            }

            ImmutableProject projet = null;
            try {
                projet = readScript(projectBuilder,fichier);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return projet;
        } finally {
            eventListener.endReadingScripts();
//        }
        }


    }
}



