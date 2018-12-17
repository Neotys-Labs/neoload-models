package com.neotys.neoload.model.function;

import java.util.List;

import com.neotys.neoload.model.core.Element;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Deprecated
public interface Function extends Element {
	List<String> getArgs();
	String getReturnValue();	
}
