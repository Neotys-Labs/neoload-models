package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.validation.constraints.CustomPolicyStepsAtLeastOneGreaterThanZeroDurationCheck;
import java.util.List;
import javax.validation.ConstraintValidatorContext;

public final class CustomPolicyStepsAtLeastOneGreaterThanZeroDurationValidator extends AbstractConstraintValidator<CustomPolicyStepsAtLeastOneGreaterThanZeroDurationCheck, List<CustomPolicyStep>> {
    @Override
    public boolean isValid(final List<CustomPolicyStep> customPolicySteps, final ConstraintValidatorContext context) {
        if (customPolicySteps == null ||
                customPolicySteps.isEmpty() ||
                customPolicySteps.stream().anyMatch(customPolicyStep ->
                        customPolicyStep.getWhen() == null || !customPolicyStep.getWhen().amount().isPresent()
                )
        ) {
            return true;
        }
        return customPolicySteps.stream()
                .map(CustomPolicyStep::getWhen)
                .anyMatch(when -> when.amount().getAsLong() > 0);
    }
}
