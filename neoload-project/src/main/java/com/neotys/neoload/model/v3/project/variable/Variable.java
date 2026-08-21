package com.neotys.neoload.model.v3.project.variable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.neotys.neoload.model.v3.project.Element;

// Subtypes are mapped to the generated Immutable* classes (not the interfaces) so that the
// polymorphic type id can be resolved at serialization time from the runtime object, which is
// always an Immutable* instance.
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(value = {
		@JsonSubTypes.Type(value = ImmutableConstantVariable.class, name = "constant"),
		@JsonSubTypes.Type(value = ImmutableFileVariable.class, name = "file"),
		@JsonSubTypes.Type(value = ImmutableCounterVariable.class, name = "counter"),
		@JsonSubTypes.Type(value = ImmutableRandomNumberVariable.class, name = "random_number"),
		@JsonSubTypes.Type(value = ImmutableRandomStringVariable.class, name = "random_string"),
		@JsonSubTypes.Type(value = ImmutableRandomUUIDVariable.class, name = "random_uuid"),
		@JsonSubTypes.Type(value = ImmutableJavaScriptVariable.class, name = "javascript"),
		@JsonSubTypes.Type(value = ImmutableSharedQueueVariable.class, name = "shared_queue"),
		@JsonSubTypes.Type(value = ImmutableListVariable.class, name = "list"),
})
public interface Variable extends Element {
}
