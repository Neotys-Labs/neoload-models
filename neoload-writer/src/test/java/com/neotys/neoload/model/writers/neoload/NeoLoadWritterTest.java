package com.neotys.neoload.model.writers.neoload;

import com.google.common.io.Files;
import com.neotys.neoload.model.ImmutableProject;
import com.neotys.neoload.model.repository.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class NeoLoadWritterTest {

    @Test
    public void writeProjectTestZip() {

        ImmutableProject project = ImmutableProject.builder()
                .name("Test project")
                .addUserPaths(getUserPath("MyPath"))
                .build();
        File tmpDir = Files.createTempDir();
        final String nlProjectFolder = tmpDir.getPath() + File.separator + project.getName().orElse("MyProject");;
        NeoLoadWriter writer = new NeoLoadWriter(project, nlProjectFolder, null);
        writer.write(true);
        assertThat(new File(tmpDir, "Test project" + File.separator + "config.zip")).exists();
        assertThat(new File(tmpDir, "Test project" + File.separator + "config.zip")).isFile();
        assertThat(new File(tmpDir, "Test project" + File.separator + "Test project.nlp")).exists();
    }
    
    @Test
    public void writeProjectTestFolder() {

        ImmutableProject project = ImmutableProject.builder()
                .name("Test project")
                .addUserPaths(getUserPath("MyPath"))
                .build();
        File tmpDir = Files.createTempDir();
        final String nlProjectFolder = tmpDir.getPath() + File.separator + project.getName().orElse("MyProject");
        NeoLoadWriter writer = new NeoLoadWriter(project, nlProjectFolder, null);
        writer.write(false);
        assertThat(new File(tmpDir, "Test project" + File.separator + "config")).exists();
        assertThat(new File(tmpDir, "Test project" + File.separator + "config")).isDirectory();
        assertThat(new File(tmpDir, "Test project" + File.separator + "Test project.nlp")).exists();
    }

    public UserPath getUserPath(String name) {
        return ImmutableUserPath.builder()
                .name(name)
                .initContainer(ImmutableContainerForMulti.builder().name("Init").tag("init-container").build())
                .actionsContainer(ImmutableContainerForMulti.builder().name("Actions").tag("actions-container").build())
                .endContainer(ImmutableContainerForMulti.builder().name("End").tag("end-container").build())
                .build();
   }
    
    @Test
    public void writeProjectTestWithVariable() throws IOException {
    	List<String> fakeColumns = new ArrayList<>();
		fakeColumns.add("colonneTest");
		File tmpDirSrc = Files.createTempDir();
    	File tmpDirDest = Files.createTempDir();
    	File file1 = new File(tmpDirSrc.getAbsolutePath(), "file1");
    	File file2 = new File(tmpDirSrc.getAbsolutePath(), "file2");
    	Files.touch(file1);
    	Files.touch(file2);
    	Map<String, List<File>> fileMap = new HashMap<>();
    	List<File> filelst = new ArrayList<>();
    	filelst.add(file1);
    	filelst.add(file2);
    	fileMap.put("variables", filelst);
    	
		FileVariable var1 = ImmutableFileVariable.builder()
    			.name("variable_test")
    			.columnsDelimiter(",")
    			.fileName(file1.getAbsolutePath())
    			.numOfFirstRowData(2)
    			.order(FileVariable.VariableOrder.SEQUENTIAL)
    			.policy(FileVariable.VariablePolicy.EACH_ITERATION)
    			.firstLineIsColumnName(true)
    			.scope(FileVariable.VariableScope.GLOBAL)
    			.columnsNames(fakeColumns)
    			.noValuesLeftBehavior(FileVariable.VariableNoValuesLeftBehavior.CYCLE)
    			.build();
    	
    	FileVariable var2 = ImmutableFileVariable.builder()
    			.name("variable_test")
    			.columnsDelimiter(",")
    			.fileName(file2.getAbsolutePath())
    			.numOfFirstRowData(2)
    			.order(FileVariable.VariableOrder.RANDOM)
    			.policy(FileVariable.VariablePolicy.EACH_USE)
    			.firstLineIsColumnName(true)
    			.scope(FileVariable.VariableScope.LOCAL)
    			.columnsNames(fakeColumns)
    			.noValuesLeftBehavior(FileVariable.VariableNoValuesLeftBehavior.STOP)
    			.build();
        
    	ImmutableProject project = ImmutableProject.builder()
                .name("Test project")
                .addUserPaths(getUserPath("MyPath"))
                .addVariables(var1)
                .addVariables(var2)
                .build();
    	final String nlProjectFolder = tmpDirDest.getPath() + File.separator + project.getName().orElse("MyProject");;
        NeoLoadWriter writer = new NeoLoadWriter(project, nlProjectFolder, fileMap);
        writer.write(true);
        assertThat(new File(tmpDirDest.getAbsolutePath() + File.separator + "Test project" + File.separator + "variables" ,"file1")).exists();
        assertThat(new File(tmpDirDest.getAbsolutePath() + File.separator + "Test project" + File.separator + "variables" ,"file2")).exists();
    }
    
}
