package com.neotys.neoload.model.v3.validation.constraintvalidators;

import com.neotys.neoload.model.v3.validation.constraints.ValueOfEnumCheck;

import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueOfEnumValidator extends AbstractConstraintValidator<ValueOfEnumCheck, CharSequence> {
    private List<String> acceptedValues;
    private String errorMessage;

    @Override
    public void initialize(ValueOfEnumCheck annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
        errorMessage = annotation.message() + " " + String.join(", ", acceptedValues) + ".";
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null || value.toString().trim().length() == 0) {
            return true;
        }

        if (acceptedValues.contains(value.toString())) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
            return false;
        }
    }
}
