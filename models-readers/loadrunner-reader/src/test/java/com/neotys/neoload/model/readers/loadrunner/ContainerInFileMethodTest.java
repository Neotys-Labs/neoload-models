package com.neotys.neoload.model.readers.loadrunner;

import com.neotys.neoload.model.Project;
import com.neotys.neoload.model.core.Element;
import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.repository.Container;
import com.neotys.neoload.model.repository.Delay;
import com.neotys.neoload.model.repository.ImmutableDelay;
import com.neotys.neoload.model.repository.UserPath;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("squid:S2699")
public class ContainerInFileMethodTest {

	@Test
	public void containersInFileReaderTest() throws URISyntaxException {

		final URL url = this.getClass().getResource("functionsfiles");
		final File projectFolder = new File(url.toURI());

		final LoadRunnerReader lrReader = new LoadRunnerReader(new TestEventListener(), projectFolder.getPath(), "project",  "");

		final Project project = lrReader.read();
		assertThat(project).isNotNull();
		assertThat(project.getSharedElements()).isNotEmpty();
		assertThat(project.getUserPaths()).isNotEmpty();

		final MutableContainer loginContainer = new MutableContainer("Login");
		loginContainer.setShared(true);
		final MutableContainer logoutContainer = new MutableContainer("Logout");
		logoutContainer.setShared(true);
		assertThat(project.getSharedElements()).usingElementComparatorIgnoringFields("childs").containsExactly(logoutContainer, loginContainer);

		final UserPath userPath = project.getUserPaths().get(0);
		final MutableContainer vuser_initContainer = new MutableContainer("vuser_init");
		assertThat(userPath.getInitContainer().getChilds()).usingElementComparatorIgnoringFields("childs").containsExactly(loginContainer,vuser_initContainer);

		final Element loginInInit = userPath.getInitContainer().getChilds().get(0);
		final Container loginInShared = project.getSharedElements().get(1);
		assertThat(loginInInit).isSameAs(loginInShared);

		final MutableContainer actionContainer = new MutableContainer("Action");
		assertThat(userPath.getActionsContainer().getChilds()).usingElementComparatorIgnoringFields("childs").containsExactly(loginContainer, actionContainer);

		final Element loginInActions = userPath.getActionsContainer().getChilds().get(0);
		assertThat(loginInActions).isSameAs(loginInShared);

		final Delay thinkTime = ImmutableDelay.builder().isThinkTime(true).name("delay").delay("1000").build();
		final MutableContainer main = new MutableContainer("Main");
		final MutableContainer foundActionsContainers = (MutableContainer) userPath.getActionsContainer().getChilds().get(1);
		assertThat(foundActionsContainers.getChilds()).usingElementComparatorIgnoringFields("childs").containsExactly(loginContainer, thinkTime, main, logoutContainer);

		final MutableContainer foundMainTransaction = (MutableContainer) foundActionsContainers.getChilds().get(2);
		// cross reference should not be found
		assertThat(foundMainTransaction.getChilds()).hasSize(3);
		assertThat(foundMainTransaction.getChilds()).usingElementComparatorIgnoringFields("childs").doesNotContain(foundActionsContainers);

		final Element logoutInActions = foundActionsContainers.getChilds().get(3);
		final Element logoutInShared = project.getSharedElements().get(0);
		assertThat(logoutInActions).isSameAs(logoutInShared);

		assertThat(userPath.getEndContainer().getChilds()).usingElementComparatorIgnoringFields("childs").containsExactly(thinkTime, logoutContainer);

		final Element logoutInEnd = userPath.getEndContainer().getChilds().get(1);
		assertThat(logoutInEnd).isSameAs(logoutInShared);
	}
}
