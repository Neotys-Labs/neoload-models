package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import javax.validation.Valid;

interface PacingElement {
	String PACING = "pacing";

	@JsonProperty(PACING)
	@Valid
	Optional<Pacing> getPacing();
}
