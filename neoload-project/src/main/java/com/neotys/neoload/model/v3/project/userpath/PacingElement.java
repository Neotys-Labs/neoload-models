package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.validation.constraints.PacingRandomMaxGreaterThanMinCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import javax.validation.Valid;

interface PacingElement {
	String PACING = "pacing";

	@JsonProperty(PACING)
	@Valid
	Optional<@PacingRandomMaxGreaterThanMinCheck(groups = {NeoLoad.class}) Duration> getPacing();
}
