package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.neotys.neoload.model.v3.project.Element;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(value = {
		@JsonSubTypes.Type(value = ConstantVariable.class, name = "constant"),
		@JsonSubTypes.Type(value = FileVariable.class, name = "file")
})
public interface Variable extends Element {
}
