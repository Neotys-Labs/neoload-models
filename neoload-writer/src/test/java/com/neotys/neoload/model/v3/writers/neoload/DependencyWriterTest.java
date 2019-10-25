package com.neotys.neoload.model.v3.writers.neoload;

import com.neotys.neoload.model.v3.project.Dependency;
import com.neotys.neoload.model.v3.project.DependencyType;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

public class DependencyWriterTest  {

    @Test
    public void testSourceIsFile() throws IOException, ParserConfigurationException {
        Path tmpSrcDir = Files.createTempDirectory("dirsrc");
        Path tmpDestDir = Files.createTempDirectory("dirdest");
        File tmpFile = new File(tmpSrcDir.toFile(),"test.lib");
        tmpFile.createNewFile();

        Dependency dependency = Dependency.builder()
                .name("test js lib")
                .description(Optional.empty())
                .filename(tmpFile.getName())
                .fileDirectory(tmpSrcDir.toString())
                .type(DependencyType.JS_LIBRARY)
                .build();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document repositoryDocument = docBuilder.newDocument();

        DependencyWriter writer = new DependencyWriter(dependency);
        Element root = repositoryDocument.createElement("root");
        writer.writeXML(repositoryDocument, root, tmpDestDir.toString());

        File dest = tmpDestDir.resolve(DependencyType.JS_LIBRARY.getLocationFolderName()).resolve(tmpFile.getName()).toFile();
        assertTrue(dest.exists());

        assertTrue("js-library".equals(root.getFirstChild().getNodeName()));
        assertTrue("test js lib".equals(root.getFirstChild().getAttributes().getNamedItem("name").getNodeValue()));
        assertTrue(dest.toString().equals(root.getFirstChild().getAttributes().getNamedItem("filename").getNodeValue()));
    }

    @Test
    public void testSourceIsInputStream() throws IOException, ParserConfigurationException {
        Path tmpDestDir = Files.createTempDirectory("dirdest");
        String tmpFile = "filevariable.csv";

        Dependency dependency = Dependency.builder()
                .name("test js lib")
                .description(Optional.empty())
                .filename(tmpFile)
                .inputStream(this.getClass().getResourceAsStream(tmpFile))
                .type(DependencyType.JS_LIBRARY)
                .build();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document repositoryDocument = docBuilder.newDocument();

        DependencyWriter writer = new DependencyWriter(dependency);
        Element root = repositoryDocument.createElement("root");
        writer.writeXML(repositoryDocument, root, tmpDestDir.toString());

        File dest = tmpDestDir.resolve(DependencyType.JS_LIBRARY.getLocationFolderName()).resolve(tmpFile).toFile();
        assertTrue(dest.exists());

        assertTrue("js-library".equals(root.getFirstChild().getNodeName()));
        assertTrue("test js lib".equals(root.getFirstChild().getAttributes().getNamedItem("name").getNodeValue()));
        assertTrue(dest.toString().equals(root.getFirstChild().getAttributes().getNamedItem("filename").getNodeValue()));
    }
}
