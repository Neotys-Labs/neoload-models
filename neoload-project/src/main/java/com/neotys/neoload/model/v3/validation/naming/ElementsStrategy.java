package com.neotys.neoload.model.v3.validation.naming;

import javax.validation.Path.Node;

import com.neotys.neoload.model.v3.project.userpath.Container;

public final class ElementsStrategy implements PropertyNamingStrategy {
    @Override
    public String apply(final Node node) {
    	return Container.DO;
    }		
}
