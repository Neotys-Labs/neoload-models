package com.neotys.models.scenario;

import java.util.Optional;

public interface LoadPolicy {
    Optional<Integer> getIterationNumber();
}
