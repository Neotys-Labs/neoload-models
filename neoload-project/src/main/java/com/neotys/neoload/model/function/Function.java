package com.neotys.neoload.model.function;

import java.util.List;

import com.neotys.neoload.model.core.Element;

@Deprecated
public interface Function extends Element {
	List<String> getArgs();
	String getReturnValue();	
}
