package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import org.immutables.value.Value;

import javax.validation.constraints.Pattern;

interface ActionDuration extends Action {
	String DEFAULT_NAME = "#duration#";

	@RequiredCheck(groups = {NeoLoad.class})
	@Value.Default
	default String getName() {
		return DEFAULT_NAME;
	}

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = "(\\d+|\\$\\{\\w+\\})", groups = {NeoLoad.class})
	String getValue();
}
