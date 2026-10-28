package com.neotys.neoload.model.v3.validation.validator;

import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.binding.converter.DurationParser;
import com.neotys.neoload.model.v3.project.Composite;
import com.neotys.neoload.model.v3.project.userpath.DurationLiteral;
import com.neotys.neoload.model.v3.validation.naming.DurationStrategy;
import com.neotys.neoload.model.v3.validation.naming.LoadPolicyStrategy;
import com.neotys.neoload.model.v3.validation.naming.PropertyNamingStrategy;
import com.neotys.neoload.model.v3.validation.naming.RampupStrategy;
import com.neotys.neoload.model.v3.validation.naming.SnakeCaseStrategy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.ValidatorFactory;

public final class Validator {
	private static final String PROPERTY_PATH_SEPARATOR = ".";
	private static final String LINE_SEPARATOR = System.lineSeparator();
	
	private static final Map<String, PropertyNamingStrategy> PROPERTY_NAMING_STRATEGIES;
	static {
		PROPERTY_NAMING_STRATEGIES = new HashMap<>();
		PROPERTY_NAMING_STRATEGIES.put("loadPolicy", new LoadPolicyStrategy());
		PROPERTY_NAMING_STRATEGIES.put("rampup", new RampupStrategy());
		PROPERTY_NAMING_STRATEGIES.put("duration", new DurationStrategy());
	}
	
	private static final PropertyNamingStrategy defaultNamingStrategy = new SnakeCaseStrategy();
	
	public Validator() {
		super();
	}

	public <T> Validation validate(final T object, Class<?>... groups) {
    	// validate the specified object
		final Configuration<?> configuration = javax.validation.Validation.byDefaultProvider().configure();
		configuration.messageInterpolator(new ResourceBundleMessageInEnglishInterpolator());
		final ValidatorFactory factory = configuration.buildValidatorFactory();
        
		final javax.validation.Validator validator = factory.getValidator();
        final Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
      
        // Manages the results
        if (!violations.isEmpty()) {
        	final StringBuilder sb = new StringBuilder();
            int count = 1;
        	final List<String> errors = normalizeErrors(violations);
        	for (final String error : errors) {
        		sb.append("Violation ").append(count).append(" - ").append(error);
        		sb.append(LINE_SEPARATOR);
	        	count = count + 1;
        	}
        	
        	sb.insert(0, LINE_SEPARATOR);
        	sb.insert(0, "Data Model is invalid. Violation Number: " + errors.size() + ".");    
        	
        	return Validation.builder()
            		.message(sb.toString())
            		.isValid(false)
            		.build();
        }
        return Validation.builder()
        		.isValid(true)
        		.build();
	}
	
	protected <T> List<String> normalizeErrors(final Set<ConstraintViolation<T>> violations) {
		final Set<String> errors = new HashSet<>(); // --> Only one same error Validation <-> Immutable
    	for (final ConstraintViolation<T> violation : violations) {
    		final String invalidValue = describeValue(violation.getInvalidValue()).map(value -> " '" + value + "'").orElse("");
    		errors.add("Incorrect value" + invalidValue + " for '"+ normalizePath(violation.getPropertyPath()) + "': " + violation.getMessage());
    	}
    	final List<String> sortedErrors = new ArrayList<>(errors);
    	Collections.sort(sortedErrors);
    	return sortedErrors;
	}
	
	/**
	 * Text of a rejected number or duration, so that the error names the value; empty for a missing value, a text
	 * (whose checks name the value in their message when needed) or a whole element.
	 */
	private static Optional<String> describeValue(final Object value) {
		if (value instanceof Number) {
			return Optional.of(value.toString());
		}
		if (value instanceof Duration) {
			return Optional.of(DurationParser.format((Duration) value));
		}
		if (value instanceof DurationLiteral) {
			return describeValue(((DurationLiteral) value).getValue());
		}
		if (value instanceof Composite) {
			return describeValue(((Composite<?, ?>) value).getValue());
		}
		if (value instanceof Optional) {
			return ((Optional<?>) value).flatMap(Validator::describeValue);
		}
		return Optional.empty();
	}

	protected String normalizePath(final Path path) {
		if (path == null) {
			return "N/A";
		}
		
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (final Node node : path) {
			final String input = node.getName();
			if (Strings.isNullOrEmpty(input)) {
				continue;
			}

			if (!first) {
				builder.append(PROPERTY_PATH_SEPARATOR);
			}

			PropertyNamingStrategy namingStrategy = PROPERTY_NAMING_STRATEGIES.get(input);
			if (namingStrategy == null) {
				namingStrategy = defaultNamingStrategy;
			}
			final String output = namingStrategy.apply(node);
			builder.append(output);

			first = false;
		}
		return builder.toString();
	}
}
