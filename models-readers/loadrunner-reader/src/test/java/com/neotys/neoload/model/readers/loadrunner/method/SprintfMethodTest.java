package com.neotys.neoload.model.readers.loadrunner.method;

import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.LOAD_RUNNER_VISITOR;
import static com.neotys.neoload.model.readers.loadrunner.LoadRunnerReaderTestUtil.METHOD_CALL_CONTEXT;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.readers.loadrunner.ImmutableMethodCall;
import com.neotys.neoload.model.repository.ImmutableSprintf;
import com.neotys.neoload.model.repository.Sprintf;

@SuppressWarnings("squid:S2699")
public class SprintfMethodTest {
	
	@Test		
	public void testSprintfGetElement() {		
		
		// sprintf(formattedVariable,"format_%s",rawVariable);
		Sprintf actualSprintf = (Sprintf) (new SprintfMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("\"sprintf\"")
				.addParameters("formattedVariable")
				.addParameters("\"format_%s\"")
				.addParameters("rawVariable")			
				.build(), METHOD_CALL_CONTEXT).get(0);
		Sprintf exprectedSprintf = ImmutableSprintf.builder()
				.name("sprintf")
				.variableName("formattedVariable")
				.format("\"format_%s\"")
				.args(ImmutableList.of("rawVariable"))
				.build();		
		assertEquals(exprectedSprintf, actualSprintf);
		
		// sprintf(formattedVariable,"format_%s",${Type});
		actualSprintf = (Sprintf) (new SprintfMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("\"sprintf\"")
				.addParameters("formattedVariable")
				.addParameters("\"format_%s\"")
				.addParameters("${Type}")			
				.build(), METHOD_CALL_CONTEXT).get(0);
		exprectedSprintf = ImmutableSprintf.builder()
				.name("sprintf")
				.variableName("formattedVariable")
				.format("\"format_%s\"")
				.args(ImmutableList.of("Type"))
				.build();		
		assertEquals(exprectedSprintf, actualSprintf);
		
		// sprintf(formattedVariable,"format_%s_%d",variable1, variable2);
		actualSprintf = (Sprintf) (new SprintfMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("\"sprintf\"")
				.addParameters("formattedVariable")
				.addParameters("\"format_%s_%d\"")
				.addParameters("variable1")			
				.addParameters("variable2")
				.build(), METHOD_CALL_CONTEXT).get(0);
		exprectedSprintf = ImmutableSprintf.builder()
				.name("sprintf")
				.variableName("formattedVariable")
				.format("\"format_%s_%d\"")
				.args(ImmutableList.of("variable1", "variable2"))
				.build();		
		assertEquals(exprectedSprintf, actualSprintf);
		
		// sprintf(formattedVariable,"{format_%s}",rawVariable);
		actualSprintf = (Sprintf) (new SprintfMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("\"sprintf\"")
				.addParameters("formattedVariable")
				.addParameters("\"{format_%s}\"")
				.addParameters("rawVariable")			
				.build(), METHOD_CALL_CONTEXT).get(0);
		exprectedSprintf = ImmutableSprintf.builder()
				.name("sprintf")
				.variableName("formattedVariable")
				.format("\"{format_%s}\"")
				.args(ImmutableList.of("rawVariable"))
				.build();		
		assertEquals(exprectedSprintf, actualSprintf);
		
		// sprintf(formattedVariable,"format_%d",i+1);
		actualSprintf = (Sprintf) (new SprintfMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("\"sprintf\"")
				.addParameters("formattedVariable")
				.addParameters("\"format_%d\"")
				.addParameters("i+1")			
				.build(), METHOD_CALL_CONTEXT).get(0);
		exprectedSprintf = ImmutableSprintf.builder()
				.name("sprintf")
				.variableName("formattedVariable")
				.format("\"format_%d\"")
				.args(ImmutableList.of("i+1"))
				.build();		
		assertEquals(exprectedSprintf, actualSprintf);		
		
		// sprintf(formattedVariable,"");
		actualSprintf = (Sprintf) (new SprintfMethod()).getElement(LOAD_RUNNER_VISITOR, ImmutableMethodCall.builder()
				.name("\"sprintf\"")
				.addParameters("formattedVariable")
				.addParameters("\"\"")			
				.build(), METHOD_CALL_CONTEXT).get(0);
		exprectedSprintf = ImmutableSprintf.builder()
				.name("sprintf")
				.variableName("formattedVariable")
				.format("\"\"")				
				.build();		
		assertEquals(exprectedSprintf, actualSprintf);		
	}

}
