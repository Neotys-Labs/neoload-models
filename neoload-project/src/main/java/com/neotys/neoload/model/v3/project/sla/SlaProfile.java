package com.neotys.neoload.model.v3.project.sla;


import java.util.List;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.constraints.SlaThresholdsCheck.UsageType;
import com.neotys.neoload.model.v3.validation.constraints.SlaThresholdsCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value = Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, SlaProfile.THRESHOLDS})
@JsonDeserialize(as = ImmutableSlaProfile.class)
@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface SlaProfile extends Element {
	String THRESHOLDS = "thresholds";

	@RequiredCheck(groups = {NeoLoad.class})
	@SlaThresholdsCheck(usage = UsageType.CHECK_UNIQUE_KPI_AND_SCOPE, from = SlaProfile.class, message = "{com.neotys.neoload.model.v3.validation.constraints.SlaThresholdsCheck.UniqueKpiAndScope.message}", groups = {NeoLoad.class})
	@SlaThresholdsCheck(usage = UsageType.CHECK_LIST_OF_KPIS_FROM_ELEMENT, from = SlaProfile.class, message = "{com.neotys.neoload.model.v3.validation.constraints.SlaThresholdsCheck.ListOfKpisFromSlaProfile.message}", groups = {NeoLoad.class})
	@Valid
	List<SlaThreshold> getThresholds();

	class Builder extends ImmutableSlaProfile.Builder {
	}
	static Builder builder() {
		return new Builder();
	}
}
