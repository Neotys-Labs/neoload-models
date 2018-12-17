package com.neotys.neoload.model.validation.naming;

import java.util.function.Function;

import javax.validation.Path.Node;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public interface PropertyNamingStrategy extends Function<Node, String> {
}
