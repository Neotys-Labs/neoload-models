package com.neotys.neoload.model.v3.binding.io;

import static com.neotys.neoload.model.v3.binding.io.IOHelper.buildProject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.neotys.neoload.model.v3.binding.io.IO.Format;
import com.neotys.neoload.model.v3.project.Project;
import com.neotys.neoload.model.v3.project.userpath.*;
import com.neotys.neoload.model.v3.project.userpath.Durations;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import com.neotys.neoload.model.v3.validation.validator.Validation;
import com.neotys.neoload.model.v3.validation.validator.Validator;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class IOSwitchTest extends AbstractIOElementsTest  {

    private static Switch getSwitchOnlyRequired() {
        return Switch.builder()
        		.value("${MySwitchVariable}")
				.addCases(Case.builder()
						.value("0")
						.isBreak(true)
						.addSteps(Delay.builder()
								.duration(DurationConstant.builder().value(Durations.of("1000")).build())
								.build())
						.build())
                .getDefault(Container.builder()
                		.addSteps(Delay.builder()
                				.duration(DurationConstant.builder().value(Durations.of("3000")).build())
                				.build())
                		.build())                
                .build();
    }
    
    private static Switch getSwitchRequiredAndOptional() {
        return Switch.builder()
        		.name("MySwitch")
                .description("MySwitchDescription")
                .value("${MySwitchVariable}")
                .addCases(Case.builder()
                		.value("0")
                        .isBreak(true)
                        .addSteps(Delay.builder()
                        		.duration(DurationConstant.builder().value(Durations.of("1000")).build())
                        		.build())
                        .build())
				.addCases(Case.builder()
						.description("MyCase1Description")
						.value("1")
						.isBreak(false)
						.addSteps(Delay.builder()
								.duration(DurationConstant.builder().value(Durations.of("1000")).build())
								.build())
						.addContentAssertions(ContentAssertion.builder()
								.contains("MyCase1Assertion on Content")
								.build())
						.build())
                .getDefault(Container.builder()
                		.addSteps(Delay.builder()
                				.duration(DurationConstant.builder().value(Durations.of("3000")).build())
                				.build())
                		.build())                
                .build();
    }


    private static Switch getSwitchCaseValueFreeText() {
        return Switch.builder()
                .value("${MySwitchVariable}")
                .addCases(Case.builder()
                        .value("0")
                        .isBreak(true)
                        .addSteps(Delay.builder()
                                .duration(DurationConstant.builder().value(Durations.of("1000")).build())
                                .build())
                        .build())
                // a case value is free text compared literally, not an element name: no character is forbidden
                .addCases(Case.builder()
                        .value("${literal} #1 | $100")
                        .isBreak(true)
                        .addSteps(Delay.builder()
                                .duration(DurationConstant.builder().value(Durations.of("1000")).build())
                                .build())
                        .build())
                .getDefault(Container.builder()
                        .addSteps(Delay.builder()
                                .duration(DurationConstant.builder().value(Durations.of("3000")).build())
                                .build())
                        .build())
                .build();
    }

    @Test
    public void readCaseValueWithForbiddenNameCharactersAccepted() throws IOException {
        final Project expectedProject = buildProject(getSwitchCaseValueFreeText());
        assertNotNull(expectedProject);

        assertRead("test-switch-case-value-free-text", expectedProject);
    }

    @Test
    public void writeCaseValueWithForbiddenNameCharacters() throws IOException {
        final Project expectedProject = buildProject(getSwitchCaseValueFreeText());
        assertNotNull(expectedProject);

        assertWrite("test-switch-case-value-free-text", expectedProject);
    }

    @Test
    public void readCaseNameIgnoredAndNeverWritten() throws IOException {
        // A case has no name in the as-code: one written in a file is ignored, and NeoLoad names the case after its value
        final ProjectDescriptor descriptor = new IO().read(getFile("test-switch-case-name-ignored", "yaml"), StandardCharsets.UTF_8);
        final Switch aSwitch = (Switch) descriptor.getProject().getUserPaths().get(0).getActions().getSteps().get(0);
        assertEquals(Case.DEFAULT_NAME, aSwitch.getCases().get(0).getName());
        assertEquals("Case 0", aSwitch.getCases().get(0).displayName());
        assertFalse(new IO().write(descriptor, Format.YAML).contains("legacy_name"));
    }

    @Test
    public void readElementNameStillValidatedAroundFreeTextCaseValue() throws IOException {
        // A case value is free text, not an element name: only the name of the transaction holding the case is reported
        final ProjectDescriptor descriptor = new IO().read(getFile("test-element-name-forbidden-character", "yaml"), StandardCharsets.UTF_8);
        final Validation validation = new Validator().validate(descriptor, NeoLoad.class);
        assertFalse(validation.isValid());
        final String message = validation.getMessage().orElse("");
        assertTrue("unexpected message: " + message, message.startsWith("Data Model is invalid. Violation Number: 1."));
        assertTrue("unexpected message: " + message,
                message.contains("'project.user_paths[0].actions.steps[0].name': element name 'bad#name' contains forbidden character '#'."));
    }

    @Test
    public void readSwitchOnlyRequired() throws IOException {
        final Project expectedProject = buildProject(getSwitchOnlyRequired());
        assertNotNull(expectedProject);

        read("test-switch-only-required", expectedProject);
    }
    
    @Test
    public void readSwitchRequiredAndOptional() throws IOException {
        final Project expectedProject = buildProject(getSwitchRequiredAndOptional());
        assertNotNull(expectedProject);

        read("test-switch-required-and-optional", expectedProject);
    }

    @Test
    public void writeSwitchOnlyRequired() throws IOException {
        final Project expectedProject = buildProject(getSwitchOnlyRequired());
        assertNotNull(expectedProject);

        write("test-switch-only-required", expectedProject);
    }

    @Test
    public void writeSwitchRequiredAndOptional() throws IOException {
        final Project expectedProject = buildProject(getSwitchRequiredAndOptional());
        assertNotNull(expectedProject);

        write("test-switch-required-and-optional", expectedProject);
    }

}
