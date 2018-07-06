package com.neotys.neoload.model.repository;

import com.neotys.neoload.model.core.Element;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface Population extends Element {
    List<PopulationSplit> getSplits();

}
