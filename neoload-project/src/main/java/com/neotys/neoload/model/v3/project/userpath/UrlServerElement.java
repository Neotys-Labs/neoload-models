package com.neotys.neoload.model.v3.project.userpath;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;
import java.util.Optional;
import javax.validation.constraints.Pattern;

public interface UrlServerElement {
	String URL = "url";
	String SERVER = "server";

	@JsonProperty(URL)
	@RequiredCheck(groups = {NeoLoad.class})
	@Pattern(regexp = "^((http[s]?):\\/\\/(([^:/\\[\\]]+)|(\\[[^/]+\\])):?((\\d+)|(\\$\\{.+\\}))?)?($|\\/.*$)", groups = {NeoLoad.class})
	String getUrl();

	@JsonProperty(SERVER)
	Optional<String> getServer();
}
