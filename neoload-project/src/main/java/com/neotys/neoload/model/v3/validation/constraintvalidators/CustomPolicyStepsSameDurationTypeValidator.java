package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.Composite;
import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.validation.constraints.CustomPolicyStepsSameDurationTypeCheck;

import javax.validation.ConstraintValidatorContext;
import java.util.List;

public final class CustomPolicyStepsSameDurationTypeValidator extends AbstractConstraintValidator<CustomPolicyStepsSameDurationTypeCheck, List<CustomPolicyStep>> {
    @Override
    public boolean isValid(final List<CustomPolicyStep> customPolicySteps, final ConstraintValidatorContext context) {
        if (customPolicySteps == null ||
                customPolicySteps.isEmpty() ||
                customPolicySteps.stream().anyMatch(customPolicyStep ->
                        customPolicyStep.getWhen() == null || customPolicyStep.getWhen().getType() == null
                )
        ) {
            return true;
        }
        return customPolicySteps.stream()
                .map(CustomPolicyStep::getWhen)
                .map(Composite::getType)
                .distinct()
                .count() == 1;
    }
}
