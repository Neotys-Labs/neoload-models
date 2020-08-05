package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.project.Element.DESCRIPTION;
import static com.neotys.neoload.model.v3.project.Element.NAME;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.ACTIONS;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.DEFAULT_USER_SESSION;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.END;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.INIT;
import static com.neotys.neoload.model.v3.project.userpath.UserPath.USER_SESSION;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.neotys.neoload.model.v3.project.userpath.Container;
import com.neotys.neoload.model.v3.project.userpath.UserPath;

public final class UserPathSerializer extends StdSerializer<UserPath> {
	private static final long serialVersionUID = -9059965357953456780L;

	public UserPathSerializer() {
		super(UserPath.class);
	}

	@Override
	public void serialize(final UserPath userPath, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeStartObject();
        
		generator.writeStringField(NAME, userPath.getName());
        final Optional<String> description = userPath.getDescription();
        if (description.isPresent()) {
        	generator.writeStringField(DESCRIPTION, description.get());
        }
        
        final UserPath.UserSession userSession = userPath.getUserSession();
        if (userSession != DEFAULT_USER_SESSION) {
        	generator.writeObjectField(USER_SESSION, userSession);
        }
    	
        final Optional<Container> init = userPath.getInit();
        if (init.isPresent()) {
        	generator.writeObjectField(INIT, Container.builder().from(init.get()).name("").build());
        }
        final Container actions = userPath.getActions();
        if (actions != null) {
        	generator.writeObjectField(ACTIONS, Container.builder().from(actions).name("").build());
        }
        final Optional<Container> end = userPath.getEnd();
        if (end.isPresent()) {
        	generator.writeObjectField(END, Container.builder().from(end.get()).name("").build());
        }
        
        generator.writeEndObject();		
	}
}