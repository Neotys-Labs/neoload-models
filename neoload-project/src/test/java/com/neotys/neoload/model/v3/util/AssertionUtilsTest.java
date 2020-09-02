package com.neotys.neoload.model.v3.util;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.userpath.assertion.ContentAssertion;

public class AssertionUtilsTest {
    @Test
    public void constants() {
    	assertEquals("assertion_", AssertionUtils.PREFIX_NAME);
    }
    
    @Test
    public void normalyzeTest() {
    	final List<ContentAssertion> assertions = new ImmutableList.Builder<ContentAssertion>()
    			.add(ContentAssertion.builder()
    					.name("assertion_0")
    					.contains("contains_0")
    					.build())  
    			.add(ContentAssertion.builder()
    					.contains("contains_1")
    					.build())  
    			.add(ContentAssertion.builder()
    					.name("assertion_2")
    					.contains("contains_2")
    					.build())    
    			.add(ContentAssertion.builder()
    					.contains("contains_3")
    					.build())    
    			.build();
    	
    	final List<ContentAssertion> normalyzedAssertions = AssertionUtils.normalyze(assertions);
    	Assertions.assertThat(normalyzedAssertions.size()).isEqualTo(4);
    	Assertions.assertThat(normalyzedAssertions.get(0).getName()).isPresent();
    	Assertions.assertThat(normalyzedAssertions.get(0).getName().get()).isEqualTo("assertion_0");
    	Assertions.assertThat(normalyzedAssertions.get(1).getName()).isPresent();
    	Assertions.assertThat(normalyzedAssertions.get(1).getName().get()).isEqualTo("assertion_1");
    	Assertions.assertThat(normalyzedAssertions.get(2).getName()).isPresent();
    	Assertions.assertThat(normalyzedAssertions.get(2).getName().get()).isEqualTo("assertion_2");
    	Assertions.assertThat(normalyzedAssertions.get(3).getName()).isPresent();
    	Assertions.assertThat(normalyzedAssertions.get(3).getName().get()).isEqualTo("assertion_3");
    }
}
