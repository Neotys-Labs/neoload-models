package com.neotys.neoload.model.repository;

import com.neotys.neoload.model.core.Element;
import org.immutables.value.Value;

import java.util.List;

/**
 * @deprecated As of v3, replaced by an associated class from v3 version.
 */
@Value.Immutable
@Deprecated
public interface Population extends Element {
    List<PopulationSplit> getSplits();

}
