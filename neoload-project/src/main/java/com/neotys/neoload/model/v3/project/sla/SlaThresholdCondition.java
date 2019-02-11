package com.neotys.neoload.model.v3.project.sla;


import javax.validation.constraints.PositiveOrZero;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface SlaThresholdCondition  {
	enum Severity {
		WARN,
		FAIL;
		
    	public static Severity of(final String name) {
    		if (Strings.isNullOrEmpty(name)) {
    			throw new IllegalArgumentException("The parameter 'name' must not be null or empty.");
    		}
    		
    		try {
    			return Severity.valueOf(name.toUpperCase());
    		}
    		catch (final IllegalArgumentException iae) {
    			throw new IllegalArgumentException("The parameter 'name' must be: 'warn' or 'fail'.");	
			}    		
    	}
    	
		public String friendlyName() {
			return name().toLowerCase();
		}
	}
	
    enum Operator {
    	LESS_THAN("<="),
        GREATER_THAN(">="),
        EQUALS("==");
    	
    	private final String friendlyName;
    	
    	private Operator(String friendlyName) {
			this.friendlyName = friendlyName;
		}

		public static Operator of(final String name) {
    		if (Strings.isNullOrEmpty(name)) {
    			throw new IllegalArgumentException("The parameter 'name' must not be null or empty.");
    		}
    		
    		if (LESS_THAN.friendlyName().equals(name)) {
    			return LESS_THAN;
    		}
    		else if (GREATER_THAN.friendlyName().equals(name)) {
    			return GREATER_THAN;
    		}
    		else if (EQUALS.friendlyName().equals(name)) {
    			return EQUALS;
    		}
    		throw new IllegalArgumentException("The parameter 'name' must be: '<=', '>=' or '=='.");	
    	}
    	
		public String friendlyName() {
			return friendlyName;
		}
    }

	@RequiredCheck(groups = {NeoLoad.class})
	Severity getSeverity();
	
	@RequiredCheck(groups = {NeoLoad.class})
	Operator getOperator();
	
	@RequiredCheck(groups = {NeoLoad.class})
	@PositiveOrZero(groups = {NeoLoad.class})
	Double getValue();
	
	class Builder extends ImmutableSlaThresholdCondition.Builder {
	}

	static Builder builder() {
		return new Builder();
	}
}
