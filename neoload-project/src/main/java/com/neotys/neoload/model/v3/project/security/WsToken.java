package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A WS-Security token: a username token or a timestamp token.
 * Subtypes are mapped to the generated Immutable* classes (not the interfaces) so that the polymorphic
 * type id can be resolved at serialization time from the runtime object, which is always an Immutable* instance.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(value = {
		@JsonSubTypes.Type(value = ImmutableWsUsernameToken.class, name = "username"),
		@JsonSubTypes.Type(value = ImmutableWsTimestampToken.class, name = "timestamp")
})
public interface WsToken {
}
