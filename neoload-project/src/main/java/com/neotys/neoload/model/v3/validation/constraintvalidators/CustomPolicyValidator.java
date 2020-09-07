package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.project.Composite;
import com.neotys.neoload.model.v3.project.scenario.CustomLoadPolicy;
import com.neotys.neoload.model.v3.project.scenario.CustomPolicyStep;
import com.neotys.neoload.model.v3.project.scenario.LoadDuration;
import com.neotys.neoload.model.v3.validation.constraints.CustomLoadPolicyCheck;

import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

public final class CustomPolicyValidator extends AbstractConstraintValidator<CustomLoadPolicyCheck, CustomLoadPolicy> {
    @Override
    public boolean isValid(final CustomLoadPolicy customLoadPolicy, final ConstraintValidatorContext context) {
        if (customLoadPolicy == null ||
                (customLoadPolicy.getDuration() != null && customLoadPolicy.getDuration().getType() == null) ||
                customLoadPolicy.getSteps() == null ||
                customLoadPolicy.getSteps().isEmpty() ||
                customLoadPolicy.getSteps().stream().anyMatch(customPolicyStep ->
                        customPolicyStep.getWhen() == null || customPolicyStep.getWhen().getType() == null
                )
        ) {
            return true;
        }
        boolean isValid;
        Stream<LoadDuration.Type> typeStream = customLoadPolicy.getSteps().stream()
                .map(CustomPolicyStep::getWhen)
                .map(Composite::getType);
        if (customLoadPolicy.getDuration() == null || LoadDuration.Type.TIME.equals(customLoadPolicy.getDuration().getType())) {
            isValid = typeStream.allMatch(LoadDuration.Type.TIME::equals);
        } else {
            isValid = typeStream.allMatch(LoadDuration.Type.ITERATION::equals);
        }
        return isValid;
    }
}
