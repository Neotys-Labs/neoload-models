package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.Composite;
import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.validation.constraints.CustomPolicyStepsAtLeastOneGreaterThanZeroDurationCheck;

import javax.validation.ConstraintValidatorContext;
import java.util.List;

public final class CustomPolicyStepsAtLeastOneGreaterThanZeroDurationValidator extends AbstractConstraintValidator<CustomPolicyStepsAtLeastOneGreaterThanZeroDurationCheck, List<CustomPolicyStep>> {
    @Override
    public boolean isValid(final List<CustomPolicyStep> customPolicySteps, final ConstraintValidatorContext context) {
        if (customPolicySteps == null ||
                customPolicySteps.isEmpty() ||
                customPolicySteps.stream().anyMatch(customPolicyStep ->
                        customPolicyStep.getWhen() == null || customPolicyStep.getWhen().getValue() == null
                )
        ) {
            return true;
        }
        return customPolicySteps.stream()
                .map(CustomPolicyStep::getWhen)
                .map(Composite::getValue)
                .anyMatch(integer -> integer > 0);
    }
}
