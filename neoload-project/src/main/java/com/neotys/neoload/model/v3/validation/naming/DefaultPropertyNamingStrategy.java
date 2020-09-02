package com.neotys.neoload.model.v3.validation.naming;

import javax.validation.Path.Node;

public final class DefaultPropertyNamingStrategy implements PropertyNamingStrategy {
	private final String friendlyName;
		
	public DefaultPropertyNamingStrategy(String friendlyName) {
		super();
		
		this.friendlyName = friendlyName;
	}

	@Override
	public String apply(final Node node) {
		return node.toString().replace(node.getName(), friendlyName);
	}
}
