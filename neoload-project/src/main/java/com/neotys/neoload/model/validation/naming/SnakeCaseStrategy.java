package com.neotys.neoload.model.validation.naming;

import javax.validation.Path.Node;

public final class SnakeCaseStrategy implements PropertyNamingStrategy {
    public SnakeCaseStrategy() {
		super();
	}

    @Override
    public String apply(final Node node) {
        if (node == null) return null; // garbage in, garbage out
        
        return translate(node.toString());
    }
    
    protected String translate(final String input) {
        int length = input.length();
        StringBuilder result = new StringBuilder(length * 2);
        int resultLength = 0;
        boolean wasPrevTranslated = false;
        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            if ((i == 0) && (c == '_')) continue;
            
            if (Character.isUpperCase(c)) {
                if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_') {
                    result.append('_');
                    resultLength++;
                }
                c = Character.toLowerCase(c);
                wasPrevTranslated = true;
            }
            else {
                wasPrevTranslated = false;
            }
            result.append(c);
            resultLength++;
        }
        return resultLength > 0 ? result.toString() : input;   	
    }
}
