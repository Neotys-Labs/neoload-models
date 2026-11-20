package com.neotys.neoload.model.v3.project.security;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neotys.neoload.model.v3.binding.serializer.WsPasswordTypeDeserializer;
import com.neotys.neoload.model.v3.binding.serializer.WsPasswordTypeSerializer;

@JsonSerialize(using = WsPasswordTypeSerializer.class)
@JsonDeserialize(using = WsPasswordTypeDeserializer.class)
public interface WsPasswordType {
}
