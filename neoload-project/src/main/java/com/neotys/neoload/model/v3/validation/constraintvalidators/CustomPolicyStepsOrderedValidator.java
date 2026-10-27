package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.validation.constraints.CustomPolicyStepsOrderedCheck;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.validation.ConstraintValidatorContext;

public final class CustomPolicyStepsOrderedValidator extends AbstractConstraintValidator<CustomPolicyStepsOrderedCheck, List<CustomPolicyStep>> {
    @Override
    public boolean isValid(final List<CustomPolicyStep> customPolicySteps, final ConstraintValidatorContext context) {
        if(customPolicySteps == null ||
                customPolicySteps.isEmpty() ||
                customPolicySteps.stream().anyMatch(customPolicyStep ->
                        customPolicyStep.getWhen() == null
                )
        ){
            return true;
        }
        List<CustomPolicyStep> customPolicyStepsSorted = new ArrayList<>(customPolicySteps);
        customPolicyStepsSorted.sort(Comparator.comparingLong(o -> o.getWhen().getAmount()));
        return customPolicyStepsSorted.equals(customPolicySteps);
    }
}
