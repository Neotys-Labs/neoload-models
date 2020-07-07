package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.ValueOfEnumCheck;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValueOfEnumValidatorTest {

    private enum TestEnum {
        VALUE1, VALUE2
    }

    @ValueOfEnumCheck(enumClass = TestEnum.class)
    private String fieldToCheck;

    @Test
    public void isValid() throws NoSuchFieldException {
        ValueOfEnumValidator validator = new ValueOfEnumValidator();
        validator.initialize(ValueOfEnumValidatorTest.class.getDeclaredField("fieldToCheck").getAnnotation(ValueOfEnumCheck.class));

        assertTrue(validator.isValid(null, null));
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid("  ", null));
        assertTrue(validator.isValid("VALUE1", null));
        assertTrue(validator.isValid("VALUE2", null));

        ConstraintValidatorContext contextMock = new ConstraintValidatorContextImpl(
                null,
                PathImpl.createPathFromString("mock"),
                null,
                null);
        assertFalse(validator.isValid("VALUE3", contextMock));
    }
}