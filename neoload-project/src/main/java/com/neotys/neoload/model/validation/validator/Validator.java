package com.neotys.neoload.model.validation.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.ValidatorFactory;

import com.neotys.neoload.model.validation.naming.LoadPolicyStrategy;
import com.neotys.neoload.model.validation.naming.PropertyNamingStrategy;
import com.neotys.neoload.model.validation.naming.RampupStrategy;
import com.neotys.neoload.model.validation.naming.SnakeCaseStrategy;

public final class Validator {
	private static final String PROPERTY_PATH_SEPARATOR = ".";
	private static final String LINE_SEPARATOR = System.lineSeparator();
	
	private static final Map<String, PropertyNamingStrategy> PROPERTY_NAMING_STRATEGIES;
	static {
		PROPERTY_NAMING_STRATEGIES = new HashMap<>();
		PROPERTY_NAMING_STRATEGIES.put("loadPolicy", new LoadPolicyStrategy());
		PROPERTY_NAMING_STRATEGIES.put("rampup", new RampupStrategy());
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
        final int size = violations.size();
        if (size != 0) {
        	final StringBuilder sb = new StringBuilder();
            sb.append("Data Model is invalid. Violation Number: " + size + ".");    
            sb.append(LINE_SEPARATOR);
        	int count = 1;
        	final List<String> errors = normalizeErrors(violations);
        	for (final String error : errors) {
        		sb.append("Violation " + count + " - " + error);
        		sb.append(LINE_SEPARATOR);
	        	count = count + 1;
        	}
        	
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
		final List<String> errors = new ArrayList<>();
    	for (final ConstraintViolation<T> violation : violations) {
    		errors.add("Incorrect value for '"+ normalizePath(violation.getPropertyPath()) + "': " + violation.getMessage());
    	}
    	Collections.sort(errors);
    	return errors;
	}
	
	protected String normalizePath(final Path path) {
		if (path == null) {
			return "N/A";
		}
		
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (final Iterator<Node> iterator = path.iterator(); iterator.hasNext();) {
			final Node node = iterator.next();
			final String input = node.toString();
			if (input.isEmpty()) {
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
