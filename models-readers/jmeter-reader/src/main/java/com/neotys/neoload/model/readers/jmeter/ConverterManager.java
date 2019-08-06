package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.readers.jmeter.variable.VariableConverters;
import org.apache.jorphan.collections.HashTree;

public final class ConverterManager {

    private final StepConverters stepConverters;
    private final VariableConverters variableConverters;

    public ConverterManager() {
        this.stepConverters = new StepConverters();
        this.variableConverters = new VariableConverters();
    }

    public void convertElement(Object o, HashTree hahsTree){

    }
}
