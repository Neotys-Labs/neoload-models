package com.neotys.neoload.model.v3.writers.neoload;

import com.google.common.io.Files;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.variable.FileVariable;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

public class NeoLoadWriterTest {

    private UserPath userPath = UserPath.builder()
            .name("my User")
            .actions(Container.builder()
                    .addSteps(Delay.builder().value("3000").build())
                    .build())
            .build();


    private Project project = Project.builder()
            .name("my Project")
            .addUserPaths(userPath)
            .build();

    @Test
    public void writeTestZip() {

        File tmpDir = Files.createTempDir();

        NeoLoadWriter writer = new NeoLoadWriter(project, tmpDir.getAbsolutePath());
        writer.write(true);

        System.out.println(tmpDir.getAbsolutePath());
        Assertions.assertThat(Paths.get(tmpDir.getAbsolutePath(), "config.zip").toFile()).exists();
        Assertions.assertThat(Paths.get(tmpDir.getAbsolutePath(), "my Project.nlp").toFile()).exists();
    }

    @Test
    public void writeTestFolder() {

        File tmpDir = Files.createTempDir();

        NeoLoadWriter writer = new NeoLoadWriter(project, tmpDir.getAbsolutePath());
        writer.write(false);

        System.out.println(tmpDir.getAbsolutePath());
        Assertions.assertThat(Paths.get(tmpDir.getAbsolutePath(), "config", "repository.xml").toFile()).exists();
        Assertions.assertThat(Paths.get(tmpDir.getAbsolutePath(), "config", "scenario.xml").toFile()).exists();
        Assertions.assertThat(Paths.get(tmpDir.getAbsolutePath(), "config", "settings.xml").toFile()).exists();
        Assertions.assertThat(Paths.get(tmpDir.getAbsolutePath(), "my Project.nlp").toFile()).exists();
    }

    @Test
    public void writeTestWithoutName() {

        File tmpDir = Files.createTempDir();

        Project project = Project.builder()
                .name(Optional.empty())
                .addUserPaths(userPath)
                .build();
        NeoLoadWriter writer = new NeoLoadWriter(project, tmpDir.getAbsolutePath());
        writer.write(true);

        Assertions.assertThat(Arrays.stream(tmpDir.listFiles()).filter(file -> file.getPath().endsWith(".nlp")).findFirst()).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeProjectOnFileShouldFail() throws IOException {

        File tmpFile = new File(Files.createTempDir().getAbsoluteFile() + File.separator + "tmpfile");
        tmpFile.createNewFile();
        Project project = Project.builder()
                .name("MyTest")
                .addUserPaths(userPath)
                .build();
        NeoLoadWriter writer = new NeoLoadWriter(project, tmpFile.getAbsolutePath());
        writer.write(true);
    }

    @Test
    public void writeProjectFolderDoesNotExist() {

        File tmpDir = Files.createTempDir();
        File destDir = new File(tmpDir.getAbsoluteFile() + File.separator + "notexist");

        Project project = Project.builder()
                .name("MyTest")
                .addUserPaths(userPath)
                .build();
        NeoLoadWriter writer = new NeoLoadWriter(project, destDir.getAbsolutePath());
        writer.write(true);

        Assertions.assertThat(Arrays.stream(destDir.listFiles()).filter(file -> file.getPath().endsWith(".nlp")).findFirst()).isNotEmpty();
        Assertions.assertThat(Arrays.stream(destDir.listFiles()).filter(file -> file.getPath().endsWith(".zip")).findFirst()).isNotEmpty();
    }

    @Test
    public void normalizeCollaborativeFileNameTest() {
        Assertions.assertThat(NeoLoadWriter.normalizeCollaborativeFileName("onlylowercase.xml")).isEqualTo("onlylowercase.xml");
        Assertions.assertThat(NeoLoadWriter.normalizeCollaborativeFileName("oneUppercase.xml")).isEqualTo("one@uppercase.xml");
        Assertions.assertThat(NeoLoadWriter.normalizeCollaborativeFileName("StartWithUpper.xml")).isEqualTo("@start@with@upper.xml");
    }

    @Test
    public void validateVersionTest() {
        Assertions.assertThat(NeoLoadWriter.validateVersion(null, 3)).isNull();
        Assertions.assertThat(NeoLoadWriter.validateVersion("", 3)).isNull();
        Assertions.assertThat(NeoLoadWriter.validateVersion("2.3", 2)).isEqualTo("2.3");
        Assertions.assertThat(NeoLoadWriter.validateVersion("2.3.1", 3)).isEqualTo("2.3.1");
        Assertions.assertThat(NeoLoadWriter.validateVersion("2.3", 3)).isNull();
        Assertions.assertThat(NeoLoadWriter.validateVersion("a.3", 2)).isNull();
        Assertions.assertThat(NeoLoadWriter.validateVersion("2.a.1", 3)).isNull();
        Assertions.assertThat(NeoLoadWriter.validateVersion("2.3.a", 3)).isNull();
    }

    @Test
    public void projectWithFileVariables() throws IOException {

        File tmpDir = Files.createTempDir();
        File projectFolder = new File(tmpDir.getAbsoluteFile() + File.separator + "myProject");

        // the resource of the variable
        File resourceFile = File.createTempFile("file", "csv");

        UserPath myUserPath = UserPath.builder()
                .name("myUser")
                .actions(Container.builder()
                        .addSteps(Delay.builder()
                                .name("myDelay")
                                .value("3000")
                                .build())
                        .build())
                .build();

        Project project = Project.builder()
                .name("myProject")
                .addUserPaths(myUserPath)
                .addVariables(FileVariable.builder()
                        .path(resourceFile.getAbsolutePath())
                        .name("myVar")
                        .description("blabla")
                        .build())
                .build();

        NeoLoadWriter writer = new NeoLoadWriter(project, projectFolder.getAbsolutePath());
        writer.write(false);

        assertTrue(Paths.get(projectFolder.getAbsolutePath()).resolve("variables").resolve(resourceFile.getName()).toFile().exists());
    }

}
