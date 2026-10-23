package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static com.neotys.neoload.model.v3.project.userpath.CustomActionParameter.Type.PASSWORD;
import static junit.framework.TestCase.assertNotNull;

import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.CustomAction;
import com.neotys.neoload.model.v3.project.userpath.CustomActionParameter;
import com.neotys.neoload.model.v3.project.userpath.assertion.DurationAssertion;
import java.io.IOException;
import org.junit.Test;

/**
 * A Real Browser step ({@code browser_click}...) is read as a {@link CustomAction} whose type is given by the
 * step name, and a {@link CustomAction} of a Real Browser type is written back as that step.
 */
public class IORealBrowserActionTest extends AbstractIOElementsTest {

	@Test
	public void readRealBrowserActions() throws IOException {
		read("test-real-browser-actions", buildProject(getRealBrowserActions()));
	}

	@Test
	public void readRealBrowserActionsWrittenAsCustomAction() throws IOException {
		read("test-real-browser-actions-as-custom-action", buildProject(getRealBrowserActions()));
	}

	@Test
	public void writeRealBrowserActions() throws IOException {
		final Project expectedProject = buildProject(getRealBrowserActions());
		assertNotNull(expectedProject);

		write("test-real-browser-actions", expectedProject);
	}

	private static CustomAction[] getRealBrowserActions() {
		return new CustomAction[] {
				CustomAction.builder()
						.name("BrowserOpen")
						.type("BrowserOpen")
						.addParameters(parameter("browser", "chromium"), parameter("resolution", "1920x1080"))
						.build(),
				CustomAction.builder()
						.name("Go to home")
						.description("Home page")
						.type("BrowserNavigate")
						.addParameters(parameter("url", "https://www.tricentis.com/"))
						.asRequest(true)
						.durationAssertion(DurationAssertion.builder().lessThan(5000L).build())
						.build(),
				CustomAction.builder()
						.name("Type password")
						.type("BrowserType")
						.addParameters(
								parameter("selector", "#password"),
								CustomActionParameter.builder().name("encryptedText").value("myPassword").type(PASSWORD).build())
						.build(),
				CustomAction.builder()
						.name("Select country")
						.type("BrowserSelectOption")
						.addParameters(parameter("selector", "#country"), parameter("options[0].text", "France"))
						.build(),
				CustomAction.builder()
						.name("Upload files")
						.type("BrowserUploadFile")
						.addParameters(
								parameter("selector", "input[type=file]"),
								parameter("filePaths", "${NL-CustomResources}/a.txt;${NL-CustomResources}/b.txt"))
						.build(),
				CustomAction.builder()
						.name("BrowserOpenTab")
						.type("BrowserOpenTab")
						.build(),
				CustomAction.builder()
						.name("sql action")
						.type("SQL")
						.build()
		};
	}

	private static CustomActionParameter parameter(final String name, final String value) {
		return CustomActionParameter.builder().name(name).value(value).build();
	}
}
