package com.neotys.neoload.model.v3.validation.naming;

import javax.validation.Path.Node;

import com.google.common.base.Strings;
import com.neotys.neoload.model.v3.project.userpath.Container;

public final class ElementsStrategy implements PropertyNamingStrategy {
	public static final String PROPERTY_NAME = "elements";
	
	@Override
    public String apply(final Node node) {
		final String str = node.toString();
		if (Strings.isNullOrEmpty(str)) {
			return Container.DO;
		}
		return str.replace(PROPERTY_NAME, Container.DO);
    }		
}
