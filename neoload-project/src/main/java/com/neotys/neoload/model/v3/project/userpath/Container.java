package com.neotys.neoload.model.v3.project.userpath;

import java.util.List;

import javax.validation.Valid;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.neotys.neoload.model.v3.project.Element;
import com.neotys.neoload.model.v3.validation.constraints.RequiredCheck;
import com.neotys.neoload.model.v3.validation.groups.NeoLoad;

@JsonInclude(value=Include.NON_EMPTY)
@JsonPropertyOrder({Element.NAME, Element.DESCRIPTION, Container.DO})
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Container extends Element {
	public static final String DO = "do";

	@JsonProperty(DO)
	@RequiredCheck(groups={NeoLoad.class})
	@Valid
	List<Element> getElements();
	
	@JsonIgnore
	@Value.Default()
	default boolean isShared(){
		return false;
	}
}
