package com.neotys.neoload.model.v3.project.userpath;

import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

import javax.validation.constraints.Pattern;

interface ActionDuration extends Action {

	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = "((((\\d+)(h\\s*))?((\\d+)(m\\s*))?((\\d+)(s\\s*))?((\\d+)(ms\\s*))?)|(\\d+))", groups = {NeoLoad.class})
	String getValue();
}
