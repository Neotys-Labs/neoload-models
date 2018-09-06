package com.neotys.neoload.model.readers.loadrunner.customaction;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.core.util.Lists;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class MappingValueUtilTest {
	
	private static final String METHOD_NAME = "methodName";

	@Test
	public void testParseMappingValue() {		
		final List<String> inputParameters = ImmutableList.of("valueArg0", "valueArg1", "valueArg2");
		final AtomicInteger counter = new AtomicInteger(0);		
		final Set<Integer> readIndex = new HashSet<>();	
		
		assertEquals("before arg0 after", MappingValueUtil.parseMappingValue(inputParameters, "before arg0 after", METHOD_NAME, counter, readIndex));
		assertEquals(0, readIndex.size());
				
		assertEquals("before valueArg0 after", MappingValueUtil.parseMappingValue(inputParameters, "before %%%arg0%%% after", METHOD_NAME, counter, readIndex));
		assertEquals(1, readIndex.size());
		assertEquals(0, readIndex.iterator().next().intValue());		
		
		assertEquals("- valueArg0 - valueArg0 -", MappingValueUtil.parseMappingValue(inputParameters, "- %%%arg0%%% - %%%arg0%%% -", METHOD_NAME, counter, readIndex));
		assertEquals(1, readIndex.size());
		assertEquals(0, readIndex.iterator().next().intValue());
		
		assertEquals("- valueArg0 - valueArg1 -", MappingValueUtil.parseMappingValue(inputParameters, "- %%%arg0%%% - %%%arg1%%% -", METHOD_NAME, counter, readIndex));
		assertEquals(2, readIndex.size());
		assertEquals(0, readIndex.toArray()[0]);
		assertEquals(1, readIndex.toArray()[1]);
		
		assertEquals("- valueArg0 - %%%arg3%%% -", MappingValueUtil.parseMappingValue(inputParameters, "- %%%arg0%%% - %%%arg3%%% -", METHOD_NAME, counter, readIndex));
		assertEquals(2, readIndex.size());
		assertEquals(0, readIndex.toArray()[0]);
		assertEquals(1, readIndex.toArray()[1]);
	}
	
	@Test
	public void testParseMappingValueVariableReturnValue() {		
		final List<String> inputParameters = Lists.newArrayList();
		final AtomicInteger counter = new AtomicInteger(0);		
		final Set<Integer> readIndex = new HashSet<>();
		final String value = MappingValueUtil.VARIABLE_RETURN_VALUE;
		String actualResult = MappingValueUtil.parseMappingValue(inputParameters, value, METHOD_NAME, counter, readIndex);
		assertEquals(METHOD_NAME+ "_1", actualResult);
		assertEquals(0, readIndex.size());
		actualResult = MappingValueUtil.parseMappingValue(inputParameters, value, METHOD_NAME, counter, readIndex);
		assertEquals(METHOD_NAME + "_2", actualResult);
		assertEquals(0, readIndex.size());
	}
	
	@Test
	public void testParseMappingEmptyValue() {	
		final AtomicInteger counter = new AtomicInteger(0);		
		final Set<Integer> readIndex = new HashSet<>();			
		assertEquals("", MappingValueUtil.parseMappingValue(Lists.newArrayList(), "", METHOD_NAME, counter, readIndex));
		assertEquals(0, readIndex.size());		
		assertEquals("", MappingValueUtil.parseMappingValue( ImmutableList.of("valueArg0", "valueArg1", "valueArg2"), "", METHOD_NAME, counter, readIndex));
		assertEquals(0, readIndex.size());		
	}
	
	@Test
	public void testParseMappingEmptyParametersList() {	
		final AtomicInteger counter = new AtomicInteger(0);		
		final Set<Integer> readIndex = new HashSet<>();			
		assertEquals("a", MappingValueUtil.parseMappingValue(Lists.newArrayList(), "a", METHOD_NAME, counter, readIndex));
		assertEquals(0, readIndex.size());		
		assertEquals("%%%arg0%%%", MappingValueUtil.parseMappingValue(Lists.newArrayList(), "%%%arg0%%%", METHOD_NAME, counter, readIndex));
		assertEquals(0, readIndex.size());		
	}

}
