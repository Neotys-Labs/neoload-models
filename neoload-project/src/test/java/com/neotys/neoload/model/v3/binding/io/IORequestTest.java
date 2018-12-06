package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Header;
import com.neotys.neoload.model.v3.project.userpath.Request;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.Request.Method;


public class IORequestTest extends AbstractIOElementsTest {
	private static Project getRequestOnlyRequired() throws IOException {
        final UserPath userPath = UserPath.builder()
        		.name("MyUserPath")
        		.actions(Container.builder()
        				.name("actions")
        				.addElements(Request.builder()
        						.url("http://www.neotys.com/select?name:neoload")
        						.build())        				
        				.build())
        		.build();
        
        final Project project = Project.builder()
        		.name("MyProject")
        		.addUserPaths(userPath)
        		.build();
        
        return project;
	}
	
	private static Project getRequestRequiredAndOptional() throws IOException {
        final UserPath userPath = UserPath.builder()
        		.name("MyUserPath")
        		.actions(Container.builder()
        				.name("actions")
        				.addElements(Request.builder()
        						.url("/select?name=neoload")
        						.server("neotys")
        						.method(Method.POST.name())
        						.addHeaders(Header.builder()
        								.name("Content-Type")
        								.value("text/html; charset=utf-8")
        								.build())
           						.addHeaders(Header.builder()
        								.name("Accept-Encoding")
        								.value("gzip, compress, br")
        								.build())
        						.body("My Body\nline 1\nline 2\n")
        						.build())     
        				.addElements(Request.builder()
        						.url("/select?name=neoload")
        						.server("neotys")
        						.method(Method.POST.name())
        						.addHeaders(Header.builder()
        								.name("Content-Type")
        								.value("text/html; charset=utf-8")
        								.build())
           						.addHeaders(Header.builder()
        								.name("Accept-Encoding")
        								.value("gzip, compress, br")
        								.build())
        						.body("My Body line 1 line 2")
        						.build())        				

        				.build())
        		.build();

 
        final Project project = Project.builder()
        		.name("MyProject")
        		.addUserPaths(userPath)
        		.build();
        
        return project;
	}

	@Test
	public void readUserPathsOnlyRequired() throws IOException, URISyntaxException {
		final Project expectedProject = getRequestOnlyRequired();
		assertNotNull(expectedProject);
		
		read("test-request-only-required", expectedProject);
	}

	@Test
	public void readUserPathsRequiredAndOptional() throws IOException, URISyntaxException {
		final Project expectedProject = getRequestRequiredAndOptional();
		assertNotNull(expectedProject);
		
		read("test-request-required-and-optional", expectedProject);
	}

//	@Test
//	public void writeUserPathsOnlyRequired() throws IOException, URISyntaxException {
//		final Project expectedProject = getUserPathsOnlyRequired();
//		assertNotNull(expectedProject);
//		
//		write("test-write-userpaths-only-required", expectedProject);
//	}
//
//	@Test
//	public void writeUserPathsRequiredAndOptional() throws IOException, URISyntaxException {
//		final Project expectedProject = getUserPathsRequiredAndOptional();
//		assertNotNull(expectedProject);
//		
//		write("test-read-write-userpaths-required-and-optional", expectedProject);
//	}
}
