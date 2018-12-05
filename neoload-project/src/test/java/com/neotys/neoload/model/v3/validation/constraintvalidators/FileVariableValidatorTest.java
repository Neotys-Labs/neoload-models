package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.variable.FileVariable;
import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileVariableValidatorTest {

	@Test
	public void testIsValid() {
		final FileVariableValidator fileVariableValidator = new FileVariableValidator();
		final FileVariable fileVariable1 = FileVariable.builder()
				.addColumnNames("Column1", "Column2")
				.isFirstLineColumnNames(false).build();
		assertTrue(fileVariableValidator.isValid(fileVariable1, null));

		final FileVariable fileVariable2 = FileVariable.builder()
				.isFirstLineColumnNames(true).build();
		assertTrue(fileVariableValidator.isValid(fileVariable2, null));

		final FileVariable fileVariable3 = FileVariable.builder().build();
		assertFalse(fileVariableValidator.isValid(fileVariable3, null));

		final FileVariable fileVariable4 = FileVariable.builder().build();
		assertFalse(fileVariableValidator.isValid(fileVariable4, null));

		final FileVariable fileVariable5 = FileVariable.builder()
				.addAllColumnNames(newArrayList()).build();
		assertFalse(fileVariableValidator.isValid(fileVariable5, null));

		final FileVariable fileVariable6 = FileVariable.builder()
				.addAllColumnNames(newArrayList())
				.isFirstLineColumnNames(true).build();
		assertTrue(fileVariableValidator.isValid(fileVariable6, null));
	}
}