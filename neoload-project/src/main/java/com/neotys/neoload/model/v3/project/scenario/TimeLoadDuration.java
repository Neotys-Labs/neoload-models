package com.neotys.neoload.model.v3.project.scenario;

import com.neotys.neoload.model.v3.validation.constraints.WholeSecondsCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.time.Duration;
import org.immutables.value.Value;

@WholeSecondsCheck(groups={NeoLoad.class})
@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
public interface TimeLoadDuration extends LoadDuration {
	Duration getValue();

	@Override
	default Type getType() {
		return Type.TIME;
	}

	@Override
	default long getAmount() {
		return getValue().getSeconds();
	}

	class Builder extends ImmutableTimeLoadDuration.Builder {}
	static Builder builder() {
		return new Builder();
	}
}
