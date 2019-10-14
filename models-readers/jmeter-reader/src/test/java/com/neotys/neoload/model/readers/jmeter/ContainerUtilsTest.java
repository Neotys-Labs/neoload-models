package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.v3.project.variable.ConstantVariable;
import com.neotys.neoload.model.v3.project.variable.Variable;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ContainerUtilsTest {

    @Test
    public void testGetterValue(){
        ContainerUtils.addKeyValue("Stagiaire","Thomas");
        assertEquals(ContainerUtils.getValue("${Stagiaire}"),"Thomas");

        Variable constantVariable = ConstantVariable.builder().build();
        List<Variable> expected = new ArrayList<>();
        expected.add(constantVariable);
        ContainerUtils.addVariable(expected);
        List<Variable> result = ContainerUtils.getVariableContainer();
        assertEquals(result,expected);
    }

}