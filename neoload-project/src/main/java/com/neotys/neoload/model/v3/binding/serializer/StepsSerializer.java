package com.neotys.neoload.model.v3.binding.serializer;

import static com.neotys.neoload.model.v3.binding.serializer.StepsConstants.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.ImmutableMap;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.project.userpath.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StepsSerializer extends StdSerializer<List<Step>> {
	private static final long serialVersionUID = -4569870233567503685L;

	private static final Map<Class<? extends Step>, String> STEPS;
	static {
		final ImmutableMap.Builder<Class<? extends Step>, String> builder = new ImmutableMap.Builder<>();
		builder.put(ImmutableContainer.class, TRANSACTION);
		builder.put(ImmutableRequest.class, REQUEST);
		builder.put(ImmutableSoapRequest.class, SOAP_REQUEST);
		builder.put(ImmutableJavaScript.class, JAVASCRIPT);
		builder.put(ImmutableIf.class, IF);
		builder.put(ImmutableLoop.class, LOOP);
		builder.put(ImmutableWhile.class, WHILE);
		builder.put(ImmutableSwitch.class, SWITCH);
		builder.put(ImmutableCustomAction.class, CUSTOM_ACTION);
		builder.put(ImmutableTryCatch.class, TRY_CATCH);
		builder.put(ImmutableGoToNextIteration.class, GO_TO_NEXT_ITERATION);
		builder.put(ImmutableFork.class, FORK);
		builder.put(ImmutableVariableModifier.class, VARIABLE_MODIFIER);
		builder.put(ImmutableRendezvous.class, RENDEZVOUS);
		builder.put(ImmutableDebugLogger.class, DEBUG_LOGGER);
		builder.put(ImmutableWebPage.class, WEB_PAGE);
		builder.put(ImmutableWaitUntil.class, WAIT_UNTIL);
		STEPS = builder.build();
	}

	public StepsSerializer() {
		super(List.class, false);
	}

	@Override
	public boolean isEmpty(final SerializerProvider provider, final List<Step> steps) {
		return steps == null || steps.isEmpty();
	}

	@Override
	public void serialize(final List<Step> steps, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		generator.writeStartArray();
		for (final Step step : steps) {
			serializeStep(step, generator, provider);
		}
		generator.writeEndArray();
	}

	private void serializeStep(final Step step, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		if (step instanceof Delay) {
			serializeDelay((Delay) step, generator);
		} else if (step instanceof GoToNextIteration) {
			// GoToNextIteration has no properties, serialized as a bare scalar string
			generator.writeString(GO_TO_NEXT_ITERATION);
		} else if (step instanceof Rendezvous) {
			serializeRendezvous((Rendezvous) step, generator);
		} else if (step instanceof StopVU) {
			serializeStopVU((StopVU) step, generator);
		} else if (step instanceof SharedElementRef) {
			generator.writeStartObject();
			generator.writeStringField(SHARED_ELEMENT, step.getName());
			generator.writeEndObject();
		} else if (step instanceof CustomAction && RealBrowserSteps.stepOf(((CustomAction) step).getType()) != null) {
			serializeRealBrowserStep((CustomAction) step, generator, provider);
		} else {
			final String stepName = STEPS.get(step.getClass());
			if (stepName != null) {
				generator.writeStartObject();
				generator.writeObjectField(stepName, step);
				generator.writeEndObject();
			}
		}
	}

	private void serializeRealBrowserStep(final CustomAction customAction, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		final ObjectNode customActionNode;
		try (final TokenBuffer buffer = new TokenBuffer(generator.getCodec(), false)) {
			provider.defaultSerializeValue(customAction, buffer);
			customActionNode = buffer.asParser(generator.getCodec()).readValueAsTree();
		}
		customActionNode.remove(CustomAction.TYPE);
		if (RealBrowserSteps.defaultNameOf(customAction.getType()).equals(customAction.getName())) {
			customActionNode.remove(Element.NAME);
		}
		final String stepName = RealBrowserSteps.stepOf(customAction.getType());
		if (customActionNode.isEmpty()) {
			generator.writeString(stepName);
			return;
		}
		generator.writeStartObject();
		generator.writeFieldName(stepName);
		generator.writeTree(customActionNode);
		generator.writeEndObject();
	}

	private void serializeDelay(final Delay delay, final JsonGenerator generator) throws IOException {
		generator.writeStartObject();
		generator.writeFieldName(delay.isThinkTime() ? THINK_TIME : DELAY);
		writeDelay(generator, delay);
		generator.writeEndObject();
	}

	private void writeDelay(final JsonGenerator generator, final Delay delay) throws IOException {
		final Duration duration = delay.getDuration();
		final boolean implicit = delay.defaultName().equals(delay.getName()) && delay.getDescription().isEmpty();
		if (duration instanceof DurationConstant && implicit) {
			generator.writeString(DurationSerializer.toText(((DurationConstant) duration).getValue()));
			return;
		}
		generator.writeStartObject();
		if (!delay.defaultName().equals(delay.getName())) {
			generator.writeStringField(Element.NAME, delay.getName());
		}
		final String description = delay.getDescription().orElse(null);
		if (description != null && !description.trim().isEmpty()) {
			generator.writeStringField(Element.DESCRIPTION, description.trim());
		}
		DurationSerializer.writeFields(generator, duration);
		generator.writeEndObject();
	}

	private void serializeRendezvous(final Rendezvous rdv, final JsonGenerator generator) throws IOException {
		final boolean isDefault = RENDEZVOUS.equals(rdv.getName()) && rdv.getDescription().isEmpty();
		if (isDefault) {
			generator.writeString(RENDEZVOUS);
		} else {
			generator.writeStartObject();
			generator.writeObjectField(RENDEZVOUS, rdv);
			generator.writeEndObject();
		}
	}

	private void serializeStopVU(final StopVU stopVU, final JsonGenerator generator) throws IOException {
		// A StopVU keeping the default start_new_vu is serialized as a bare scalar string
		if (stopVU.getStartNewVU()) {
			generator.writeString(STOP_VU);
		} else {
			generator.writeStartObject();
			generator.writeObjectFieldStart(STOP_VU);
			generator.writeBooleanField(StopVU.START_NEW_VU, stopVU.getStartNewVU());
			generator.writeEndObject();
			generator.writeEndObject();
		}
	}
}
