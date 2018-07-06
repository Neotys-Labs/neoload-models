package com.neotys.neoload.model.scenario;

import java.util.Optional;

public interface LoadPolicy {
    Optional<Integer> getIterationNumber();
}
