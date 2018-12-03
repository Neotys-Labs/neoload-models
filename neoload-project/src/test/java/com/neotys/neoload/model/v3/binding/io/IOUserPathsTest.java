package com.neotys.neoload.model.v3.binding.io;


import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.Delay;
import com.neotys.neoload.model.v3.project.userpath.Transaction;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import com.neotys.neoload.model.v3.project.userpath.UserPath.UserSession;


public class IOUserPathsTest extends AbstractIOElementsTest {
	private static Project getUserPathsOnlyRequired() throws IOException {
        final UserPath userPath = UserPath.builder()
        		.name("MyUserPath")
        		.actions(Container.builder()
        				.addElements(Transaction.builder()
        						.name("MyTransaction")
        						.addElements(Delay.builder().delay("1000").build())
        						.build())        				
        				.build())
        		.build();
        
        final Project project = Project.builder()
        		.name("MyProject")
        		.addUserPaths(userPath)
        		.build();
        
        return project;
	}
	
	private static Project getUserPathsRequiredAndOptional() throws IOException {
        final UserPath userPath1 = UserPath.builder()
        		.name("MyUserPath1")
        		.description("My User Path 1")
        		.userSession(UserSession.RESET_ON)
        		.init(Container.builder()
        				.description("My Init Container from My User Path 1")
        				.addElements(Transaction.builder()
        						.name("MyInitTransaction")
        						.addElements(Delay.builder().delay("1000").build())
        						.build())
        				.build())
        		.actions(Container.builder()
        				.description("My Actions Container from My User Path 1")
        				.addElements(Transaction.builder()
        						.name("MyActionsTransaction")
        						.addElements(Delay.builder().delay("1000").build())
        						.build())
        				.build())
        		.end(Container.builder()
        				.description("My End Container from My User Path 1")
        				.addElements(Transaction.builder()
        						.name("MyEndTransaction")
        						.addElements(Delay.builder().delay("1000").build())
        						.build())
        				.build())
                .build();

        final UserPath userPath2 = UserPath.builder()
        		.name("MyUserPath2")
        		.description("My User Path 2")
        		.userSession(UserSession.RESET_OFF)
        		.init(Container.builder()
        				.description("My Init Container from My User Path 2")
        				.addElements(Transaction.builder()
        						.name("MyInitTransaction")
        						.addElements(Delay.builder().delay("1000").build())
        						.build())
        				.build())
        		.actions(Container.builder()
        				.description("My Actions Container from My User Path 2")
           				.addElements(Transaction.builder()
        						.name("MyActionsTransaction")
        						.addElements(Delay.builder().delay("1000").build())
        						.build())
        				.build())
        		.end(Container.builder()
        				.description("My End Container from My User Path 2")
        				.addElements(Transaction.builder()
        						.name("MyEndTransaction")
        						.addElements(Delay.builder().delay("1000").build())
        						.build())
        				.build())
                .build();

        final UserPath userPath3 = UserPath.builder()
        		.name("MyUserPath3")
        		.description("My User Path 3")
        		.userSession(UserSession.RESET_AUTO)
        		.init(Container.builder()
        				.description("My Init Container from My User Path 3")
        				.addElements(Transaction.builder()
        						.name("MyInitTransaction")
        						.addElements(Delay.builder().delay("1000").build())
        						.build())
        				.build())
        		.actions(Container.builder()
        				.description("My Actions Container from My User Path 3")
           				.addElements(Transaction.builder()
        						.name("MyActionsTransaction")
        						.addElements(Delay.builder().delay("1000").build())
        						.build())
         				.build())
        		.end(Container.builder()
        				.description("My End Container from My User Path 3")
        				.addElements(Transaction.builder()
        						.name("MyEndTransaction")
        						.addElements(Delay.builder().delay("1000").build())
        						.build())
        				.build())
                .build();

        final Project project = Project.builder()
        		.name("MyProject")
        		.addUserPaths(userPath1)
        		.addUserPaths(userPath2)
        		.addUserPaths(userPath3)
        		.build();
        
        return project;
	}

	@Test
	public void readUserPathsOnlyRequired() throws IOException, URISyntaxException {
		final Project expectedProject = getUserPathsOnlyRequired();
		assertNotNull(expectedProject);
		
		read("test-read-userpaths-only-required", expectedProject);
	}

//	@Test
//	public void readUserPathsRequiredAndOptional() throws IOException, URISyntaxException {
//		final Project expectedProject = getUserPathsRequiredAndOptional();
//		assertNotNull(expectedProject);
//		
//		read("test-read-write-userpaths-required-and-optional", expectedProject);
//	}
//
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
