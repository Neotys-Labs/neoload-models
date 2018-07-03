package com.neotys.models.repository;

import com.neotys.models.core.Element;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface Population extends Element {
    List<PopulationSplit> getSplits();

}
