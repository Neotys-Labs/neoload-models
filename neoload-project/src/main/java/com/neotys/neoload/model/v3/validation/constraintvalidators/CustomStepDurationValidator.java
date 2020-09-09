package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.validation.constraints.CustomStepDurationCheck;

import javax.validation.ConstraintValidatorContext;

public final class CustomStepDurationValidator extends AbstractConstraintValidator<CustomStepDurationCheck, LoadDuration> {
    @Override
    public boolean isValid(final LoadDuration loadDuration, final ConstraintValidatorContext context) {
        if (loadDuration == null) {
            return true;
        }
        return loadDuration.getValue() != null
                && loadDuration.getValue() >= 0
                && loadDuration.getType() != null;
    }
}
