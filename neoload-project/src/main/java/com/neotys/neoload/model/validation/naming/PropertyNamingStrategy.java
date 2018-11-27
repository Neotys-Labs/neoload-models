package com.neotys.neoload.model.validation.naming;

import java.util.function.Function;

import javax.validation.Path.Node;

public interface PropertyNamingStrategy extends Function<Node, String> {
}
